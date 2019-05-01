package com.deprez;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Driver extends JFrame {
    private Driver() {
        super("Hello, World!");
        setSize(1024, 768);
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
    }

    public static void main(String[] args) {
        new Driver().setVisible(true);
        Database database = new Database("jdbc:sqlite:appstore.db");
        database.connect();
        database.createTable("user_tb",
                new String[]{"userId", "userName"},
                new String[]{"INTEGER", "varchar(50)"},
                new String[][]{
                        {"primary key", "autoincrement"},
                        {"NOT NULL"}
                });
    }

    private static void quit() {
        System.exit(0);
    }
}
