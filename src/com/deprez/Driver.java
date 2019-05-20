package com.deprez;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * <p>
 * The main class of the project. Accesses and writes the database. Manages the user interface.
 * </p>
 *
 * @author William Deprez
 * @version 1.0
 * @see com.deprez.User
 * @see com.deprez.Community
 * @see com.deprez.App
 * @see com.deprez.Store
 * @see com.deprez.UserAppReview
 * @see com.deprez.UserAppReviews
 * @see com.deprez.Database
 */
public class Driver {
    
    /**
     * The static logger for the package.
     *
     * @see java.util.logging
     */
    static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
    
    /**
     * The member declaration of the inner LoginJFrame class.
     *
     * @see com.deprez.User
     * @see com.deprez.Driver.MMJFrame
     */
    private LoginJFrame loginJFrame;
    
    /**
     * The member declaration of the inner MMJFrame class.
     *
     * @see com.deprez.Driver.CommunityJFrame
     * @see com.deprez.Driver.StoreJFrame
     * @see com.deprez.Driver.LoginJFrame
     */
    private MMJFrame mmJFrame;
    
    /**
     * The member declaration of the inner CommunityJFrame class.
     *
     * @see com.deprez.User
     * @see com.deprez.App
     */
    private CommunityJFrame communityJFrame;
    
    /**
     * The member declaration of the inner StoreJFrame class.
     *
     * @see com.deprez.App
     */
    private StoreJFrame storeJFrame;
    
    /**
     * The current User object declared within the LoginJFrame class.
     *
     * @see com.deprez.User
     */
    private String currentUser;
    
    /**
     * The Database instance.
     *
     * @see java.sql
     */
    private Database database;
    
    /**
     * The list of users.
     *
     * @see com.deprez.User
     */
    private Community community;
    
    /**
     * The list of apps.
     *
     * @see com.deprez.App
     */
    private Store store;
    
    /**
     * The Many-to-Many relationship pair of userId and appIds<br> This helps manage the Users, Apps, and the Review
     * score and detail associated with those pairs.<br> For example: userId = 1 adds app with appId = 5 to their
     * account In order to manage this pair of identifiers without the UserAppReview list, It would be necessary to add
     * the appId or userId to each {@link com.deprez.App} object and {@link com.deprez.User} object. Obviously this
     * would become incredibly inefficient, especially when attempting to remove either of those objects from {@link
     * com.deprez.Store} or {@link com.deprez.Community}
     *
     * @see com.deprez.Database
     * @see com.deprez.App
     * @see com.deprez.User
     * @see com.deprez.UserAppReview
     * @see com.deprez.Store
     * @see com.deprez.Community
     * @see com.deprez.UserAppReviews
     */
    private UserAppReviews userAppReviews;
    
    /**
     * Creates a Driver object.
     *
     * @see com.deprez.Database
     * @see com.deprez.Driver.LoginJFrame
     */
    private Driver() {
        setupLogger();
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community      = new Community(database.loadUsers());
        store          = new Store(database.loadApps());
        userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        loginJFrame    = new LoginJFrame("Login");
        storeJFrame    = new StoreJFrame("Store");
        loginJFrame.setVisible(true);
    }
    
    /**
     * Initializes the logging system.
     *
     * @see java.util.logging
     * @see java.util.logging.ConsoleHandler
     * @see java.io.FileReader
     * @see java.io.IOException
     */
    private static void setupLogger() {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);
        LOGGER.log(Level.INFO, "Driver Started.");
    
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
    
        LOGGER.addHandler(consoleHandler);
    
        try {
            FileHandler fileHandler = new FileHandler("appstore.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create log file", e);
        }
    
