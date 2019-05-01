package com.deprez;

import javax.swing.*;

public class Driver extends JFrame {
    public static void main(String[] args) {
        new Driver().setVisible(true);
    }

    private Driver() {
        super("Hello, World!");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel jLabel1 = new JLabel("Hello, JFrame!");
        add(jLabel1);
    }

    private static void quit() {}
}
