package org.rainfall.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Produces a JFrame providing detailed information about how to use the
 * program.
 *
 * @author Kieran
 */
public class AboutInterface extends JFrame implements ActionListener {
    
    private static final String GUI_TITLE = "How to use this software";
    private static final int DEFAULT_CLOSE_OP = JFrame.DISPOSE_ON_CLOSE;
    
    private JButton closeButton = new JButton("Close");

    /**
     * Main constructor used to create and visualise the JFrame
     */
    public AboutInterface() {
        setTitle(GUI_TITLE);
        setDefaultCloseOperation(DEFAULT_CLOSE_OP);

        initComponents();

        pack();
        setVisible(true);
    }

    private void initComponents() {
        JPanel bgPanel = new JPanel(new BorderLayout());
        bgPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(bgPanel);

        JLabel howToLabel = new JLabel("<html><center>How do I use this <i>darn</i> software!?</center></html>");
        howToLabel.setFont(new Font( "Tiresias PCFont Z", Font.BOLD, 28 ));
        bgPanel.add(howToLabel, BorderLayout.NORTH);

        JTextPane descText = new JTextPane( );
        descText.setPreferredSize(new Dimension(530, 400));
        descText.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        descText.setEditable(false);
        descText.setContentType("text/html");
        descText.setDisabledTextColor(Color.BLACK);
        descText.setBackground(Color.WHITE);
        descText.setText(HELP_TEXT);
        JScrollPane scrollBar = new JScrollPane(descText );
        descText.setCaretPosition(0); // scroll to the top
        bgPanel.add(scrollBar, BorderLayout.CENTER);

        JPanel bottomCenteredPanel = new JPanel(new BorderLayout());
        closeButton.addActionListener(this);
        bottomCenteredPanel.add(closeButton, BorderLayout.CENTER);
        bgPanel.add(bottomCenteredPanel, BorderLayout.SOUTH);
    }

    /**
     * Listens for actions on specific GUI components
     * @param e ActionEvent for a given user interaction
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton)
            dispose();
    }

    private static final String HELP_TEXT =
            "<html>"
            + "<center><b>Author:</b> Kieran Brahney<br /><b>Created:</b> May 2011<br /></center><br />"
            + "The purpose of this software is to analyse specific rainfall data files. The software "
            + "can <b>ONLY</b> cope with data provided by Hazelrigg Weather Station, Lancaster University. "
            + "This software aims to make analysing the file much easier, picking out key pieces of information "
            + "over specific time periods. For example, calculating the average rainfall for a particular year, "
            + "or the wettest day of a particular year.<br /><br />"
            + "Guide to using this software:"
            + "<ol>"
            + "<li>Click the 'File' menu and locate your Lancaster rainfall data file.</li>"
            + "<li>Select a calculation type. </li>"
            + "<li>Select the time period you would like to search for. For example, if I want the wettest day "
            + "of 1974 then SOLELY select the year leaving all other time periods blank. <b>Note the following "
            + "configuraton restrictions:</b>"
            + "<ul>"
            + "<li><i>Averages</i> - [Year], [Year, Month], and [Year, Month, Week]</li>"
            + "<li><i>Totals</i> - [Year], [Year, Month], [Year, Month, Week], and [Year, Donth, Day]</li>"
            + "<li><i>Wettest/Driest</li> - [Year], [Year, Month]"
            + "</ul>"
            + "<li>Hit the search button</li>"
            + "</ol>"
            + "</html>";

}
