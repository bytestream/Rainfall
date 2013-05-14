package org.rainfall.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import org.rainfall.RainfallData;
import org.rainfall.gui.Interface;
import org.rainfall.gui.jfreechart.BarChart;

/**
 * Hides some of the complexities of the action listener which was previously 
 * held within the interface class. <br />
 * This class listens for user interaction events and perform the necessary action,
 * all rainfall data query methods are linked to this class.
 *
 * @author Kieran
 */
public class ButtonListener implements ActionListener {

    /* Drop-down Objects */
    private JComboBox durationBox = Interface.durationBox;
    private JComboBox yearBox = Interface.yearBox, yearBoxGraphs = Interface.yearBoxGraphs;
    private JComboBox monthBox = Interface.monthBox, monthBoxGraphs = Interface.monthBoxGraphs;
    private JComboBox weeksBox = Interface.weeksBox;
    private JComboBox daysBox = Interface.daysBox;

    /* Button Objects */
    private JRadioButton averageButton = Interface.averageButton, averageButtonGraph = Interface.averageButtonGraph;
    private JRadioButton totalButton = Interface.totalButton, totalButtonGraph = Interface.totalButtonGraph;
    private JRadioButton wettestButton = Interface.wettestButton, wettestButtonGraph = Interface.wettestButtonGraph;
    private JRadioButton driestButton = Interface.driestButton;

    private final String SEARCH_BUTTON_TEXT = Interface.SEARCH_BUTTON_TEXT;
    private final String GRAPH_BUTTON_TEXT = Interface.GRAPH_BUTTON_TEXT;

    /**
     * Listens for actions on specific GUI components
     * @param e ActionEvent for a given user interaction
     */
    public void actionPerformed(ActionEvent e) {
        /* Search buttonObject */
        if (e.getActionCommand().equals(SEARCH_BUTTON_TEXT)) {
            if (averageButton.isSelected()) {
                Interface.setQueryResultsText(callCorrectAverageMethod());
            }
            else if (totalButton.isSelected()) {
                Interface.setQueryResultsText(callCorrectTotalMethod());
            }
            else if (wettestButton.isSelected()) {
                Interface.setQueryResultsText(callCorrectWettestMethod());
            }
            else if (driestButton.isSelected()) {
                Interface.setQueryResultsText(callCorrectDriestMethod());
            }
        }
        else if (e.getActionCommand().equals(GRAPH_BUTTON_TEXT)) {
            Interface.setGraphResultsText(drawGraphForCorrectPeriod());
        }
        /* End Button */
    }

    private String drawYearGraphs(int year) {
        if (totalButtonGraph.isSelected()) {
            String title = "Total Rainfall over the year " + year;
            new BarChart(title, "Month", "Rainfall (mm)", RainfallData.getTotalYearlyRainfallPerMonth(year), 0);
            return "Nothing to see here!";
        }
        else if (averageButtonGraph.isSelected()) {
            String duration = durationBox.getSelectedItem().toString();
            if (duration != " ") {
                String title = "Average Rainfall over the year " + year + " (+/- " + duration + " years)";
                new BarChart(title, "Year", "Rainfall (mm)", RainfallData.getAverageYearlyRainfallOverTime(year, Integer.parseInt(duration)), 0);

                return "Nothing to see here!";
            }
            else {
                String title = "Average Rainfall per month of the year " + year;
                new BarChart(title, "Month", "Rainfall (mm)", RainfallData.getAverageYearlyRainfallPerMonth(year), 0);

                return "Nothing to see here!";
            }
        }
        else if (wettestButtonGraph.isSelected()) {
            String duration = durationBox.getSelectedItem().toString();
            if (duration != " ") {
                String title = "Wettest day of the year " + year + " (+/- " + duration + " years)";
                new BarChart(title, "Year", "Rainfall (mm)", RainfallData.getWettestDayOfMultipleYears(year, Integer.parseInt(duration)), 0);

                return "Nothing to see here!";
            }
            else {
                String title = "Wettest day for each month of " + year;
                new BarChart(title, "Month", "Rainfall (mm)", RainfallData.getWettestDayPerMonthOfYear(year), 0);

                return "Nothing to see here!";
            }
        }

        return "Erm.. I don't quite understand what you're trying to do!";
    }
    
