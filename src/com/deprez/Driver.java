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
        community = new Community(database.loadUsers());
        store = new Store(database.loadApps());
        userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        loginJFrame = new LoginJFrame("Login");
        loginJFrame.setVisible(true);
        storeJFrame = new StoreJFrame("Store");
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
        } catch ( IOException e ) {
            LOGGER.log(Level.WARNING, "Failed to create log file", e);
        }
    
        LOGGER.log(Level.FINEST, "NEW INSTANCE!");
    }
    
    /**
     * Initializes the program.
     *
     * @param args command line arguments.
     */
    public static void main( String[] args ) {
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
    private void quit( JFrame jFrame ) {
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
        LoginJFrame( String header ) {
            super(header);
            
            LOGGER.log(Level.FINE, "LoginJFrame instantiated.");
            
            setFrameRules();
            
            userJLabel = new JLabel("Username");
            userJTextField = new JTextField("", 30);
            loginJButton = new JButton("Login");
            quitJButton = new JButton("Quit");
            
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
                public void windowClosing( WindowEvent windowEvent ) {
                    quit(loginJFrame);
                }
            });
    
            userJTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed( KeyEvent e ) {
                    if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                        login();
                    }
                }
            });
    
            loginJButton.addActionListener(actionEvent -> login());
    
            quitJButton.addActionListener(actionEvent -> quit(this));
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
            if ( userJTextField.getText().trim().matches(".*\\w.*") ) {
                loginJFrame.setVisible(false);
                community.addUser(userJTextField.getText());
                currentUser = userJTextField.getText();
                mmJFrame = new MMJFrame("Main Menu: " + currentUser, currentUser);
                communityJFrame = new CommunityJFrame("Community: " + currentUser);
                storeJFrame = new StoreJFrame("Store: " + currentUser);
                mmJFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginJFrame.this, "Please input a username that contains a non-whitespace character", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        /**
         * Adds the components to the LoginJFrame.
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            this.add(userJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(userJTextField, gridBagConstraints);
    
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            this.add(loginJButton, gridBagConstraints);
    
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            this.add(quitJButton, gridBagConstraints);
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
        MMJFrame( String header, String userName ) {
            super(header);
            
            LOGGER.log(Level.FINE, "New Main Menu instantiation.");
            
            setFrameRules();
            
            usernameJLabel = new JLabel("Welcome " + userName);
            communityJButton = new JButton("Community");
            storeJButton = new JButton("Store");
            showAllJButton = new JButton("Show all");
            helpJButton = new JButton("Help");
            logJButton = new JButton("Log");
            backJButton = new JButton("Logout");
            
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
                public void windowClosing( WindowEvent windowEvent ) {
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
                JOptionPane.showMessageDialog(this, "This program will implement a database of Users, Apps, and Reviews to make up a social app store.\n " +
                        "It tracks a user’s owned apps, existing apps, and reviews of that app.\n" +
                        "This creates a useful store and essentially it’s own digital marketplace where anyone can create their own applications.\n" +
                        "My list of apps will be contained within a store object, while my users are contained within my community object.");
            });
    
            logJButton.addActionListener(actionEvent -> {
                String line;
                StringBuilder stringBuilder = new StringBuilder();
        
                try {
                    String filename = "appstore.log";
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
            
                    while ( ( line = bufferedReader.readLine() ) != null ) {
                        stringBuilder.append(line + '\n');
                    }
            
                    bufferedReader.close();
                } catch ( IOException ex ) {
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
        
                for ( String[] arr : userAppReviews.toTable(community, store) ) {
                    for ( String str : arr ) {
                        System.out.print(str + ", ");
                    }
                    System.out.println();
                }
        
                JTable jTable = new JTable(
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
        
                JScrollPane jScrollPane = new JScrollPane(jTable);
        
                showall.add(jScrollPane);
        
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
        
                // TODO: showall.add() second table for Users that have no app, if a user creates an app they own the app, so each app has a user, but every user does not have an app.
        
                JOptionPane.showMessageDialog(this, showall);
            });
        }
        
        /**
         * Adds the components to the MMJFrame.
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            //gridBagConstraints.weightx = 1.0;
    
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
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
         * The list of users containing {@link com.deprez.Community}
         *
         * @see com.deprez.User
         * @see com.deprez.Community
         */
        JList<User> userJList;
        
        /**
         * The list model for {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        DefaultListModel<User> defaultListModel;
        
        /**
         * The list of apps for the selected {@link com.deprez.User} from {@link com.deprez.Driver.CommunityJFrame#userJList}
         */
        JList appJList;
        
        /**
         * The split pane including {@link com.deprez.Driver.CommunityJFrame#userJList}, {@link
         * com.deprez.Driver.CommunityJFrame#deleteUserJButton}, and {@link com.deprez.Driver.CommunityJFrame#userJTextArea}
         */
        JSplitPane jSplitPane;
        
        /**
         * The button to delete a user from {@link com.deprez.Driver.CommunityJFrame#userJList} and {@link
         * com.deprez.Community}
         *
         * @see com.deprez.User
         * @see com.deprez.Community
         */
        JButton deleteUserJButton;
        
        /**
         * Displays the information about the user selected in {@link com.deprez.Driver.CommunityJFrame#userJList}
         *
         * @see com.deprez.User
         * @see com.deprez.Community
         */
        JTextArea userJTextArea;
        
        /**
         * Creates a CommunityJFrame object.
         *
         * @param header the window's header
         */
        CommunityJFrame( String header ) {
            super(header);
            
            setFrameRules();
            
            communityJLabel = new JLabel("Community: " + header);
            backJButton = new JButton("BACK");
            userJList = new JList<>();
            defaultListModel = new DefaultListModel<>();
            userJList.setModel(defaultListModel);
            community.getUsers().forEach(user -> defaultListModel.addElement(user));
            appJList = new JList();
            
            jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(new JScrollPane(userJList));
            
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            userJTextArea = new JTextArea("");
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            jPanel.add(userJTextArea, gridBagConstraints);
            deleteUserJButton = new JButton("Delete user");
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            jPanel.add(deleteUserJButton, gridBagConstraints);
            jSplitPane.setRightComponent(new JScrollPane(jPanel));
            
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
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });
    
            userJList.addListSelectionListener(e -> {
                Object[] users = userAppReviews.getAppsOfUser(community.getUserId(userJList.getSelectedValue().toString())).toArray();
                String[] appNames = new String[users.length];
                for ( int i = 0; i < appNames.length; i++ ) {
                    appNames[i] = store.getAppName(i);
                }
            });
    
            userJList.getSelectionModel().addListSelectionListener(e -> {
                User user = userJList.getSelectedValue();
                userJTextArea.setText("UserId: " + user.getUserId() + "\nUsername: " + user.getUserName());
                deleteUserJButton.setText("Delete user: " + user.getUserName());
                deleteUserJButton.addActionListener(actionEvent -> community.removeUser(userJList.getSelectedValue()));
            });
        }
        
        /**
         * Adds the components to the {@link com.deprez.Driver.CommunityJFrame}
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(communityJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            this.add(jSplitPane, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
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
        
        JComboBox<App> appJComboBox;
        JButton createAppJButton;
        JButton addAppJButton;
        JList appJList;
        
        /**
         * Creates an object of {@link com.deprez.Driver.StoreJFrame}
         *
         * @param header the window's header
         */
        StoreJFrame( String header ) {
            super("Store: " + header);
            
            setFrameRules();
            
            storeJLabel = new JLabel("Store: " + currentUser);
            backJButton = new JButton("BACK");
            appJComboBox = new JComboBox<>();
            for ( App app : store.getApps() ) {
                appJComboBox.addItem(app);
            }
            
            appJList = new JList(store.getApps().stream().map(app -> app.getAppName()).collect(Collectors.toList()).toArray());
            
            createAppJButton = new JButton("New Application");
            
            addAppJButton = new JButton("Add to Account");
            
            addListeners();
            
            addComponents();
        }
        
        private void setFrameRules() {
            this.setSize(600, 400);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        
        private void addListeners() {
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });
            
            
            appJList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged( ListSelectionEvent e ) {
                    // System.out.println(userJList.getSelectedIndex());
                    //`appJList.remove
                }
            });
            
            createAppJButton.addActionListener(actionEvent -> {
                JTextField appNameJTextField = new JTextField("", 30);
                //JOptionPane.showMessageDialog(this, appNameJTextField);
                while ( true ) {
                    String appName = JOptionPane.showInputDialog(this, "Input app name: ");
                    if ( appName != null && appName.matches(".*\\w.*") && !( store.hasApp(appName) >= 0 ) ) {
                        store.addApp(appName);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this, "Please input an app name that contains a non-whitespace character", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
            });
            
            addAppJButton.addActionListener(actionEvent -> {
                /* community.getUser(currentUser).addUserApp(appJList.getSelectedValue());
                System.out.println(appJList.getSelectedValue()); */
                int userId = community.getUser(currentUser).getUserId() - 1;
                int appId = store.getAppId(appJList.getSelectedValue().toString()).getAppId();
                // System.out.println("Adding (" + userId + "," + appId + ")");
                
                if ( userAppReviews.userHasApp(userId, appId) ) {
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId - 1).toString() + "' wasn't able to be added to: '" + currentUser + "',\n" + currentUser + " already owns " + appJList.getSelectedValue(), "App failed to add", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.WARNING, "Failed to add app : '" + store.getApps().get(appId - 1).toString() + "' to " + currentUser + ",\n" + currentUser + " already owns the app.");
                } else {
                    userAppReviews.addUserAppReview(userId, appId);
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId - 1).toString() + "' was successfully added to: '" + currentUser + "'", "App Added", JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.log(Level.INFO, "App '" + store.getApps().get(appId - 1).toString() + "' added to " + currentUser + ".");
                }
            });
        }
        
        /**
         * Adds the components to {@link com.deprez.Driver.StoreJFrame}
         */
        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            this.add(storeJLabel, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(backJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            this.add(appJList, gridBagConstraints);
    
            gridBagConstraints.gridx = 1;
            this.add(addAppJButton, gridBagConstraints);
    
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            this.add(createAppJButton, gridBagConstraints);
        }
    }
    
}
