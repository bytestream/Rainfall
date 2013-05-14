package org.rainfall;

import org.rainfall.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import org.rainfall.lang.Output;

/**
 * Stores the data set which is loaded in from a file reader. Data is parsed into a HashMap
 * based on Date objects (Year, Month) as the key and the an array of floats consisting
 * of the total rainfall per day of each month of a given year in the data set. <br /><br />
 * Also provides a large amount of functionality which can be performed on the data set
 * such as searching for Wettest, Driest, Averages and Total rainfall values over a selection
 * of time periods such as Years, Months, Weeks and Days.
 *
 * @author Kieran
 * @version 1.8
 * @see Date
 */
public class RainfallData extends Date {

    private static HashMap<Date, ArrayList<Float>> rainData = new HashMap<Date, ArrayList<Float>>();

    /** Constructor */
    public RainfallData() {
        super();
    }

    /**
     * List of the years collected from the inputted data file
     * @return ArrayList of years from the data file (we don't know how many items there will be)
     */
    public static HashSet<Integer> getYears() {
        HashSet<Integer> years = new HashSet<Integer>();
        for (Date key : rainData.keySet()) {
            years.add(key.getYear());
        }
        return years;
    }

    /**
     * Prints to console all items of rainfall data in the format: <br />
     * <Year, Month> : [ Rainfall Data 1:31 days ]
     */
    public static void printRainfallData() {
        for (Date key : rainData.keySet()) {
            Output.print("<" + key.getYear() + ", " + key.getMonth() + "> : " + rainData.get(key));
        }
    }

