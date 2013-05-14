package org.rainfall.gui;

import org.rainfall.gui.listeners.MenuListener;
import java.awt.*;
import javax.swing.*;
import org.rainfall.gui.listeners.ButtonListener;

/**
 * Produces the main interface used to interact and query the data set. Provides 
 * functionality such as opening rainfall data files, and a number of methods
 * to query the data sets.
 *
 * @author Kieran
 */
public class Interface extends JFrame {

    /* GUI Configuration */
    private static final String title = "Rainfall Analyser";
    private static final int defaultCloseOperation = JFrame.EXIT_ON_CLOSE;

    /* GUI Main Components */
    private JPanel framePanel = new JPanel(new BorderLayout());

    /* Drop Down Menus */
    public static JComboBox durationBox = new JComboBox(new String[] { " ", "1", "2", "3", "4", "5" });
    public static JComboBox yearBox = new JComboBox(new String[]{ " " }), yearBoxGraphs = new JComboBox(new String[]{ " " });
    public static JComboBox monthBox = new JComboBox(new String[]{ " " }), monthBoxGraphs = new JComboBox(new String[]{ " " });
    public static JComboBox weeksBox = new JComboBox(new String[]{ " " });
    public static JComboBox daysBox = new JComboBox(new String[]{ " " });

    /* Buttons */
    public static final String SEARCH_BUTTON_TEXT = "Search";
    public static final String GRAPH_BUTTON_TEXT = "Visual Data";
    public static JRadioButton averageButton = new JRadioButton("Average"), averageButtonGraph = new JRadioButton("Average");
    public static JRadioButton totalButton = new JRadioButton("Total"), totalButtonGraph = new JRadioButton("Total");
    public static JRadioButton wettestButton = new JRadioButton("Wettest"), wettestButtonGraph = new JRadioButton("Wettest");
    public static JRadioButton driestButton = new JRadioButton("Driest");

    /* Labels */
    private static JLabel queryResultsLabel = new JLabel("No Results.");
    private static JLabel graphResultsLabel = new JLabel("Nothing to see here.");

    /* Object declaration */
    private static final MenuListener menuObject = new MenuListener();
    private static final ButtonListener buttonListener = new ButtonListener();

    /**
     * Main constructor - initialises all interface components
     */
    public Interface() {
        setTitle(title);
        setDefaultCloseOperation(defaultCloseOperation);
        setContentPane(framePanel);

        initMenuComponents();
        initTabbedPane();

        pack();
    }

