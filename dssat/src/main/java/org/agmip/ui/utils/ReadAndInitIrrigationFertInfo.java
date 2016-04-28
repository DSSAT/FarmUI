/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import com.opencsv.CSVReader;
import com.toedter.calendar.JDateChooserCellEditor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static org.agmip.ui.dssat1.IrrigationFrame.fertilizerTable;

/**
 *
 * @author rohit
 * 
 * I strongly feel that I should remove the redundant Couple Class and instead use the HashMap. But due to time contraint I could 
 * Not do that. 
 * 
 * Once I find time, I will perform this cleanup.
 * 
 */
public class ReadAndInitIrrigationFertInfo {

    private static LinkedList<Couple> fertmethod = new LinkedList<>();
    private static LinkedList<Couple> fertmaterial = new LinkedList<>();
    private static LinkedList<Couple> plantingMethodsList = new LinkedList<>();
    
    private static HashMap<String, String> plantingMethodToCodeMap = new HashMap<String, String> ();
    
    private static LinkedList<Couple> irrigationMethod = new LinkedList<>();
    private static HashMap<String, String> fertMethodVsCode = new HashMap<>();
    private static HashMap<String, String> fertMaterialVsCode = new HashMap<String, String>();

    public static LinkedList<Couple> getFertmethod() {
        return fertmethod;
    }

    public static LinkedList<Couple> getFertmaterial() {
        return fertmaterial;
    }

    public static LinkedList<Couple> getPlantingMethodsList() {
        return plantingMethodsList;
    }
        
    public static HashMap<String, String> getPlantingMethodToCodeMap() {
        return plantingMethodToCodeMap;
    }

    public static LinkedList<Couple> getIrrigationMethod() {
        return irrigationMethod;
    }

    public static HashMap<String, String> getFertMethodVsCode() {
        return fertMethodVsCode;
    }

    public static HashMap<String, String> getFertMaterialVsCode() {
        return fertMaterialVsCode;
    }

    private static class CoupleCompare implements Comparator<Couple> {

        @Override
        public int compare(Couple o1, Couple o2) {
            return o1.method.compareTo(o2.method);
        }
    }
    


    public static void initFertInfo() {

        CSVReader reader;
        String[] nextLine;

        try {
            reader = new CSVReader(new InputStreamReader(ReadAndInitIrrigationFertInfo.class.getClass().getResourceAsStream("/properties/csv/dssat_codelookup.csv")));

            while ((nextLine = reader.readNext()) != null) {
                //cropName.add(nextLine[1]);
                String column2 = nextLine[2];
                String column1 = nextLine[1];
                String plantingMethodHeader = nextLine[0];

                String description = new String();
                // Check if the code is FE/AP Then Build the descripttion till end.
                if (column2.startsWith("FE")) {
                    for (int i = 3; i < nextLine.length; i++) {
                        description = description + nextLine[i];
                    }
                    Couple c = new Couple(description, column2);
                    fertmaterial.add(c);

                    // This have all the values for code vs Code value
                    fertMaterialVsCode.put(description.trim(), column2);
                } else if (column1.startsWith("AP")) {

                    for (int i = 2; i < nextLine.length; i++) {
                        description = description + nextLine[i];
                    }
                    Couple c = new Couple(description.trim(), column2);
                    fertmethod.add(c);
                    // This have all the values for code vs Code value                    
                    fertMethodVsCode.put(description.trim(), column1);
                } else if (nextLine[0].contains("Methods - Irrigation and")) {

                    System.out.println("Hello Irrigation." + nextLine[2] + nextLine[1]);

                    if (nextLine[2].startsWith("Drip") || nextLine[2].startsWith("Water")) {

                        if (nextLine[2].startsWith("Drip")) {
                            Couple c = new Couple("Dripper", nextLine[1]);
                            irrigationMethod.add(c);
                        }

                        if (nextLine[2].startsWith("Water")) {
                            Couple c = new Couple("Water Table", nextLine[1]);
                            irrigationMethod.add(c);
                        }
                    }

                }

                if (plantingMethodHeader.equals("Planting Material/Method")) {
                    String plantingMethodName = nextLine[2];
                    if (plantingMethodName.isEmpty() == false) {
                        Couple pm = new Couple(nextLine[2], nextLine[1]);
                        plantingMethodsList.add(pm);
                        plantingMethodToCodeMap.put(nextLine[2], nextLine[1]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(plantingMethodsList, new CoupleCompare());
        Collections.sort(fertmethod, new CoupleCompare());
        Collections.sort(fertmaterial, new CoupleCompare());
        Collections.sort(irrigationMethod, new CoupleCompare());
    }

}
