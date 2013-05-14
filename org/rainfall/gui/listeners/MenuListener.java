package org.rainfall.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import org.rainfall.RainfallData;
import org.rainfall.gui.AboutInterface;
import org.rainfall.gui.Interface;
import org.rainfall.io.LancasterReader;
import org.rainfall.lang.Output;

/**
 * Listens for actions performed on all menus of the interface. <br />
 * Provides configuration options for the menu such as menu names.
 *
 * @author Kieran
 */
public class MenuListener implements ActionListener {

    /* File menu names */
    public static final String FILE_NAME = "File";
    public static final String OPEN_FILE_NAME = "Open File";
    public static final String OPEN_FILE_NAME1 = "Lancaster Data";
    public static final String EXIT_NAME = "Exit";

    /* Help menu names */
    public static final String HELP_NAME = "Help";
    public static final String HELP_ABOUT_NAME = "About";

    /* Interface Objects */
    private JComboBox yearBox = Interface.yearBox, yearBoxGraphs = Interface.yearBoxGraphs;
    private JComboBox monthBox = Interface.monthBox, monthBoxGraphs = Interface.monthBoxGraphs;
    private JComboBox weeksBox = Interface.weeksBox;
    private JComboBox daysBox = Interface.daysBox;

    /**
     * Listens for actions on specific GUI components
     * @param e ActionEvent for a given user interaction
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(OPEN_FILE_NAME1)) {
            openFileDialogBox(OPEN_FILE_NAME1);
            toogleSearchBoxes(true);
            updateSearchBoxValues();
        }
        else if (e.getActionCommand().equals(EXIT_NAME)) {
            System.exit(0);
        }
        else if (e.getActionCommand().equals(HELP_ABOUT_NAME)) {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    return new AboutInterface();
                }
            };
            worker.execute();
        }
    }

    private void updateSearchBoxValues() {
        // Only update the boxes once..
        if (yearBox.getItemCount() == 1) {
            /* Update Years */
            ArrayList<Integer> years = new ArrayList<Integer>(RainfallData.getYears());
            Collections.sort(years);
            for (Integer s : years) {
                yearBox.addItem(s);
                yearBoxGraphs.addItem(s);
            }
            /* Update Months */
            for (int i = 1; i <= 12; i++) {
                monthBox.addItem(i);
                monthBoxGraphs.addItem(i);
            }
            /* Update Weeks */
            for (int i = 1; i <= 5; i++) {
                weeksBox.addItem(i);
            }
            /* Update Days */
            for (int i = 1; i <= 31; i++) {
                daysBox.addItem(i);
            }
        }
    }

    private void toogleSearchBoxes(boolean toogle) {
        yearBox.setEnabled(toogle); yearBoxGraphs.setEnabled(toogle);
        monthBox.setEnabled(toogle); monthBoxGraphs.setEnabled(toogle);
        weeksBox.setEnabled(toogle); 
        daysBox.setEnabled(toogle); 
    }

    private void openLancasterFile(File file) {
        LancasterReader rf = new LancasterReader();
        String[] rainfallData = rf.getRainfallData(file.getAbsolutePath());
        if (rainfallData != null)
            RainfallData.storeObjects(rainfallData);
        else
            Output.errorToGUI("Invalid file type chosen, please try again!");
    }

    private void openFileDialogBox(String fileType) {
        JFileChooser fc = new JFileChooser();
        int openDialog = fc.showOpenDialog(fc);

        if (openDialog == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            openLancasterFile(file);
        }
    }

}
