package com.deprez;

import java.io.IOError;
import java.io.IOException;
import java.util.logging.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Driver {
    public static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
    private Database database;
    private Community community;
    private Store store;
    private UserAppReviews userAppReviews;
    private JFrame loginJFrame;
    private JFrame mainJFrame;
    private String currentUserName;

    private Driver() {
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        store = new Store(database.loadApps());
        userAppReviews = new UserAppReviews(database.loadUserAppReviews());
        setupLogger();

        /*
        try {
            // User tmp = new User(2, "William");
            // tmp.addAppReview(new UserAppReview(1, 9, "Game sucks."));
            // community.addUser(tmp);
            // store.addApp(new App(store.size(), "Battlefield"));
            // store.addApp(new App(store.size(), "Call of Duty"));

            //loop through users->appReviews and add the user into the apps user list

            //or create new class call UserApps which includes the Appreviews list
            //create methods to
            // getUsersOfApp(appId)
            // getAppsOfUser(userId)


        } catch (AlreadyExistsException e) {
            LOGGER.log(Level.WARNING, "Attempted to add an existing item to list...", e);
        }
         */
        loginJFrame = new JFrame("Login");
        createLoginWindow();
        mainJFrame = new JFrame("Main Menu");
    }

    // TODO: Setup elements method
    // TODO: Visibility switcher (E.g. switching from login -> main menu makes login invisible and main menu visible...)

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

    private void createLoginWindow() {
        loginJFrame.setSize(400, 320);
        loginJFrame.setLayout(new GridLayout(10, 10, 10, 10));
        loginJFrame.setLocationRelativeTo(null);
        loginJFrame.setResizable(false);
        loginJFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loginJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                loginJFrame.dispose();
            }
        });

        JLabel userJLabel = new JLabel("Username");
        loginJFrame.add(userJLabel);

        JTextField userJTextField = new JTextField("", 30);
        userJTextField.setToolTipText("Enter Username: ");
        loginJFrame.add(userJTextField);

        JButton loginJButton = new JButton("Login");
        loginJButton.addActionListener(actionEvent -> {
            System.out.println("Action: " + actionEvent);
            if (userJTextField.getText().matches(".*\\w.*")) {
                loginJFrame.dispose();
                try {
                    community.addUser(userJTextField.getText());
                } catch (AlreadyExistsException e) {
                    LOGGER.log(Level.FINE, "User already exists, settings 'currentUserName' to the field...", e);
                }
                currentUserName = userJTextField.getText();
                createMainMenuWindow();
            }
        });
        loginJFrame.add(loginJButton);

        JButton quitJButton = new JButton("Quit");
        quitJButton.addActionListener(actionEvent -> {
            quit(loginJFrame);
        });
        loginJFrame.add(quitJButton);

        loginJFrame.setVisible(true);
    }

    private void createMainMenuWindow() {
        mainJFrame.setSize(600, 400);
        mainJFrame.setLayout(new GridLayout(10, 10, 10, 10));
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setResizable(false);
        mainJFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                mainJFrame.dispose();
            }
        });

        JButton backJButton = new JButton("Logout");
        backJButton.addActionListener(actionEvent -> {
            // System.out.println("Action" + actionEvent);
            mainJFrame.dispose();
            currentUserName = null;
            createLoginWindow();
        });
        mainJFrame.add(backJButton);

        mainJFrame.setVisible(true);

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
