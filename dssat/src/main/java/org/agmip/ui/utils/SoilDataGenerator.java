/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author rkmalik
 */
public class SoilDataGenerator {
    
    public static String GenerateSoilData() {

        /*
         * Below are the steps to prepare the Soil File
         * 
         * 1. Get the Soil name from the soil Combobox
         * 2. Get the Soild ID 
         * 3. Need to open the FL.SOL file
         * 4. Read the soil file line by line and Find the SOIL Id section in the file. 
         * 5. Read the whole section till you reach the next Soil ID details. 
         * 6. Now you Open a file to write the data 
         * 7. Write the data to the New soild file. 
         * 8. Close teh file if you need to close the file. 	 * 
         * */
        //String finalOutputPath = PathGenerator.getDssatInstallationFolder() + PathGenerator.getDirSeperator()+ 
        //DssatUtil.getInfo(AppKeys.CROP);//jComboBoxCropList.getSelectedItem();
        
        return ReadSoilData();
        
    }

    private static String ReadSoilData() {

        String outputPath = PathGenerator.getSoilFilePath();
        String datastring = "*" + DssatUtil.getInfo(AppKeys.SOIL_ID);
        boolean isdatablock = false;
        

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(SoilDataGenerator.class.getClass().getResourceAsStream("/properties/csv/FL.sol")));
            // Open  the file to write the data to the file 
            File outputfile = new File(outputPath);

            System.out.println("Rohit Created File : " + outputPath);
            if (!outputfile.exists()) {
                outputfile.createNewFile();
            }
            FileWriter fw = new FileWriter(outputfile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String str;
            while ((str = in.readLine()) != null) {
                // Check if a line starts with datastring the we need to start re
                if (isdatablock == false) {

                    if (str.startsWith(datastring)) {
                        isdatablock = true;
                        bw.write(str + "\r\n");
                    }

                } else {

                    if (str.startsWith("*")) {
                        break;
                    }
                    // Break when we encounter another astrix in the begining of the line 
                    bw.write(str + "\r\n");
                }
            }

            bw.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputPath;
    }
}
