/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;

/**
 *
 * @author rohit
 */
public class SiteListFileManager {
    
    public static void setSiteListFile() {
        File f = new File(PathGenerator.getDataDir()+ PathGenerator.getDirSeperator()+ "/SiteIDList.txt");
        String block = DssatUtil.getInfo(AppKeys.BLOCK);
        String site = DssatUtil.getInfo(AppKeys.FARM);;
        
        String siteCode = DssatUtil.getInfo(AppKeys.STATION_CODE);
        int expNo = 1;
        Calendar c = Calendar.getInstance();
        String year = "" + c.get(Calendar.YEAR);
        year = year.substring(2);

        try {
            if (f.exists()) {
                //Check for existing Site ID

                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                boolean isExist = false;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (site.toUpperCase().equals(fields[0]) && block.toUpperCase().equals(fields[1])) {
                        isExist = true;
                        String code = fields[2];
                        expNo = Integer.parseInt(fields[3]) + 1;
                        siteCode = code + year + String.format("%02d", expNo);
                    }
                }
                br.close();

                if (!isExist) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
                    line = site.toUpperCase() + "," + block.toUpperCase() + "," + siteCode + "," + expNo + "\n";
                    siteCode = siteCode + year + String.format("%02d", expNo);
                    bw.write(line);
                    bw.close();
                }
            } else {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                String line = site.toUpperCase() + "," + block.toUpperCase() + "," + siteCode + "," + expNo;
                siteCode = siteCode + year + String.format("%02d", expNo);
                bw.write(line);
                bw.close();
            }
            
            DssatUtil.updateInfo(AppKeys.STATION_CODE, siteCode);

        } catch (Exception e) {
            siteCode = siteCode + year + "01";
            e.printStackTrace();
        }
    }
    
}
