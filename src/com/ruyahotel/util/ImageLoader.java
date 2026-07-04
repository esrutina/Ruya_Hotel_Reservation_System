package com.ruyahotel.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Utility to load and resize images for the application.
 */
public class ImageLoader {

    private static final String IMAGE_DIR = "src/com/ruyahotel/image/";

    public static ImageIcon loadIcon(String path, int width, int height) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        
        File file = new File(path);
        if (!file.exists()) {
            // Try in the image folder
            file = new File(IMAGE_DIR + path);
            if (!file.exists()) {
                // Try with just the filename
                file = new File(IMAGE_DIR + new File(path).getName());
                if (!file.exists()) {
                    return null;
                }
            }
        }
        
        ImageIcon original = new ImageIcon(file.getAbsolutePath());
        Image img = original.getImage();
        
        // Use original size if width or height is 0
        int targetWidth = width > 0 ? width : img.getWidth(null);
        int targetHeight = height > 0 ? height : img.getHeight(null);
        
        if (targetWidth <= 0 || targetHeight <= 0) {
            targetWidth = 380;
            targetHeight = 160;
        }
        
        Image scaled = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static ImageIcon loadPlaceholder(int width, int height, Color bg, String text) {
        return null;
    }
}
