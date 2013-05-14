package org.rainfall.io;

import java.util.ArrayList;
import org.rainfall.lang.Output;

/**
 * Adds a layer of abstraction from the FileHandler class due to the specific
 * formating in the lancaster rainfall data file.
 *
 * @author Kieran
 */
public class LancasterReader extends FileHandler {

    /**
     * Main constructor
     */
    public LancasterReader() {
        super();
    }

    /**
     * Organises and sanitizes all the data from the file. For example data is structured
     * such that: 1974      1    0.0    0.0    0.0    1.2 [...]
     * This method reads the file and trims the spaces ready for use.
     * @param fileLocation The absolute location of the data file
     * @return String array of the data from the file
     */
    public String[] getRainfallData(String fileLocation) {
        // Check before reading any HUGE files..
        if (!fileLocation.endsWith(".txt")) {
            Output.error("getRainfallData(): Invalid file type.");
            return null;
        }
        
        // Read the file (like any other)
        String rawData = super.readFile( fileLocation );

        if (!rawData.equals("readFile():error") && !rawData.equals("")) {
            // Extract line by line into array
            String[] rawDataLines = super.extractLines( rawData );
            if (isValidFile(rawDataLines))
                return sanitizeData( rawDataLines ); // Remove any unwanted data
            else {
                Output.error("getRainfallData(): Invalid file type.");
                return null;
            }
        }
        else {
            Output.error("getRainfallData(): There was an error reading the file.");
            return null;
        }
    }

    private boolean isValidFile(String[] data) {
        if (data[0].contains("Hazelrigg Weather Station, Lancaster University"))
            return true;
        else
            return false;
    }

    private String[] sanitizeData(String[] fileData) {
        // We don't know the size of the array we're going to create so use arraylist
        ArrayList<String> data = new ArrayList<String>();

        for (int i = 0; i < fileData.length; i++) {
            // Check if it matches the file format for the lines we want
            if (fileData[i].matches("^[0-9 .-]+$")) {
                fileData[i] = trimSpaces(fileData[i]);
                data.add(fileData[i]);
            }
        }

        // convert back to array of String objects
        String filteredData[] = new String[data.size()];
        for (int i = 0; i < data.size(); i++ ) {
            filteredData[i] = data.get(i);
        }
        return filteredData;
    }

}

