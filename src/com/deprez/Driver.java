package com.deprez;

import javax.swing.*;

public class Driver extends JFrame {
    public static void main(String[] args) {
        new Driver().setVisible(true);
        // Test, checking if merge conflicts occur...
        System.out.println("Hello, World!");
    }

    private Driver() {}

    private static void quit() {}
}
