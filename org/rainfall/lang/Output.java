package org.rainfall.lang;

import javax.swing.JOptionPane;

/**
 *
 * @author Kieran
 * @version 0.01
 * File: Output.java
 * Created: 16:05:23
 */
public class Output
{

    /**
     * Prints a given message to console
     * @param message String to send to console
     */
    public static void print(String message)
    {
        System.out.println(message);
    }

    /**
     * Prints a given message in the error stream of the console
     * @param message String to send to error stream
     */
    public static void error(String message)
    {
        System.err.println(message);
    }

    /**
     * Opens a message dialog box displaying a given message
     * @param message String to send to the user interface
     */
    public static void errorToGUI(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

}
