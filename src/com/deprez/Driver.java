package com.deprez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Driver {
    private Database database;
    private Community community;
    private Store store;
    private GridBagConstraints gridBagConstraints;
    private JFrame loginJFrame;
    private JFrame mainJFrame;

    private Driver() {
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        // store = new Store(database.loadApps());

        loginJFrame = new JFrame("Login");
        mainJFrame = new JFrame("Main Menu");
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.createLoginWindow();
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
            loginJFrame.dispose();
            createMainMenuWindow();
        });
        loginJFrame.add(loginJButton);

        JButton quitJButton = new JButton("Quit");
        quitJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Quitting ...");
                quit(loginJFrame);
            }
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
            System.out.println("Action" + actionEvent);
            mainJFrame.dispose();
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

    private void quit(JFrame jFrame) {
        database.saveUsers(community.getUsers());
        jFrame.dispose();
        // database.saveApps(store.getApps());
        System.out.println("die");
        System.exit(0);
    }
}
