package com.deprez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Driver {
    private Database database;
    private Community community;
    private Store store;
    private GridBagConstraints gridBagConstraints;
    private JFrame jFrame;

    private Driver() {
        jFrame = new JFrame("Hello, World");
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        // store = new Store(database.loadApps());


    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.createDatabase();
        driver.createWindow();
    }

    private void createWindow() {
        jFrame.setSize(1024, 768);
        jFrame.setLayout(new GridLayout(10, 10, 10, 10));
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // super.windowClosing(windowEvent);
                jFrame.dispose();
                quit();
            }
        });

    }

    private void createDatabase() {
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
                });
    }

    private void quit() {
        database.saveUsers(community.getUsers());
        // database.saveApps(store.getApps());
        System.exit(0);
    }
}
