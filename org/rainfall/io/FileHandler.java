package org.rainfall.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.rainfall.lang.Output;

/**
 * FileHandler is a class to wrap some of the complexity of Java's FileReader class.
 * FileHandler offers a number of methods for parsing any type of file:<br>
 *  - readFile reads a given file and returns a String containing all the data from the file<br>
 *  - print sends all the data read from the file to console<br>
 *  - trimSpaces replaces all spaces with 1 space e.g. (he     llo -> he llo)
 *  - extractLines splits a string based on new lines
 *
 * @author Kieran
 */
public class FileHandler {

    public FileHandler() { }

    /**
     * Reads a given file (of any format) and returns its contents as a long string
     * @param fileLocation The absolute location of the file
     * @return A very long string of all the data in the file
     */
    public String readFile(String fileLocation) {
        // Local variable declaration
        FileReader fr = null;
        BufferedReader br = null;
        String dataLine = "";
        String data = "";

        Output.print("Opening file: " + fileLocation);

        try {
            fr = new FileReader(fileLocation);
            br = new BufferedReader(fr);

            while ((dataLine = br.readLine()) != null) {
                data += dataLine + '\n';
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "readFile():error";
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "readFile():error";
        } finally {
            try {
                fr.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            Output.print("Closing file: " + fileLocation);
        }

        return data;
    }

    /**
     * Prints all file data to console
     * @param linesOfData An array of strings consisting of the data for a
     *          particular file (use extractLines() to get the array.)
     */
    public void print(String[] linesOfData) {
        for (int i = 0; i < linesOfData.length; i++) {
            System.out.println(linesOfData[i]);
        }
    }

    /**
     * Replaces all spaces with 1 space
     * @param line A string for a particular line of a file
     * @return A String with all spaces replaced by only 1
     */
    public String trimSpaces(String line) {
        return line.replaceAll("\\s+", " ");
    }

    /**
     * Splits a string based on the new line character
     * @param fileData A long string of data from a file (e.g. readFile())
     * @return An array of the data split by the new line character
     */
    public String[] extractLines(String fileData) {
        return fileData.split("\n");
    }

}
