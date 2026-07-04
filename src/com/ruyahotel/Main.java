package com.ruyahotel;

import com.ruyahotel.ui.MainFrame;

import javax.swing.*;

/**
 * Entry point for Ruya Hotel Management System.
 */
public class Main {
    public static void main(String[] args) {

        // Set system properties for better rendering
        System.setProperty("sun.java2d.uiScale", "1");
        System.setProperty("awt.useSystemAAFontSettings", "on");

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);

        });
    }
}
