package org.rainfall.util;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * The Date class provides functionality for verifying dates and storing all the
 * dates from the loaded rainfall data file.
 *
 * @author Kieran
 */
public class Date {

    private static final int MINIMUM_MONTH_SUPPORTED = 1;
    private static final int MAXIMUM_MONTH_SUPPORTED = 12;
    private static final int MINIMUM_YEAR_SUPPORTED = 1950;
    private static final int MAXIMUM_YEAR_SUPPORTED = Calendar.getInstance().get(Calendar.YEAR);

    private static int mostRecentYear = 0, oldestYear = 0;
    private int year, month;

    private static ArrayList<Date> dates = new ArrayList<Date>();

    /**
     * Main constructor
     */
    public Date() { }

    /**
     * Constructor used to create date objects
     * @param year int - year number
     * @param month int - month number
     */
    public Date(int year, int month) {
        if (!dateIsValid(year, month))
            return;
        else {
            this.year = year;
            this.month = month;
        }
    }

    /**
     * Gets the month number of the date object
     * @return int - Month number
     */
    public int getMonth() {
        return month;
    }

    /**
     * Get the year number of the date object
     * @return int - Year number
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the most recent year from the list of dates collected from our file
     * @return int - the most recent year (e.g. 2012)
     */
    public static int getMostRecentYear() {
        if (mostRecentYear != 0) return mostRecentYear; // speed up efficiency (only loop once)
        else {
            int mostRecent = dates.get(0).getYear();
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).getYear() > mostRecent)
                    mostRecent = dates.get(i).getYear();
            }
            mostRecentYear = mostRecent;
            return mostRecent;
        }
    }

    /**
     * Gets the oldest year from the dataset loaded from file
     * @return int - oldest year in the dataset e.g. 1940
     */
    public static int getOldestYear() {
        if (oldestYear != 0) return oldestYear; // speed up efficiency (only loop once)
        else {
            int oldest = dates.get(0).getYear();
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).getYear() < oldest)
                    oldest = dates.get(i).getYear();
            }
            oldestYear = oldest;
            return oldest;
        }
    }

    /**
     * Adds a date object to our array of date objects
     * @param d Date object consisting of year and month
     */
    public static void storeDateObject(Date d) {
        dates.add(d);
    }

    /**
     * Removes the specified date object from our array of date objects
     * @param d Date object consisting of year and month
     */
    public void removeDateObject(Date d) {
        dates.remove(d);
    }

    /**
     * Convert the integer of a month to its string equivalent
     * @param month Month number
     * @return String of the converted month number
     */
    public static String monthToName(int month) {
        switch (month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "Septemer";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }

    /**
     * Gets the number of days in the specified month, year is required due to
     * leap year check on february
     * @param year The year you wish to search for
     * @param month The month you wish to search for
     * @return int - Number of days in the month
     */
    public static int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                if (isLeapYear(year))
                    return 29;
                else
                    return 28;
        }
        return -1;
    }

    private static boolean isLeapYear(int year) {
        if (year % 4 == 0 && (year % 100 == 0 || year % 400 == 0))
            return true;
        else
            return false;
    }

    /**
     * Searches through the array of date objects until it finds the specified
     * year and month - <b>used as a key when storing rainfall data</b>
     * @param year The year you wish to look for
     * @param month The month you wish to look for in the specified year
     * @return A date object for the specified year and month
     */
    public static Date getDateObjectForMonth(int year, int month) {
        int arraySize = dates.size(); // improve efficiency (don't lookup size every iteration)
        for (int i = 0; i < arraySize; i++) {
            Date d = dates.get(i);
            if (d.getMonth() == month && d.getYear() == year)
                return d;
        }
        return null;
    }

    /**
     * Searches through the array of date objects until it finds the specified year
     * then returns an array of date objects (12) - one for each month of that year
     * - <b>used as a key when storing rainfall data</b>
     * @param year The year you wish to look for
     * @return An array of 12 Date objects e.g. "year":"1", "year":"2"...
     */
    public static Date[] getDateObjectsForYear(int year) {
        Date[] monthData = new Date[12];
        int c = 0, arraySize = dates.size(); // improve efficiency (don't lookup size every iteration)
        for (int i = 0; i < arraySize; i++) {
            Date d = dates.get(i);
            if (d.getYear() == year)
                monthData[c++] = d;
        }
        return monthData;
    }

    private boolean dateIsValid(int year, int month) {
        if ((year >= MINIMUM_YEAR_SUPPORTED && year <= MAXIMUM_YEAR_SUPPORTED) &&
             (month >= MINIMUM_MONTH_SUPPORTED && month <= MAXIMUM_MONTH_SUPPORTED))
                return true;
        else
            return false;
    }

}