    /**
     * Initialises all menu components with appropriate MenuListener() class
     * ActionListener
     */
    private void initMenuComponents() {
        /* Create Menu */
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File Menu
        JMenu fileMenu = new JMenu(menuObject.FILE_NAME);
            JMenu openFileMenuItem = new JMenu(menuObject.OPEN_FILE_NAME);
                JMenuItem openLancasterFile = new JMenuItem(menuObject.OPEN_FILE_NAME1);
                openLancasterFile.addActionListener(menuObject);
                openFileMenuItem.add(openLancasterFile);
            fileMenu.add(openFileMenuItem);

            fileMenu.addSeparator();

            JMenuItem exitMenuItem = new JMenuItem(menuObject.EXIT_NAME);
            exitMenuItem.addActionListener(menuObject);
            fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        // Help Menu
        JMenu helpMenu = new JMenu(menuObject.HELP_NAME);
            JMenuItem aboutMenu = new JMenuItem(menuObject.HELP_ABOUT_NAME);
            aboutMenu.addActionListener(menuObject);
            helpMenu.add(aboutMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Initialises a tabbed pane on the contentpane with appropriate JPanels
     * for each separate tab.
     */
    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1
        JPanel queryPnl = initQueryPanel();
        tabbedPane.add(queryPnl, "Single Data Queries");

        // Tab 2
        JPanel graphPnl = initGraphPanel();
        tabbedPane.add(graphPnl, "Visualise Large Data Sets");

        framePanel.add(tabbedPane);
    }

    /**
     * Initialises components for the calculation buttons section of the interface
     * @param method integer (0 / 1) to check whether we're looking at the query
     *              or graph panel. Note that 0 represents the query panel and 1
     *              represents the graph panel.
     * @return JPanel for the calculations section of the window.
     */
    private JPanel initCalculationsPanel(int method) {
        JPanel optionPanel = new JPanel(new GridLayout(5, 1));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            JLabel optionsLabel = new JLabel("Calculation Type:");
            optionPanel.add(optionsLabel);
            ButtonGroup buttons = new ButtonGroup( );
                buttons.add(method == 0 ? averageButton : averageButtonGraph); // check which method invoked this
                buttons.add(method == 0 ? totalButton : totalButtonGraph);     // need 2 variables for each panel
                buttons.add(method == 0 ? wettestButton : wettestButtonGraph); // in the tabbed pane
                if (method == 0) buttons.add(driestButton);

            optionPanel.add(method == 0 ? averageButton : averageButtonGraph);
            optionPanel.add(method == 0 ? totalButton : totalButtonGraph);
            optionPanel.add(method == 0 ? wettestButton : wettestButtonGraph);
            if (method == 0) optionPanel.add(driestButton);
            totalButton.setSelected(true);
            totalButtonGraph.setSelected(true);

       return optionPanel;
    }

    /**
     * Initialises components for the year drop-down section of the interface
     * @param method integer (0 / 1) to check whether we're looking at the query
     *              or graph panel. Note that 0 represents the query panel and 1
     *              represents the graph panel.
     * @return JPanel for the year drop-down section of the window.
     */
    private JPanel initYearsOptionPane(int method) {
        JPanel yearPanel = new JPanel(new GridLayout(2, 1));
            yearPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                JLabel yearLabel = new JLabel("Year:");
                yearPanel.add(yearLabel);

                if (method == 0) {
                    yearBox.setPreferredSize(new Dimension(70, 20));
                    yearBox.setEnabled(false);
                    yearPanel.add(yearBox);
                }
                else {
                    yearBoxGraphs.setPreferredSize(new Dimension(70, 20));
                    yearBoxGraphs.setEnabled(false);
                    yearPanel.add(yearBoxGraphs);
                }
                
        return yearPanel;
    }

    /**
     * Initialises components for the month drop-down section of the interface
     * @param method integer (0 / 1) to check whether we're looking at the query
     *              or graph panel. Note that 0 represents the query panel and 1
     *              represents the graph panel.
     * @return JPanel for the months drop-down section of the window.
     */
    private JPanel initMonthsOptionPane(int method) {
        JPanel monthPanel = new JPanel(new GridLayout(2, 1));
            monthPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                JLabel monthLabel = new JLabel("Month:");
                monthPanel.add(monthLabel);

                if (method == 0) {
                    monthBox.setPreferredSize(new Dimension(40, 20));
                    monthBox.setEnabled(false);
                    monthPanel.add(monthBox);
                }
                else {
                    monthBoxGraphs.setPreferredSize(new Dimension(40, 20));
                    monthBoxGraphs.setEnabled(false);
                    monthPanel.add(monthBoxGraphs);
                }
                
        return monthPanel;
    }

    /**
     * Initialises components for the weeks drop-down section of the interface
     * @param method integer (0 / 1) to check whether we're looking at the query
     *              or graph panel. Note that 0 represents the query panel and 1
     *              represents the graph panel.
     * @return JPanel for the weeks drop-down section of the window.
     */
    private JPanel initWeeksOptionPane(int method) {
        JPanel weeksPanel = new JPanel(new GridLayout(2, 1));
            weeksPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                JLabel weeksLabel = new JLabel("Week:");
                weeksPanel.add(weeksLabel);

                if (method == 0) {
                    weeksBox.setPreferredSize(new Dimension(40, 20));
                    weeksBox.setEnabled(false);
                    weeksPanel.add(weeksBox);
                }

        return weeksPanel;
    }

    /**
     * Initialises components for the days drop-down section of the interface
     * @param method integer (0 / 1) to check whether we're looking at the query
     *              or graph panel. Note that 0 represents the query panel and 1
     *              represents the graph panel.
     * @return JPanel for the days drop-down section of the window.
     */
    private JPanel initDaysOptionPane(int method) {
        JPanel daysPanel = new JPanel(new GridLayout(2, 1));
            daysPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                JLabel daysLabel = new JLabel("Day:");
                daysPanel.add(daysLabel);

                if (method == 0) {
                    daysBox.setPreferredSize(new Dimension(40, 20));
                    daysBox.setEnabled(false);
                    daysPanel.add(daysBox);
                }

        return daysPanel;
    }

    /**
     * Initialises components for the graph tab of the interface, calling necessary
     * JPanel components (drop-down menus and buttons.)
     * @return JPanel for the graph tab of the window.
     */
    private JPanel initGraphPanel() {
        JPanel bgPanel = new JPanel(new BorderLayout());
            JPanel pnl = new JPanel();

            pnl.add(initCalculationsPanel(1), BorderLayout.WEST);

            JPanel outerTimePanel = new JPanel(new BorderLayout());
                JPanel durationPnl = new JPanel();
                    JLabel duration = new JLabel("Duration (+/- selected period):");
                    durationPnl.add(duration);
                    durationPnl.add(durationBox);
                outerTimePanel.add(durationPnl, BorderLayout.NORTH);

                JPanel timePanel = new JPanel();
                timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.LINE_AXIS));
                timePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    timePanel.add(initYearsOptionPane(1));
                    timePanel.add(initMonthsOptionPane(1));

