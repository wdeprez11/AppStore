package com.deprez;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * <p>
 * The main class of the project.
 * Accesses and writes the database.
 * Manages the user interface.
 * </p>
 *
 * @author William Deprez
 * @version 1.0
 */
public class Driver {

    /**
     * The static logger for the package.
     */
    static final Logger LOGGER = Logger.getLogger(Driver.class.getName());

    /**
     * The member declaration of the inner LoginJFrame class.
     */
    private LoginJFrame loginJFrame;

    /**
     * The member declaration of the inner MMJFrame class.
     */
    private MMJFrame mmJFrame;

    /**
     * The member declaration of the inner CommunityJFrame class.
     */
    private CommunityJFrame communityJFrame;

    /**
     * The member declaration of the inner StoreJFrame class.
     */
    private StoreJFrame storeJFrame;

    /**
     * The current User object declared within the LoginJFrame class.
     */
    private String currentUser;

    private Database database;

    /**
     * The list of users.
     */
    private Community community;

    /**
     * The list of apps.
     */
    private Store store;

    private UserAppReviews userAppReviews;

    /**
     * Creates a Driver object.
     */
    private Driver() {
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        store = new Store(database.loadApps());
        //userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        userAppReviews = new UserAppReviews();
        setupLogger();
        loginJFrame = new LoginJFrame("Login");
        loginJFrame.setVisible(true);
        storeJFrame = new StoreJFrame("Store");
    }

    /**
     * Initializes the logging system.
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
     */
    private void save() {
        database.saveUsers(community.getUsers());
        database.saveApps(store.getApps());
        database.saveUserAppReviews(community.getUsers());
    }

    /**
     * Calls methods to avoid repeated code.
     *
     * @param jFrame the JFrame to destroy, instead of duplicate code.
     */
    private void quit(JFrame jFrame) {
        save();
        jFrame.dispose();
        LOGGER.log(Level.INFO, Community.class.getName() + " list\n" + community.toString());
        LOGGER.log(Level.INFO, Store.class.getName() + " list\n" + store.toString());
        LOGGER.log(Level.INFO, "\nDONE\nDONE\nDONE\n");
        System.exit(0);
    }

    /**
     * Manages the elements of the LoginJFrame.
     */
    private class LoginJFrame extends JFrame {
        JLabel userJLabel;
        JTextField userJTextField;
        JButton loginJButton;
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

            loginJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    login();
                }
            });

            quitJButton.addActionListener(actionEvent -> {
                quit(this);
            });
        }

        private void login() {
            if (userJTextField.getText().trim().matches(".*\\w.*")) {
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

            //Container container = this.getContentPane();

            GridBagConstraints gridBagConstraints = new GridBagConstraints();


            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            //gridBagConstraints.weightx = 1.0;

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
     * Manages the elements of the MMJFrame.
     */
    private class MMJFrame extends JFrame {
        JLabel usernameJLabel;
        JButton communityJButton;
        JButton storeJButton;
        JButton helpJButton;
        JButton logJButton;
        JButton backJButton;

        MMJFrame(String header, String userName) {
            super(header);

            LOGGER.log(Level.FINE, "New Main Menu instantiation.");

            setFrameRules();

            usernameJLabel = new JLabel("Welcome " + userName);
            communityJButton = new JButton("Community");
            storeJButton = new JButton("Store");
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
                public void windowClosing(WindowEvent windowEvent) {
                    //mmJFrame.dispose();
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
                // this.setVisible(false);
                String line;
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    String filename = "appstore.log";
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + '\n');
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
            this.add(helpJButton, gridBagConstraints);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            this.add(logJButton, gridBagConstraints);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 5;
            this.add(backJButton, gridBagConstraints);
        }
    }

    private class ProfileJFrame extends JFrame {
        JLabel usernameJLabel;
        JButton closeJButton;

        ProfileJFrame(String header) {
            super("Profile: " + header);

            setFrameRules();

            // instantiate class members
            usernameJLabel = new JLabel(header);
            closeJButton = new JButton("close");

            addListeners();

            addComponents();
        }

        private void setFrameRules() {
            this.setSize(1024, 768);
            this.setLayout(new GridBagLayout());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        private void addListeners() {

        }

        private void addComponents() {
            this.add(usernameJLabel);
            this.add(closeJButton);
        }
    }

    /**
     * Manages the elements of the CommunityJFrame.
     */
    private class CommunityJFrame extends JFrame {
        JLabel jLabel;
        JButton backJButton;
        // JComboBox<User> userJComboBox;
        JList userJList;
        JList appJList;

        CommunityJFrame(String header) {
            super(header);

            setFrameRules();

            jLabel = new JLabel("Community");
            backJButton = new JButton("BACK");
            // userJComboBox = new JComboBox<>();
            userJList = new JList(community.getUsers().stream().map(user -> user.getUserName()).collect(Collectors.toList()).toArray());
            appJList = new JList(new Object[]{new App(1, "Hello, World!")});

            /*
            for (User user : community.getUsers()) {
                userJComboBox.addItem(user);
            }
            */

            addListeners();

            addComponents();
        }


        private void setFrameRules() {
            this.setSize(1024, 768);
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

            userJList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // System.out.println(userJList.getSelectedIndex());
                    //`appJList.remove
                    /*
                    TODO!!!
                    appJList.setListData(
                            community.getUsers().get(userJList.getSelectedIndex()).getUserApps().toArray().length == 0 ?
                                    new Object[]{"This user has no apps"}
                                    :
                                    community.getUsers().get(userJList.getSelectedIndex()).getUserApps().toArray()
                    );
                     */
                    // TODO System.out.println(userAppReviews.getAppsOfUser(community.getUserId(currentUser)));
                    Object[] users = userAppReviews.getAppsOfUser(community.getUserId(userJList.getSelectedValue().toString())).toArray();
                    String[] appNames = new String[users.length];
                    for (int i = 0; i < appNames.length; i++) {
                        appNames[i] = store.getApp(i);
                    }
                    //System.out.println(appNames);

                    appJList.setListData(users.length == 0 ? new Object[]{"null"} : appNames);

                }
            });
        }

        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            this.add(jLabel, gridBagConstraints);

            /*
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            this.add(userJComboBox, gridBagConstraints);
            */

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            this.add(userJList, gridBagConstraints);

            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            this.add(appJList, gridBagConstraints);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
            this.add(backJButton, gridBagConstraints);

        }
    }

    /**
     * Manages the elements of the StoreJFrame.
     */
    private class StoreJFrame extends JFrame {
        JLabel jLabel;
        JButton backJButton;
        JComboBox<App> appJComboBox;
        JButton createAppJButton;
        JButton addAppJButton;
        JList appJList;

        StoreJFrame(String header) {
            super("Store: " + header);

            setFrameRules();

            jLabel = new JLabel("Store: " + currentUser);
            backJButton = new JButton("BACK");
            appJComboBox = new JComboBox<>();
            for (App app : store.getApps()) {
                appJComboBox.addItem(app);
            }

            appJList = new JList(store.getApps().stream().map(app -> app.getAppName()).collect(Collectors.toList()).toArray());

            createAppJButton = new JButton("New Application");

            addAppJButton = new JButton("Add to Account");

            addListeners();

            addComponents();
        }

        private void setFrameRules() {
            this.setSize(1024, 768);
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
                public void valueChanged(ListSelectionEvent e) {
                    // System.out.println(userJList.getSelectedIndex());
                    //`appJList.remove
                }
            });

            createAppJButton.addActionListener(actionEvent -> {
                JTextField appNameJTextField = new JTextField("", 30);
                //JOptionPane.showMessageDialog(this, appNameJTextField);
                while (true) {
                    String appName = JOptionPane.showInputDialog(this, "Input app name: ");
                    if (appName != null && appName.matches(".*\\w.*") && !(store.hasApp(appName) >= 0)) {
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
                int userId = community.getUser(currentUser).getUserId();
                int appId = store.getApp(appJList.getSelectedValue().toString()).getAppId();
                // System.out.println("Adding (" + userId + "," + appId + ")");

                if (userAppReviews.userHasApp(userId, appId)) {
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId - 1).toString() + "' wasn't able to be added to: '" + currentUser + "',\n" + currentUser + " already owns " + appJList.getSelectedValue(), "App failed to add", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.WARNING, "Failed to add app : '" + store.getApps().get(appId - 1).toString() + "' to " + currentUser + ",\n" + currentUser + " already owns the app.");
                } else {
                    userAppReviews.addUserAppReview(userId, appId);
                    JOptionPane.showMessageDialog(this, "App '" + store.getApps().get(appId - 1).toString() + "' was successfully added to: '" + currentUser + "'", "App Added", JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.log(Level.INFO, "App '" + store.getApps().get(appId - 1).toString() + "' added to " + currentUser + ".");
                }
            });
        }

        private void addComponents() {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            this.add(jLabel, gridBagConstraints);

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
