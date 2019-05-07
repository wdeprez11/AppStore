package com.deprez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.*;

/**
 * @author William Deprez
 * @version 1.0
 * May 17, 2019
 */
public class Driver {

    static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
    private LoginJFrame loginJFrame;
    private MMJFrame mmJFrame;
    private CommunityJFrame communityJFrame;
    // private JFrame loginJFrame;
    private JFrame storeJFrame;

    private Driver() {
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        store = new Store(database.loadApps());
        userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        setupLogger();
        loginJFrame = new LoginJFrame("Login");
        loginJFrame.setVisible(true);
        storeJFrame = new JFrame("Store");
        setupStore();
        // communityJFrame = new JFrame("Community");
        // setupComm();
    }

    private String currentUser;

    // private StoreJFrame storeJFrame;

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
            this.setLayout(new GridLayout(10, 10, 10, 10));
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
                    // super.windowClosing(windowEvent);
                    loginJFrame.dispose();
                }
            });

            loginJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (userJTextField.getText().matches(".*\\w.*")) {
                        loginJFrame.setVisible(false);
                        try {
                            community.addUser(userJTextField.getText());
                        } catch (AlreadyExistsException e) {
                            LOGGER.log(Level.INFO, "User already exists, setting 'currentUser' to the text field...", e);
                        }
                        currentUser = userJTextField.getText();
                        mmJFrame = new MMJFrame("Main Menu: " + currentUser, currentUser);
                        communityJFrame = new CommunityJFrame("Community: " + currentUser);
                        storeJFrame = new StoreJFrame("Store: " + currentUser);
                        mmJFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(LoginJFrame.this, "Please input a username that contains a non-whitespace character", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            quitJButton.addActionListener(actionEvent -> {
                quit(this);
            });
        }

        /**
         * Adds the components to the LoginJFrame.
         */
        private void addComponents() {
            this.add(userJLabel);
            this.add(userJTextField);
            this.add(loginJButton);
            this.add(quitJButton);
        }
    }

    private Database database;
    private Community community;
    private Store store;
    private UserAppReviews userAppReviews;

    private class CommunityJFrame extends JFrame {
        JLabel jLabel;

        public CommunityJFrame(String header) {
            super(header);

            jLabel = new JLabel();

            setFrameRules();

            addListeners();

            addComponents();
        }

        private void setFrameRules() {
            this.setSize(400, 320);
            this.setLayout(new GridLayout(10, 10, 10, 10));
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        private void addListeners() {

        }

        private void addComponents() {
            add(jLabel);
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

            usernameJLabel = new JLabel("Username: " + userName);
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
            this.setLayout(new GridLayout(10, 10, 10, 10));
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        /**
         * Adds the window listeners for the buttons on the MMJFrame.
         * TODO: Add listeners for Community, Store, Report, Help, and Log
         */
        private void addListeners() {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    super.windowClosing(windowEvent);
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

            /*
            helpJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                helpJFrame.setVisible(true);
            });*/

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
                // System.out.println("Action" + actionEvent);
                this.setVisible(false);
                currentUser = null;
                loginJFrame.setVisible(true);
            });
        }

        /**
         * Adds the components to the MMJFrame.
         */
        private void addComponents() {
            this.add(usernameJLabel);
            this.add(communityJButton);
            this.add(storeJButton);
            this.add(helpJButton);
            this.add(logJButton);
            this.add(backJButton);
        }
    }

    // TODO: Add other inner classes for JFrame windows.

    private class StoreJFrame extends JFrame {
        StoreJFrame(String header) {
            super(header);
        }
    }

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

    public static void main(String[] args) {
        Driver driver = new Driver();
        // setupLogger();
        driver.save();
        // driver.createLoginWindow();
        // driver.createDatabase();
        // driver.createWindow();
    }


    private void setupStore() {
    }

    private void createDatabase() {
        database.createUserTable();
        // database.createAppTable(); TODO: Finish user table then will get to doing app table.
        // database.createUserAppTable(); TODO: Link tables together
        /*
        database.createTable("user_tb",
                new String[]{"userId", "userName"},
                new String[]{"INTEGER", "VARCHAR(50)"},
                new String[][]{
                        {"PRIMARY KEY", "AUTOINCREMENT"},
                        {"NOT NULL"}
                });
        database.createTable("app_tb",
                new String[]{"appId", "appName", "creatorId"},
                new String[]{"INTEGER", "VARCHAR(64)", "INTEGER"},
                new String[][]{
                        {"PRIMARY KEY", "AUTOINCREMENT"},
                        {"NOT NULL"},
                        {}
                });
        database.createTable("userapp_tb",
                new String[]{"appId", "userId", "review"},
                new String[]{"INTEGER", "INTEGER", "VARCHAR(2000)"},
                new String[][]{
                        {},
                        {},
                        {}
                });*/
    }

    private void save() {
        database.saveUsers(community.getUsers());
        database.saveApps(store.getApps());
        database.saveUserAppReviews(community.getUsers());
        // database.saveApps(store.getApps());
        // database.saveUserApp(user);
    }

    private void quit(JFrame jFrame) {
        save();
        jFrame.dispose();
        LOGGER.log(Level.INFO, Community.class.getName() + " list\n" + community.toString());
        LOGGER.log(Level.INFO, Store.class.getName() + " list\n" + store.toString());
        LOGGER.log(Level.INFO, "\nDONE\nDONE\nDONE\n");
        System.exit(0);
    }
}