                outerTimePanel.add(timePanel, BorderLayout.CENTER);

                JPanel btnPanel = new JPanel();
                    JButton graphButton = new JButton(GRAPH_BUTTON_TEXT);
                        graphButton.addActionListener(buttonListener);
                        btnPanel.add(graphButton);

                outerTimePanel.add(btnPanel, BorderLayout.SOUTH);
            pnl.add(outerTimePanel);

        bgPanel.add(pnl, BorderLayout.NORTH);

        JPanel rPanel = new JPanel(new BorderLayout());
            JSeparator sep = new JSeparator();
            sep.setPreferredSize(new Dimension(1, 1));
            rPanel.add(sep, BorderLayout.NORTH);

            JPanel rlabelPanel = new JPanel();
            rlabelPanel.setPreferredSize(new Dimension(JFrame.WIDTH, 70));
            rlabelPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                graphResultsLabel.setFont(new Font("Serif", Font.PLAIN, 16));
                rlabelPanel.add(graphResultsLabel, BorderLayout.CENTER);
            rPanel.add(rlabelPanel);
        bgPanel.add(rPanel, BorderLayout.SOUTH);

        return bgPanel;
    }

    /**
     * Initialises components for the query tab of the interface, calling necessary
     * JPanel components (drop-down menus and buttons.)
     * @return JPanel for the query tab of the window.
     */
    private JPanel initQueryPanel() {
        JPanel bgPanel = new JPanel(new BorderLayout());

        JPanel pnl = new JPanel();
            pnl.add(initCalculationsPanel(0), BorderLayout.WEST);

            JPanel outerTimePanel = new JPanel(new BorderLayout());
                JPanel timePanel = new JPanel();
                timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.LINE_AXIS));
                timePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    timePanel.add(initYearsOptionPane(0));
                    timePanel.add(initMonthsOptionPane(0));
                    timePanel.add(initWeeksOptionPane(0));
                    timePanel.add(initDaysOptionPane(0));

                outerTimePanel.add(timePanel, BorderLayout.NORTH);

                JPanel btnPanel = new JPanel();
                    JButton search = new JButton(SEARCH_BUTTON_TEXT);
                        search.addActionListener(buttonListener);
                        btnPanel.add(search);
                outerTimePanel.add(btnPanel);
            pnl.add(outerTimePanel);

        bgPanel.add(pnl, BorderLayout.NORTH);

        JPanel rPanel = new JPanel(new BorderLayout());
            JSeparator sep = new JSeparator();
            sep.setPreferredSize(new Dimension(1, 1));
            rPanel.add(sep, BorderLayout.NORTH);
            
            JPanel rlabelPanel = new JPanel();
            rlabelPanel.setPreferredSize(new Dimension(JFrame.WIDTH, 70));
            rlabelPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                queryResultsLabel.setFont(new Font("Serif", Font.PLAIN, 16));
                rlabelPanel.add(queryResultsLabel, BorderLayout.CENTER);
            rPanel.add(rlabelPanel);
        bgPanel.add(rPanel, BorderLayout.SOUTH);

        return bgPanel;
    }

    /**
     * Sets the results field on the Query Tab of the interface
     * @param message Message to send to the interface
     */
    public static void setQueryResultsText(String message) {
        queryResultsLabel.setText(message);
    }

    /**
     * Sets the results field on the Graphs Tab of the interface
     * @param message Message to send to the interface
     */
    public static void setGraphResultsText(String message) {
        graphResultsLabel.setText(message);
    }

}
