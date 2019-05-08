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
 * <p>
 * The main class of the project.
 * Accesses and writes the database.
 * Manages the user interface.
 * </p>
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

    // TODO: Move database to inner class.
    private Database database;

    /**
     * The list of users.
     */
    private Community community;

    /**
     * The list of apps.
     */
    private Store store;

    /**
     * The list of userAppReviews
     */
    private UserAppReviews userAppReviews;


    /**
     * Creates a Driver object.
     */
    private Driver() {
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        store = new Store(database.loadApps());
        userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        setupLogger();
        loginJFrame = new LoginJFrame("Login");
        loginJFrame.setVisible(true);
        storeJFrame = new StoreJFrame("Store");
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
     * Manages the elements of the CommunityJFrame.
     */
    private class CommunityJFrame extends JFrame {
        JLabel jLabel;
        JButton backJButton;
        JComboBox<User> userJComboBox;

        CommunityJFrame(String header) {
            super(header);

            setFrameRules();

            jLabel = new JLabel("Community");
            backJButton = new JButton("BACK");
            userJComboBox = new JComboBox<>();

            for (User user : community.getUsers()) {
                userJComboBox.addItem(user);
            }

            addListeners();

            addComponents();
        }

        private void setFrameRules() {
            this.setSize(1024, 768);
            this.setLayout(new GridLayout(10, 10, 10, 10));
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        private void addListeners() {
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });
        }

        private void addComponents() {
            this.add(jLabel);
            this.add(backJButton);
            this.add(userJComboBox);
        }
    }

    /**
     * Manages the elements of the StoreJFrame.
     */
    private class StoreJFrame extends JFrame {
        JLabel jLabel;
        JButton backJButton;
        JComboBox<App> appJComboBox;
        JButton addAppJButton;

        StoreJFrame(String header) {
            super(header);

            setFrameRules();

            jLabel = new JLabel("Store");
            backJButton = new JButton("BACK");
            appJComboBox = new JComboBox<>();
            for (App app : store.getApps()) {
                appJComboBox.addItem(app);
            }
            addAppJButton = new JButton("New Application");

            addListeners();

            addComponents();
        }

        private void setFrameRules() {
            this.setSize(1024, 768);
            this.setLayout(new GridLayout(10, 10, 10, 10));
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        private void addListeners() {
            backJButton.addActionListener(actionEvent -> {
                this.setVisible(false);
                mmJFrame.setVisible(true);
            });

            addAppJButton.addActionListener(actionEvent -> {
                JTextField appNameJTextField = new JTextField("", 30);
                JOptionPane.showMessageDialog(this, appNameJTextField);
            });
        }

        private void addComponents() {
            this.add(jLabel);
            this.add(backJButton);
            this.add(appJComboBox);
            this.add(addAppJButton);
        }
    }

}