        LOGGER.log(Level.FINEST, "NEW INSTANCE!");
    }
    
    /**
     * Initializes the program.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.save();
    }
    
    /**
     * Calls the methods of the Database class which write the active lists to the tables.
     *
     * @see com.deprez.Database
     * @see com.deprez.Community
     * @see com.deprez.Store
     * @see com.deprez.UserAppReviews
     */
    private void save() {
        database.saveUsers(community.getUsers());
        database.saveApps(store.getApps());
        database.saveUserAppReviews(userAppReviews.getUserAppReviews());
    }
    
    /**
     * Calls methods to avoid repeated code.
     *
     * @param jFrame the JFrame to destroy, instead of duplicate code.
     *
     * @see com.deprez.Community
     * @see com.deprez.Store
     * @see com.deprez.UserAppReviews
     * @see Driver#setupLogger()
     * @see Driver#save()
     * @see Window#dispose()
     */
    private void quit(JFrame jFrame) {
        save();
        jFrame.dispose();
        LOGGER.log(Level.INFO, Community.class.getName() + " list\n" + community.toString());
        LOGGER.log(Level.INFO, Store.class.getName() + " list\n" + store.toString());
        LOGGER.log(Level.INFO, UserAppReviews.class.getName() + " list\n" + userAppReviews.toString());
        LOGGER.log(Level.INFO, "\nDONE\nDONE\nDONE\n");
        System.exit(0);
    }
    
    /**
     * Manages the elements of the LoginJFrame.
     *
     * @see com.deprez.Driver.MMJFrame
     * @see com.deprez.Community
     */
    private class LoginJFrame extends JFrame {
        /**
         * The label asking for the username.
         */
        JLabel userJLabel;
        
        /**
         * The text field to input a username.
         *
         * @see com.deprez.User
         */
        JTextField userJTextField;
        
        /**
         * The button that logs the user in
         */
        JButton loginJButton;
        
        /**
         * The button that quits the program
         *
         * @see Driver#save()
         * @see Driver#quit(JFrame)
         */
        JButton quitJButton;
        
        /**
         * Creates a LoginJFrame object.
         *
         * @param header the header to give to the JFrame constructor.
         */
        LoginJFrame(String header) {
            super(header);
            
            LOGGER.log(Level.FINE, "LoginJFrame instantiated.");
            
            setFrameRules();
    
            userJLabel     = new JLabel("Login");
            userJTextField = new JTextField("", 30);
            loginJButton   = new JButton("Login");
            quitJButton    = new JButton("Quit");
            
            userJTextField.setToolTipText("Enter Username");
            
            addListeners();
            
            addComponents();
        }
        
        /**
         * Declares the frame rules for the LoginJFrame.
         */
        private void setFrameRules() {
            this.setSize(400, 320);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        
        /**
         * Adds the window listeners for the buttons on the LoginJFrame.
         */
        private void addListeners() {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    quit(loginJFrame);
                }
            });
    
            userJTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        login();
                    }
                }
            });
    
            loginJButton.addActionListener(actionEvent -> login());
    
            quitJButton.addActionListener(actionEvent -> quit(this));
        }
    
        /**
         * Adds the components to the LoginJFrame.
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill   = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints.gridx  = 0;
            gridBagConstraints.gridy  = 0;
            userJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(userJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(userJTextField, gridBagConstraints);
    
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridx   = 0;
            gridBagConstraints.gridy   = 2;
            this.add(loginJButton, gridBagConstraints);
    
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridx   = 0;
            gridBagConstraints.gridy   = 3;
            this.add(quitJButton, gridBagConstraints);
        }
        
        /**
         * Logs the user into the program, sets {@link Driver#currentUser} to the username given in {@link
         * Driver.LoginJFrame#userJTextField} Creates the other JFrames specified for the input user.
         *
         * @see com.deprez.User
         * @see com.deprez.Driver.LoginJFrame
         * @see com.deprez.Driver.MMJFrame
         * @see com.deprez.Driver.CommunityJFrame
         * @see com.deprez.Driver.StoreJFrame
         */
        private void login() {
            if (userJTextField.getText().trim().matches(".*\\w.*")) {
                loginJFrame.setVisible(false);
                community.addUser(userJTextField.getText());
                currentUser     = userJTextField.getText();
                mmJFrame        = new MMJFrame("Main Menu: " + currentUser, currentUser);
                communityJFrame = new CommunityJFrame(currentUser);
                storeJFrame     = new StoreJFrame("Store: " + currentUser);
                mmJFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginJFrame.this, "Please input a username that contains a " +
                                                                "non-whitespace character", "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Manages the elements of the MMJFrame. essentially a wrapper class to contain a JFrame instance
     *
     * @see com.deprez.Driver.LoginJFrame
     * @see com.deprez.Driver.CommunityJFrame
     * @see com.deprez.Driver.StoreJFrame
     */
    private class MMJFrame extends JFrame {
        /**
         * The username label of the {@link com.deprez.Driver#currentUser}
         */
        JLabel usernameJLabel;
        
        /**
         * The button that changes window visibility to the CommunityJFrame
         *
         * @see com.deprez.Community
         * @see com.deprez.Driver.CommunityJFrame
         */
        JButton communityJButton;
        
        /**
         * The button that changes window visibility to the StoreJFrame
         *
         * @see com.deprez.Store
         * @see com.deprez.Driver.StoreJFrame
         */
        JButton storeJButton;
        
        /**
         * Shows all data in the database.
         *
         * @see com.deprez.Community
         * @see com.deprez.Store
         * @see com.deprez.Database
         */
        JButton showAllJButton;
        
        /**
         * Shows help for the user
         */
        JButton helpJButton;
        
        /**
         * Shows the entire log of the current machines runtime, as long as appstore.log was never deleted.
         */
        JButton logJButton;
        
        /**
         * Returns the view to the login splash, and sets {@link com.deprez.Driver#currentUser} to null.
         *
         * @see com.deprez.Driver.LoginJFrame
         */
        JButton backJButton;
        
        /**
         * Creates an instance of a MMJFrame,
         *
         * @param header   the window header
         * @param userName the userName of the user
         *                 <p>
         *                 TODO: Pass an actual User object
         */
        MMJFrame(String header, String userName) {
            super(header);
            
            LOGGER.log(Level.FINE, "New Main Menu instantiation.");
            
            setFrameRules();
    
            usernameJLabel   = new JLabel("Welcome " + userName);
            communityJButton = new JButton("Community");
            storeJButton     = new JButton("Store");
            showAllJButton   = new JButton("Show all");
            helpJButton      = new JButton("Help");
            logJButton       = new JButton("Log");
            backJButton      = new JButton("Logout");
            
            addListeners();
            
            addComponents();
        }
        
        /**
         * Declares the frame rules for the MMJFrame.
         */
        private void setFrameRules() {
            this.setSize(600, 400);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        
        /**
         * Adds the window listeners for the buttons on the MMJFrame.
         */
        private void addListeners() {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    quit(mmJFrame);
                }
            });
    
            communityJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                communityJFrame.setVisible(true);
            });
    
            storeJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                storeJFrame.setVisible(true);
            });
    
    
            helpJButton.addActionListener(actionEvent -> {
                JOptionPane.showMessageDialog(this, "This program will implement a database of Users, Apps, and " +
                                                    "Reviews to make up a social app store.\n " +
                                                    "It tracks a user’s owned apps, existing apps, and reviews of " +
                                                    "that app.\n" +
                                                    "This creates a useful store and essentially it’s own digital " +
                                                    "marketplace where anyone can create their own applications.\n" +
                                                    "My list of apps will be contained within a store object, while " +
                                                    "my users are contained within my community object.");
            });
    
            logJButton.addActionListener(actionEvent -> {
                String line;
                StringBuilder stringBuilder = new StringBuilder();
        
                try {
                    String filename = "appstore.log";
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
            
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append('\n');
                    }
            
                    bufferedReader.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.FINE, "", ex);
                }
                JTextArea tempJTextField = new JTextArea(25, 80);
                tempJTextField.setText(stringBuilder.toString());
                tempJTextField.setSize(new Dimension(1000, 300));
                tempJTextField.setPreferredSize(new Dimension(1000, tempJTextField.getPreferredSize().height));
                JScrollPane jScrollPane = new JScrollPane(tempJTextField);
                JOptionPane.showMessageDialog(this, jScrollPane);
            });
    
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                currentUser = null;
                loginJFrame.setVisible(true);
            });
    
            showAllJButton.addActionListener(actionEvent -> {
                JPanel showall = new JPanel();
                showall.setLayout(new GridBagLayout());
        
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
        
                JTable userAppJTable = new JTable(
                        userAppReviews.toTable(community, store)
                        ,
                        new String[]{
                                "userId",
                                "userName",
                                "appId",
                                "appName",
                                "reviewScore",
                                "reviewDetail"
                        }
                );
        
                JTable userJTable = new JTable(
                        community.toTable()
                        ,
                        new String[]{
                                "userId",
                                "userName",
                                }
                );
        
                JTable appJTable = new JTable(
                        store.toTable()
                        ,
                        new String[]{
                                "appId",
                                "appName"
                        }
                );
        
                gridBagConstraints.insets = new Insets(3, 3, 3, 3);
                JLabel userAppJLabel = new JLabel("userapp_tb");
                userAppJLabel.setHorizontalAlignment(JLabel.CENTER);
                JScrollPane userAppJScrollPane = new JScrollPane(userAppJTable);
        
                showall.add(userAppJLabel, gridBagConstraints);
                gridBagConstraints.gridy = 1;
                showall.add(userAppJScrollPane, gridBagConstraints);
        
                gridBagConstraints.gridx = 1;
        
                JLabel userJLabel = new JLabel("user_tb");
                userJLabel.setHorizontalAlignment(JLabel.CENTER);
                JScrollPane userJScrollPane = new JScrollPane(userJTable);
        
                gridBagConstraints.gridy = 0;
                showall.add(userJLabel, gridBagConstraints);
                gridBagConstraints.gridy = 1;
                showall.add(userJScrollPane, gridBagConstraints);
        
                gridBagConstraints.gridx = 2;
        
                JLabel appJLabel = new JLabel("app_tb");
                appJLabel.setHorizontalAlignment(JLabel.CENTER);
                JScrollPane appsJScrollPane = new JScrollPane(appJTable);
        
                gridBagConstraints.gridy = 0;
                showall.add(appJLabel, gridBagConstraints);
                gridBagConstraints.gridy = 1;
                showall.add(appsJScrollPane, gridBagConstraints);
        
                JOptionPane.showMessageDialog(this, showall, "SHOW ALL INFO", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        
        /**
         * Adds the components to the MMJFrame.
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill   = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.gridx  = 0;
            gridBagConstraints.gridy  = 0;
            usernameJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(usernameJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(communityJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            this.add(storeJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            this.add(showAllJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            this.add(helpJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 5;
            this.add(logJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 6;
            this.add(backJButton, gridBagConstraints);
        }
    }
    
    /**
     * Manages the elements of the CommunityJFrame.
     *
     * @see com.deprez.Community
     * @see com.deprez.Driver.MMJFrame
     */
    private class CommunityJFrame extends JFrame {
        
        /**
         * The label indicating the {@link com.deprez.Driver#currentUser}
         */
        JLabel communityJLabel;
        
        /**
         * The button which returns to the {@link com.deprez.Driver.MMJFrame}
         */
        JButton backJButton;
    
    
        /**
         * The {@link javax.swing.JLabel} for {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        JLabel userListJLabel;
        
        /**
         * The list of users containing {@link com.deprez.Community}
         *
         * @see com.deprez.User
         * @see com.deprez.Community
         */
        JList<User> userJList;
        
        /**
         * The list model for {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        DefaultListModel<User> defaultUserListModel;
    
        /**
         * The {@link javax.swing.JLabel} for {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        JLabel appListJLabel;
        
        /**
         * The list of apps for the selected {@link com.deprez.User} from
         * {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        JList<App> appJList;
    
        /**
         * The list model for {@link com.deprez.Driver.CommunityJFrame#appJList}
         */
        DefaultListModel<App> defaultAppListModel;
    
        JButton moreInfoJButton;
        
        /**
         * The button to delete a user from {@link com.deprez.Driver.CommunityJFrame#userJList} and {@link
         * com.deprez.Community}
         *
         * @see com.deprez.User
         * @see com.deprez.Community
         */
        JButton deleteUserJButton;
    
        /**
         * The user {@link javax.swing.JLabel}
         */
        JLabel userJLabel;
    
        /**
         * The app {@link javax.swing.JLabel}
         */
        JLabel appJLabel;
    
        java.util.List<Integer> appIds;
        
        /**
         * Creates a CommunityJFrame object.
         *
         * @param header the window's header
         */
        CommunityJFrame(String header) {
            super(header);
            
            setFrameRules();
    
            communityJLabel      = new JLabel("Community: " + header);
            backJButton          = new JButton("Back");
            userListJLabel       = new JLabel("Community List");
            userJList            = new JList<>();
            appListJLabel        = new JLabel("Empty App List");
            appJList             = new JList<>();
            defaultUserListModel = new DefaultListModel<>();
            defaultAppListModel  = new DefaultListModel<>();
            moreInfoJButton      = new JButton("More Info");
            deleteUserJButton    = new JButton("Delete User");
            
            community.getUsers().forEach(user -> defaultUserListModel.addElement(user));
            defaultAppListModel.addElement(new App(0, "null app"));
            
            addListeners();
            
            addComponents();
        }
        
        /**
         * Sets the frame rules for {@link com.deprez.Driver.CommunityJFrame}
         */
        private void setFrameRules() {
            this.setSize(600, 400);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        
        /**
         * Adds listeners to components of {@link com.deprez.Driver.CommunityJFrame}
         */
        private void addListeners() {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    quit(communityJFrame);
                }
            });
            
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });
    
            userJList.addListSelectionListener(e -> {
                java.util.List<Integer> userApps =
                        userAppReviews.getAppsOfUser(community.getUserId(userJList.getSelectedValue().toString()));
                java.util.List<String> appNames =
                        userApps.stream().map(appId -> store.getAppName(appId)).collect(Collectors.toList());
                /*
                for (String str : appNames) {
                    System.out.println(str);
                }
                 */
                /*
                for (int i = 0; i < appNames.length; i++) {
                    // appNames[i] = store.getAppName(i);
                    System.out.println(userApps[i]);
                }
                 */
            });
    
            userJList.getSelectionModel().addListSelectionListener(e -> {
                defaultAppListModel.clear();
                //java.util.List<Integer> appIds = userAppReviews.getAppsOfUser(userJList.getSelectedValue()
                // .getUserId());
                appIds = userAppReviews.getAppsOfUser(userJList.getSelectedValue().getUserId());
                appListJLabel.setText("Apps: " + userJList.getSelectedValue().getUserName());
                appIds.forEach(appId -> defaultAppListModel.addElement(store.getApp(appId)));
                if (!(appIds.size() > 0)) {
                    defaultAppListModel.addElement(new App(0, "This user has no apps."));
                }
            });
    
            moreInfoJButton.addActionListener(actionEvent -> {
                JPanel temp = new JPanel();
                temp.setLayout(new GridBagLayout());
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
                userJLabel = new JLabel("User: " + userJList.getSelectedValue().getUserName());
                appJLabel  = new JLabel("App: " + appJList.getSelectedValue().getAppName());
                
                gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor    = GridBagConstraints.CENTER;
                gridBagConstraints.gridx     = 0;
                gridBagConstraints.gridy     = 0;
                gridBagConstraints.insets    = new Insets(3, 3, 3, 3);
                gridBagConstraints.gridwidth = 2;
        
                temp.add(userJLabel, gridBagConstraints);
        
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                temp.add(appJLabel, gridBagConstraints);
        
                JPanel reviewJPanel = new JPanel();
                UserAppReview potentialReview =
                        userAppReviews.getUserAppReview(userJList.getSelectedValue().getUserId(),
                                                        appJList.getSelectedValue().getAppId());
                if (potentialReview.getAppReviewScore() == 0 || potentialReview.getAppReviewDetail().equals("")) {
                    reviewJPanel.add(new JLabel("review missing"));
                } else {
                    reviewJPanel.add(new JLabel("Score: " + potentialReview.getAppReviewScore()));
                    reviewJPanel.add(new JTextArea("Detail: " + potentialReview.getAppReviewDetail()));
                }
                JOptionPane.showMessageDialog(this, reviewJPanel, "Review", JOptionPane.PLAIN_MESSAGE);
            });
    
            deleteUserJButton.addActionListener(actionEvent -> {
                if (userJList.getSelectedValue().getUserName().equals(currentUser)) {
                    JOptionPane.showMessageDialog(this, "You can't delete your own user while logged in!", "Deletion " +
                                                                                                           "error!",
                                                  JOptionPane.ERROR_MESSAGE);
                } else {
                    community.removeUser(userJList.getSelectedValue());
                    defaultUserListModel.clear(); // TODO: NullPointer???
                    community.getUsers().forEach(user -> defaultUserListModel.addElement(user));
                    defaultAppListModel.clear();
                    defaultAppListModel.addElement(new App(0, "Please select a user."));
                }
            });
            
        }
        
        /**
         * Adds the components to the {@link com.deprez.Driver.CommunityJFrame}
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor    = GridBagConstraints.CENTER;
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 0;
            gridBagConstraints.insets    = new Insets(3, 3, 3, 3);
            gridBagConstraints.gridwidth = 2;
            communityJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(communityJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 1;
            gridBagConstraints.gridwidth = 1;
            userListJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(userListJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 2;
            gridBagConstraints.gridwidth = 1;
            userJList.setVisibleRowCount(10);
            userJList.setPreferredSize(new Dimension(200, 200));
            userJList.setModel(defaultUserListModel);
            this.add(new JScrollPane(userJList), gridBagConstraints);
    
            gridBagConstraints.gridx     = 1;
            gridBagConstraints.gridy     = 1;
            gridBagConstraints.gridwidth = 1;
            appListJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(appListJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx     = 1;
            gridBagConstraints.gridy     = 2;
            gridBagConstraints.gridwidth = 1;
            appJList.setVisibleRowCount(10);
            appJList.setPreferredSize(new Dimension(200, 200));
            appJList.setModel(defaultAppListModel);
            this.add(new JScrollPane(appJList), gridBagConstraints);
    
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 3;
            gridBagConstraints.gridwidth = 2;
            this.add(moreInfoJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            this.add(deleteUserJButton, gridBagConstraints);
    
            gridBagConstraints.gridx  = 0;
            gridBagConstraints.gridy  = 5;
            gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
            this.add(backJButton, gridBagConstraints);
        }
    }
    
    /**
     * Manages the elements of the StoreJFrame.
     *
     * @see com.deprez.Store
     * @see com.deprez.Driver.MMJFrame
     */
    private class StoreJFrame extends JFrame {
        
        /**
         * The label indicating the {@link com.deprez.Driver#currentUser}
         */
        JLabel storeJLabel;
        
        /**
         * The button which changes the visibility to {@link com.deprez.Driver.MMJFrame}
         *
         * @see com.deprez.Driver.MMJFrame
         */
        JButton backJButton;
    
        /**
         * The {@link javax.swing.JButton} which creates an input dialog to create new {@link com.deprez.App} objects
         * for {@link com.deprez.Store}
         */
        JButton createAppJButton;
    
        /**
         * The {@link javax.swing.JButton} which adds an app to a user account
         */
        JButton addAppJButton;
    
        /**
         * The {@link javax.swing.JButton} which adds an app to a user account if not already a pair, then asks for a
         * review score and review detail.
         */
        JButton reviewAppJButton;
    
        /**
         * The {@link javax.swing.JList} of {@link com.deprez.App} objects
         */
        JList<App> appJList;
    
        /**
         * The {@link javax.swing.DefaultListModel} for {@link com.deprez.Driver.CommunityJFrame#appJList}
         */
        DefaultListModel defaultAppListModel;
        
        /**
         * Creates an object of {@link com.deprez.Driver.StoreJFrame}
         *
         * @param header the window's header
         */
        StoreJFrame(String header) {
            super("Store: " + header);
            
            setFrameRules();
    
            storeJLabel = new JLabel("Store: " + currentUser);
            backJButton = new JButton("Back");
    
            appJList            = new JList<>();
            defaultAppListModel = new DefaultListModel<>();
            appJList.setModel(defaultAppListModel);
            store.getApps().forEach(app -> defaultAppListModel.addElement(app));
            
            createAppJButton = new JButton("New Application");
            
            addAppJButton = new JButton("Add to Account");
            
            addListeners();
            
            addComponents();
        }
    
        /**
         * Sets the frame rules for {@link com.deprez.Driver.StoreJFrame}
         */
        private void setFrameRules() {
            this.setSize(600, 400);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    
        /**
         * Adds the listeners to the components of {@link com.deprez.Driver.StoreJFrame}
         */
        private void addListeners() {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    quit(storeJFrame);
                }
            });
        
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });
        
            appJList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // System.out.println(userJList.getSelectedIndex());
                    //`appJList.remove
                }
            });
        
            createAppJButton.addActionListener(actionEvent -> {
                //JOptionPane.showMessageDialog(this, appNameJTextField);
                while (true) {
                    String appName = JOptionPane.showInputDialog(this, "Input app name: ");
                    if (appName == null) {
                        break;
                    } else if (appName.matches(".*\\w.*") && !(store.hasApp(appName) >= 0)) {
                        store.addApp(appName);
                        break;
                    } else if (store.hasApp(appName) >= 0) {
                        JOptionPane.showMessageDialog(this, "Please input a unique app name. '" + appName + "' was " +
                                                            "already found with identifier: " + store.getAppId(appName));
                    } else {
                        JOptionPane.showMessageDialog(this, "Please input an app name that contains a non-whitespace " +
                                                            "character", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            
                defaultAppListModel.clear();
                store.getApps().forEach(app -> defaultAppListModel.addElement(app));
            });
        
            addAppJButton.addActionListener(actionEvent -> {
                /* community.getUser(currentUser).addUserApp(appJList.getSelectedValue());
                System.out.println(appJList.getSelectedValue()); */
            
                //STORE - add app to this current user account
                int userId = community.getUser(currentUser).getUserId() - 1;
                int appId = store.getAppId(appJList.getSelectedValue().toString()) - 1;
                /*
                System.out.println("Adding (" + userId + ":" + community.getUserName(userId) + "," + appId + ":" +
                store.getAppName(appId) + ")");
                System.out.println(appJList.getSelectedValue().toString());
                 */
            
            
                if (userAppReviews.userHasApp(userId, appId)) {
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId).toString() + "' " +
                                                        "wasn't able to be added to: '" + currentUser + "', " + currentUser + " already owns " + appJList.getSelectedValue(), "App failed to add", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.WARNING, "Failed to add app : '" + store.getApps().get(appId).toString() +
                                              "' to " + currentUser + ",\n" + currentUser + " already owns the app.");
                } else {
                    userAppReviews.addUserAppReview(userId, appId);
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId).toString() + "' was " +
                                                        "successfully added to: '" + currentUser + "'", "App Added",
                                                  JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.log(Level.INFO,
                               "App '" + store.getApps().get(appId).toString() + "' added to " + currentUser + ".");
                }
            });
            
            /*
            reviewAppJButton.addActionListener(actionEvent -> {
                int userId = community.getUser(currentUser).getUserId();
                int appId = store.getAppId(appJList.getSelectedValue().toString());
                
                if (userAppReviews.userHasApp(userId, appId)) {
                    UserAppReview reference = userAppReviews.getUserAppReview(userId, appId);
                    String score;
                    String detail;
                    
                    String str1, str2 = "";
                    while (true) {
                        str1 = JOptionPane.showInputDialog("Please input an integer between 1 and 10");
                        if (str1.matches("\\n")) {
                            while (true) {
                                str2 = JOptionPane.showInputDialog("Please detail why you chose the score you did --
                                if you want to change your score, you need to press cancel now.");
                                if (str2.length() <= 20) {
                                    str2 = JOptionPane.showInputDialog("Please give more detail than 20 characters.");
                                }
                                if (str2.equals("null")) {
                                    break;
                                }
                            }
                        } else if (str1.equals("null")) {
                            break;
                        }
                        if (str2.equals("null")) {
                            break;
                        }
                    }
                    
                    reference.setAppReviewScore(Integer.parseInt(str1));
                    reference.setAppReviewDetail(str2);
                    //reference.setAppReviewScore(Integer.parseInt(reviewScore.getText()));
                    //reference.setAppReviewDetail(reviewDetail.getText());
                }
            });
             */
        }
        
        /**
         * Adds the components to {@link com.deprez.Driver.StoreJFrame}
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor    = GridBagConstraints.CENTER;
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 0;
            gridBagConstraints.insets    = new Insets(3, 3, 3, 3);
            gridBagConstraints.gridwidth = 2;
            storeJLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(storeJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(new JScrollPane(appJList), gridBagConstraints);
    
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 2;
            gridBagConstraints.gridwidth = 1;
            this.add(addAppJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            this.add(createAppJButton, gridBagConstraints);
    
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 3;
            this.add(backJButton, gridBagConstraints);
        }
    }
    
}