    private String drawMonthGraphs(int year, int month) {
        if (totalButtonGraph.isSelected()) {
            String title = "Total Rainfall over month " + month + " of " + year;
            new BarChart(title, "Day", "Rainfall (mm)", RainfallData.getMonthlyRainfall(year, month), 0);
            return "Nothing to see here!";
        }
        else if (averageButtonGraph.isSelected()) {
            String duration = durationBox.getSelectedItem().toString();
            if (duration != " ") {
                String title = "Average Rainfall over months " + month + " (+/- " + duration + " month(s)) for the year " + year;
                new BarChart(title, "Month", "Rainfall (mm)", RainfallData.getAverageMonthlyRainfallOverTime(year, month, Integer.parseInt(duration)), month);

                return "Nothing to see here!";
            }
        }
        else if (wettestButtonGraph.isSelected()) {
            String duration = durationBox.getSelectedItem().toString();
            if (duration != " ") {
                String title = "Wettest day of the months " + month + " (+/- " + duration + " month(s)) for the year " + year;
                new BarChart(title, "Month", "Rainfall (mm)", RainfallData.getWettestDayOfMultipleMonths(year, month, Integer.parseInt(duration)), month);

                return "Nothing to see here!";
            }
        }
        
        return "Erm.. I don't quite understand what you're trying to do!";
    }

    private String drawGraphForCorrectPeriod() {
        int year = yearBoxGraphs.getSelectedItem() != " " ? Integer.parseInt(yearBoxGraphs.getSelectedItem().toString()) : 0;
        int month = monthBoxGraphs.getSelectedItem() != " " ? Integer.parseInt(monthBoxGraphs.getSelectedItem().toString()) : 0;

        // Yearly data
        if (yearBoxGraphs.getSelectedIndex() > 0 && monthBoxGraphs.getSelectedIndex() == 0)
        {
            return drawYearGraphs(year);
        }
        // Monthly data
        else if (yearBoxGraphs.getSelectedIndex() > 0 && monthBoxGraphs.getSelectedIndex() > 0)
        {
            return drawMonthGraphs(year, month);
        }
        return "Erm.. I don't quite understand what you're trying to do!";
    }

    private String callCorrectAverageMethod() {
        if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() == 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());

            return "Average Yearly Rainfall: " + RainfallData.getAverageYearlyRainfall(year);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());

            return "Average Monthly Rainfall: " + RainfallData.getAverageMonthlyRainfall(year, month);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() > 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());
            int week = Integer.parseInt(weeksBox.getSelectedItem().toString());

            return "Average Weekly Rainfall: " + RainfallData.getAverageWeeklyRainfall(year, month, week);
        }
        return "<html>No results, please try chosing a different time period.<br /> Consult the \"Help\" menu for guidance.</html>";
    }

    private String callCorrectTotalMethod() {
        if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() == 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());

            return "Total Yearly Rainfall: " + RainfallData.getTotalYearlyRainfall(year);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());

            return "Total Monthly Rainfall: " + RainfallData.getTotalMonthlyRainfall(year, month);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() > 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());
            int week = Integer.parseInt(weeksBox.getSelectedItem().toString());

            return "Total Weekly Rainfall: " + RainfallData.getTotalWeeklyRainfall(year, month, week);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() > 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());
            int day = Integer.parseInt(daysBox.getSelectedItem().toString());

            return "Total Weekly Rainfall: " + RainfallData.getTotalDailyRainfall(year, month, day);
        }
        return "<html>No results, please try chosing a different time period.<br /> Consult the \"Help\" menu for guidance.</html>";
    }

    private String callCorrectWettestMethod() {
        if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() == 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());

            return "Wettest day of the year: " + RainfallData.getWettestDayOfYear(year);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());

            return "Wettest day of the month: " + RainfallData.getWettestDayOfMonth(year, month);
        }
        return "<html>No results, please try chosing a different time period.<br /> Consult the \"Help\" menu for guidance.</html>";
    }

    private String callCorrectDriestMethod() {
        if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() == 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());

            return "Driest day of the year: " + RainfallData.getDriestDayOfYear(year);
        }
        else if (yearBox.getSelectedIndex() > 0 && monthBox.getSelectedIndex() > 0 && weeksBox.getSelectedIndex() == 0 && daysBox.getSelectedIndex() == 0)
        {
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());
            int month = Integer.parseInt(monthBox.getSelectedItem().toString());

            return "Driest day of the month: " + RainfallData.getDriestDayOfMonth(year, month);
        }
        return "<html>No results, please try chosing a different time period.<br /> Consult the \"Help\" menu for guidance.</html>";
    }

}
