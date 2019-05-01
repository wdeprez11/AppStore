package com.deprez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Driver extends JFrame {
    private Database database;
    private Community community;
    private Store store;
    private GridBagConstraints gridBagConstraints;

    private Driver() {
        super("Hello, World!");
        database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        community = new Community(database.loadUsers());
        // store = new Store(database.loadApps());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;


        setSize(1024, 768);
        setLayout(new GridLayout(10, 10, 10, 10));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // super.windowClosing(windowEvent);
                // TODO: Save data prior to exiting.
                dispose();
                quit();
            }
        });

        JLabel jLabel1 = new JLabel("Hello, JFrame!");

        add(jLabel1);

        JComboBox<String> jComboBox = new JComboBox();

        JButton jButton = new JButton("JButton");
        add(jButton, gridBagConstraints);

        for(User user : community.getUsers()) {
            jComboBox.addItem(user.toString());
        }
        add(jComboBox, gridBagConstraints);

    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.setVisible(true);

        /*
        community.addUser("William");
        community.addUser("George");
         */

        /*
        database.createTable("user_tb",
                new String[]{"userId", "userName"},
                new String[]{"INTEGER", "varchar(50)"},
                new String[][]{
                        {"primary key", "autoincrement"},
                        {"NOT NULL"}
                });
         */
    }

    private void quit() {
        database.saveUsers(community.getUsers());
        System.exit(0);
    }
}