    /**
     * Calculates the wettest day of a given month
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the wettest day of the specified month is returned.
     */
    public static float getWettestDayOfMonth(int year, int month) {
        if (year <= 0 && (month <= 0 || month > 12)) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            ArrayList<Float> rain = getMonthlyRainfall(year, month);
            if (rain != null) {
                try {
                    return Collections.max(rain);
                } catch (NoSuchElementException nse) { return 0; }
            }
            else
                return 0;
        }
    }

    /**
     * Grabs the wettest day over a duration of months
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @param duration +/- value for the specified month e.g. (Month 2 +/- 1 would be 1, 2, 3)
     * @return ArrayList of floats containing the wettest day over a number of months
     */
    public static ArrayList<Float> getWettestDayOfMultipleMonths(int year, int month, int duration) {
        ArrayList<Float> data = new ArrayList<Float>();

        // Go backwards (year - duration)
        for (int k = duration; k > 0; k--) {
            if ((month - k) < 1 && year-1 >= getOldestYear()) {
                year--; month = 12;
                data.add(getWettestDayOfMonth(year, month));
            }
            else {
                data.add(getWettestDayOfMonth(year, month - k));
            }
        }
        // Get year
        data.add(getWettestDayOfMonth(year, month));
        // Get year + duration
        for (int k = 1; k <= duration; k++) {
            if ((month + k) > 12 && (year+1 <= getMostRecentYear())) {
                year++; month = 1;
                data.add(getWettestDayOfMonth(year, month));
            }
            else {
                data.add(getWettestDayOfMonth(year, month + k));
            }
        }

        return data;
    }

    /**
     * Calculates the wettest day of the year
     * @param year The year you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the wettest day of the specified year is returned.
     */
    public static float getWettestDayOfYear(int year) {
        if (year <= 0) {
            Output.error("Please specify a year number");
            return -99.99F;
        }
        else {
            Date[] months = getDateObjectsForYear(year);
            float yearMax = 0;

            if (months[0] != null) {
                for (int i = 0; i < months.length; i++) {
                    int month = months[i].getMonth();
                    ArrayList<Float> rainMonth = getMonthlyRainfall(year, month);

                    float monthMax;
                    
                    try { monthMax = Collections.max(rainMonth); }
                    catch (NoSuchElementException nsee) { monthMax = 0; }
                    if (monthMax > yearMax)
                        yearMax = monthMax;
                }
            }
            return yearMax;
        }
    }

    /**
     * Grabs the wettest day of each month for a given year
     * @param year The year you wish to lookup
     * @return ArrayList of 12 floats one for each month of the given year consisting of the wettest day value
     */
    public static ArrayList<Float> getWettestDayPerMonthOfYear(int year) {
        Date[] dateObject = getDateObjectsForYear(year);
        ArrayList<Float> data = new ArrayList<Float>();

        for (int i = 0; i < dateObject.length; i++) {
            int month = dateObject[i].getMonth();
            data.add(getWettestDayOfMonth(year, month));
        }

        return data;
    }

    /**
     * Grabs the wettest day over multiple years
     * @param year The year you wish to lookup
     * @param duration +/- a number of months based on the given month
     * @return ArrayList of floats consisting of the wettest day over multiple years
     */
    public static ArrayList<Float> getWettestDayOfMultipleYears(int year, int duration) {
        if (year <= 0) {
            Output.error("Please specify a year number");
            return null;
        }
        else {
            ArrayList<Float> data = new ArrayList<Float>();
            Date[] months = null;
            float yearMax = 0;
            
            // Go backwards (year - duration)
            for (int k = duration; k > 0; k--) {
                yearMax = getWettestDayOfYear(year - k);
                data.add(yearMax);
            }
            // Get year
            yearMax = getWettestDayOfYear(year);
            data.add(yearMax);
            // Get year + duration
            for (int k = 1; k <= duration; k++) {
                yearMax = getWettestDayOfYear(year + k);
                data.add(yearMax);
            }

            return data;
        }
    }

    /**
     * Calculates the driest day of a given month
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the driest day of the specified month is returned.
     */
    public static float getDriestDayOfMonth(int year, int month) {
        if (year <= 0 && (month <= 0 || month > 12)) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            ArrayList<Float> rain = getMonthlyRainfall(year, month);
            return Collections.min(rain);
        }
    }

    /**
     * Calculates the driest day of the year
     * @param year The year you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the driest day of the specified year is returned.
     */
    public static float getDriestDayOfYear(int year) {
        if (year <= 0) {
            Output.error("Please specify a year number");
            return -99.99F;
        }
        else {
            Date[] months = getDateObjectsForYear(year);
            float yearMin = 0;

            for (int i = 0; i < months.length; i++) {
                int month = months[i].getMonth();
                ArrayList<Float> rainMonth = getMonthlyRainfall(year, month);

                float monthMin = Collections.min(rainMonth);
                if (monthMin > yearMin)
                    yearMin = monthMin;
            }
            return yearMin;
        }
    }

    /**
     * Gets the rainfall for a specified day of month and year
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @param day Which days rainfall you want to collect
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the rainfall on the specified day is returned.
     */
    public static float getTotalDailyRainfall(int year, int month, int day) {
        if (year <= 0 && month <= 0 && day <= 0) {
            Output.error("Please specify a year, month and day number");
            return -99.99F;
        }
        else {
            ArrayList<Float> rain = getMonthlyRainfall(year, month);
            return rain.get(day - 1);  // change day by -1 because array starts at 0
        }
    }

    /**
     * Grabs the amount of rainfall that occurred each day of a particular week
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @param week The week number you wish to lookup
     * @return ArrayList of floats containing the rainfall for each day of that particular week.
     */
    public static ArrayList<Float> getTotalWeeklyRainfallList(int year, int month, int week) {
        if (year <= 0 && month <= 0 && week <= 0) {
            Output.error("Please specify a year, month and week number");
            return null;
        }
        else if (week <= 0 || week > 5) {
            Output.error("Invalid week number specified");
            return null;
        }
        else {
            ArrayList<Float> data = new ArrayList<Float>();
            int counter = 0;
            float totalWeeksInMonth = (float)getDaysInMonth(year, month) / 7;

            while (true) {
                ArrayList<Float> rain = getMonthlyRainfall(year, month);

                // We want weeks to start from 0 so 0*7 loops through the first few days 0 - 6
                for (int day = (week-1)*7; counter < 7; day++, counter++) {
                    if (day >= 31 || rain.get(day) == -99.99F) break;

                    data.add(rain.get(day));
                }

                // if all is good finish the loop checking for a rollover week (see below)
                if (week <= totalWeeksInMonth) break;
                // if the week number flows into another month
                else if (week > totalWeeksInMonth && totalWeeksInMonth < 5) {
                    week = 1;
                    month++; // flow into the next month
                    if (month > 12) { // flow into the next year
                        year++;
                        month = 1;
                    }
                }
            }
            return data;
        }
    }

    /**
     * Calculates the total rainfall for a specified week. For example the total rainfall
     * that occurred on week 2 (day 7 - 14) of a specified month and year
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @param week The week number you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the rainfall on the specified day is returned.
     */
    public static float getTotalWeeklyRainfall(int year, int month, int week) {
        if (year <= 0 && month <= 0 && week <= 0) {
            Output.error("Please specify a year, month and week number");
            return -99.99F;
        }
        else if (week <= 0 || week > 5) {
            Output.error("Invalid week number specified");
            return -99.99F;
        }
        else {
            float total = 0;
            int counter = 0;
            float totalWeeksInMonth = (float)getDaysInMonth(year, month) / 7;

            while (true) {
                ArrayList<Float> rain = getMonthlyRainfall(year, month);

                // We want weeks to start from 0 so 0*7 loops through the first few days 0 - 6
                for (int day = (week-1)*7; counter < 7; day++, counter++) {
                    if (day >= 31 || rain.get(day) == -99.99F) break;

                    total += rain.get(day);
                }

                // if all is good finish the loop checking for a rollover week (see below)
                if (week <= totalWeeksInMonth) break;
                // if the week number flows into another month
                else if (week > totalWeeksInMonth && totalWeeksInMonth < 5) {
                    week = 1;
                    month++; // flow into the next month
                    if (month > 12) { // flow into the next year
                        year++;
                        month = 1;
                    }
                }
            }
            return total;
        }
    }

    /**
     * Calculates the average rainfall for a specified week. For example the mean (average) rainfall
     * that occurred on week 2 (day 7 - 14) of a specified month and year
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @param week The week number you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the rainfall on the specified week is returned.
     */
    public static float getAverageWeeklyRainfall(int year, int month, int week) {
        if (year <= 0 && month <= 0 && week <= 0) {
            Output.error("Please specify a year, month and week number");
            return -99.99F;
        }
        else {
            return getTotalWeeklyRainfall(year, month, week) / 7;
        }
    }

    /**
     * Gets a specified dateObject rainfall data in ArrayList<Float> format
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @return ArrayList - If there is an error null is returned<br />
     *                     Otherwise an ArrayList of floats consisting of the rainfall data for that month (1 - 31 days.)
     */
    public static ArrayList<Float> getMonthlyRainfall(int year, int month) {
        if (year <= 0 && month <= 0) {
            Output.error("Please specify a year and month number");
            return null;
        }
        else {
            // getDateObjectForMonth(): Returns a Date object for the specified month/year used as the key in the hashmap
            return rainData.get(getDateObjectForMonth(year, month));
        }
    }

    /**
     * Returns the total rainfall for a specific month of a year
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the rainfall on the specified month is returned.
     */
    public static float getTotalMonthlyRainfall(int year, int month) {
        if (year <= 0 && month <= 0) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            ArrayList<Float> rain = getMonthlyRainfall(year, month);
            float total = 0;
            if (rain != null) {
                // Loop through each day totalling up the rainfall
                for (int day = 0; day < rain.size(); day++) {
                    if (rain.get(day) == -99.99F) break;
                    total += rain.get(day);
                }
            }
            return total;
        }
    }

    /**
     * Calculates the mean/average rainfall for a specified month of a year
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the rainfall on the specified month is returned.
     */
    public static float getAverageMonthlyRainfall(int year, int month) {
        if (year <= 0 && month <= 0) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            return getTotalMonthlyRainfall(year, month)/getDaysInMonth(year, month);
        }
    }

    /**
     * Grabs the average monthly rainfall over a number of months
     * @param year The year you wish to lookup
     * @param month The month you wish to lookup (used as a pivot on the duration)
     * @param duration +/- a number of months based on the given month
     * @return ArrayList of floats consisting of the average rainfall over a number of months
     */
    public static ArrayList<Float> getAverageMonthlyRainfallOverTime(int year, int month, int duration) {
        ArrayList<Float> data = new ArrayList<Float>();

        // Go backwards (month - duration)
        for (int k = duration; k > 0; k--) {
            if ((month - k) < 1 && year-1 >= getOldestYear()) {
                year--; month = 12;
                data.add(getAverageMonthlyRainfall(year, month));
            }
            else {
                data.add(getAverageMonthlyRainfall(year, month - k));
            }
        }
        // Get month
        data.add(getAverageMonthlyRainfall(year, month));
        // Get month + duration
        for (int k = 1; k <= duration; k++) {
            if ((month + k) > 12 && (year+1 <= getMostRecentYear())) {
                year++; month = 1;
                data.add(getAverageMonthlyRainfall(year, month));
            }
            else {
                data.add(getAverageMonthlyRainfall(year, month + k));
            }
        }

        return data;
    }

    /**
     * Grabs the average rainfall per month over a particular year
     * @param year The year you wish to lookup
     * @return ArrayList of floats containing the average rainfall per month over a particular year
     */
    public static ArrayList<Float> getAverageYearlyRainfallPerMonth(int year) {
        Date[] dateObject = getDateObjectsForYear(year);
        ArrayList<Float> data = new ArrayList<Float>();

        for (int i = 0; i < dateObject.length; i++) {
            int month = dateObject[i].getMonth();
            data.add(getAverageMonthlyRainfall(year, month));
        }
        return data;
    }

    /**
     * Grabs the total rainfall per month over the duration of a specified year
     * @param year The year you wish to lookup
     * @return An array list of floats consisting of the total rainfall per month
     */
    public static ArrayList<Float> getTotalYearlyRainfallPerMonth(int year) {
        if (year <= 0) {
            Output.error("Please specify a year and month number");
            return null;
        }
        else {
            Date[] dateObject = getDateObjectsForYear(year);
            ArrayList<Float> data = new ArrayList<Float>();

            for (int i = 0; i < dateObject.length; i++) {
                int month = dateObject[i].getMonth();
                data.add(getTotalMonthlyRainfall(year, month));
            }
            return data;
        }
    }

    /**
     * Grabs the total rainfall per year over a particular duration
     * @param year The year you wish to lookup
     * @param duration The duration +/- the specified year to lookup
     * @return An array list of floats consisting of the total rainfall per year
     */
    public static ArrayList<Float> getTotalYearlyRainfallOverTime(int year, int duration) {
        if (year <= 0) {
            Output.error("Please specify a year and duration number");
            return null;
        }
        else {
            ArrayList<Float> data = new ArrayList<Float>();

            // Go backwards (year - duration)
            for (int k = duration; k > 0; k--) {
                data.add(getTotalYearlyRainfall(year - k));
            }
            // Get year
            data.add(getTotalYearlyRainfall(year));
            // Get year + duration
            for (int k = 1; k <= duration; k++) {
                data.add(getTotalYearlyRainfall(year + k));
            }

            return data;
        }
    }

    /**
     * Calculates the total rainfall across all dateObject of the specified year
     * @param year The year you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the total rainfall on the specified year is returned.
     */
    public static float getTotalYearlyRainfall(int year) {
        if (year <= 0) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            Date[] dateObject = getDateObjectsForYear(year);
            float total = 0;
            if (dateObject[0] != null) {
                for (int i = 0; i < dateObject.length; i++) {
                    int month = dateObject[i].getMonth();
                    total += getTotalMonthlyRainfall(year, month);
                }
            }
            return total;
        }
    }

    /**
     * Calculates the mean (average) amount of rainfall across the specified year
     * @param year The year you wish to lookup
     * @return float - If there is an error -99.99 is returned<br />
     *                 Otherwise a float for the average rainfall on the specified year is returned.
     */
    public static float getAverageYearlyRainfall(int year) {
        if (year <= 0) {
            Output.error("Please specify a year and month number");
            return -99.99F;
        }
        else {
            return getTotalYearlyRainfall(year)/365;
        }
    }

    /**
     * Grabs the average rainfall over a number of years
     * @param year The year you wish to lookup
     * @param duration The duration +/- the specified year to lookup
     * @return ArrayList of floats containing the average rainfall over each year
     */
    public static ArrayList<Float> getAverageYearlyRainfallOverTime(int year, int duration) {
        ArrayList<Float> data = new ArrayList<Float>();

        // Go backwards (year - duration)
        for (int k = duration; k > 0; k--) {
            data.add(getAverageYearlyRainfall(year - k));
        }
        // Get year
        data.add(getAverageYearlyRainfall(year));
        // Get year + duration
        for (int k = 1; k <= duration; k++) {
            data.add(getAverageYearlyRainfall(year + k));
        }

        return data;
    }

    /**
     * Parses the array of data collected from the file reader into a HashMap
     * consisting of Date objects (Year, Month) and an array of floats consisting
     * of the total rainfall per day of a given month.
     * @param data String array of the data collected from the File Reader
     */
    public static void storeObjects(String[] data) {
        for (int i = 0; i < data.length; i++) {
            String[] split = data[i].split(" ");

            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            Date d = new Date(year, month);
            storeDateObject(d);

            ArrayList<Float> monthData = new ArrayList<Float>();
            for (int c = 2; c < split.length; c++)
            {
                float value = Float.parseFloat(split[c]);
                if (value != -99.99F)
                    monthData.add(value);
            }

            rainData.put(d, monthData);
        }
    }

}
