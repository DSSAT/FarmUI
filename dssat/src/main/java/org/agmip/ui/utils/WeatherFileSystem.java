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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.objects.NativeString;
import org.agmip.acmo.util.AcmoUtil;
import org.agmip.functions.DataCombinationHelper;
import org.agmip.translators.dssat.DssatControllerOutput;
import org.agmip.translators.dssat.DssatXFileInput;
import org.agmip.translators.dssat.DssatXFileOutput;
import org.agmip.util.JSONAdapter;
import static org.agmip.util.MapUtil.getObjectOr;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rkmalik
 */
public class WeatherFileSystem {

 
    //private static HashMap<String, String> incache = new HashMap<>();
    // private static HashMap<String, String> fertilizerMap = new HashMap<> ();



    // This method is called on the Finish Button to create the weather file. 
    /*public String getFileName() {
        String wthFileName;
        try {
            String orgName = DssatUtil.getInfo(AppKeys.FARM);
            String siteIndex = DssatUtil.getInfo("SiteIndex");
            String year = DssatUtil.getInfo(AppKeys.PLANTING_DATE);

            Integer plantingyear = Integer.parseInt(year);
            Integer twodigyr = plantingyear % 100;
            wthFileName = orgName + twodigyr + "01" + ".WTH";

            wthFileName = PathGenerator.getDataDir() + PathGenerator.getDirSeperator() + wthFileName;
        } catch (Exception e) {
            wthFileName = "WeatherData.WTH";
        }

        return wthFileName;
    }*/

    public static void WriteToFile() {

        // prepare the file name from the organization name and the site index number and then write to that file
        String orgName = DssatUtil.getInfo(AppKeys.FARM);
        String siteIndex = DssatUtil.getInfo("SiteIndex");
        File file = new File(PathGenerator.getWeatherFilePath());

        String writeBuffer = null;

        PrintWriter pr = null;
        try {
            pr = new PrintWriter(file);
            writeBuffer = new String("*WEATHER : \n");
            pr.println(writeBuffer);
            writeBuffer = new String("@ INSI      LAT     LONG  ELEV   TAV   AMP REFHT WNDHT");
            pr.println(writeBuffer);

            double latitude = -34.23;
            double longitude = -34.23;
            double elevation = -99;
            double tavg = -99;
            double tamp = -99;
            double tmht = -99;
            double wmht = -99;

            writeBuffer = new String();
            //Empty 2 Char, Org 2 Char, Site 2Char, Space 1 Char, latitude 8Chars with 3 dec
            writeBuffer = String.format("%.2s%.2s%.2s%.1s%8.3f%.1s%8.3f%.1s%5.0f%.1s%5.1f%.1s%5.1f%.1s%5.1f%.1s%5.1f", "  ", orgName, siteIndex, " ", latitude, " ", longitude, " ", elevation, " ", tavg, " ", tamp, " ", tmht, " ", wmht);
            pr.println(writeBuffer);

            int julianday = 56001;
            double solarrad = -99;
            double tmax = -99;
            double tmin = -99;
            double rain = -99;
            double dewp = -99;
            double wind = -99;
            double par = -99;

            writeBuffer = new String("@DATE  SRAD  TMAX  TMIN  RAIN  RHUM  WIND  TDEW  PAR");
            pr.println(writeBuffer);
            writeBuffer = new String();

            URL url = null;
            InputStream is = null;
            Integer plantingyear;
            if (DssatUtil.getInfo("PlantingYear") != null) {
                plantingyear = Integer.parseInt(DssatUtil.getInfo("PlantingYear"));
            } else {
                Date d = new Date();
                plantingyear = d.getYear() + 1900;
            }
            /*Bring the Weather details for last 10 Years and write the details to the WTH File. */
            try {

                String query = new String(
                        "SELECT%20*%20FROM%20FAWN_historic_daily_20140212%20"
                        + "WHERE%20(%20yyyy%20BETWEEN%20" + (plantingyear - 10) + "%20AND%20" + plantingyear.toString() + ")"
                        + "%20AND%20(%20LocId%20=%20" + DssatUtil.getInfo(AppKeys.STATION_LOCATION_ID) + ")"
                        + "%20ORDER%20BY%20yyyy%20DESC%20");

                String urlStr = "http://abe.ufl.edu/bmpmodel/xmlread/rohit.php?sql=" + query;

                System.out.println("********************************");
                System.out.println(urlStr);

                //System.out.println(urlStr);
                URL uflconnection = new URL(urlStr);
                HttpURLConnection huc = (HttpURLConnection) uflconnection.openConnection();
                HttpURLConnection.setFollowRedirects(false);
                huc.setConnectTimeout(15 * 1000);
                huc.setRequestMethod("GET");
                huc.connect();
                InputStream input = huc.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (!(inputLine.startsWith("S") || inputLine.startsWith("s"))) {
                        String[] fields = inputLine.split(",");

                        int M = Integer.parseInt(fields[3]);
                        int D = Integer.parseInt(fields[4]);
                        int a = (14 - M) / 12;
                        int y = Integer.parseInt(fields[2]) + 4800 - a;
                        int m = M + 12 * a - 3;

                        long JD = D + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;

                        String tempDate = String.format("%05d", Integer.parseInt(fields[2].substring(2)) * 1000 + Integer.parseInt(fields[5]));
                        String SRad;
                        if (fields[13].length() > 1) {
                            SRad = String.format("%.1f", Double.parseDouble(fields[13]));
                        } else {
                            SRad = "";
                        }
                        String TMax = String.format("%.1f", Double.parseDouble(fields[9]));
                        String TMin = String.format("%.1f", Double.parseDouble(fields[8]));
                        String RAIN = String.format("%.1f", Double.parseDouble(fields[12]));
                        String RHUM = String.format("%.1f", Double.parseDouble(fields[11]));
                        String WIND = String.format("%.1f", Double.parseDouble(fields[14]));
                        String TDEW = String.format("%.1f", Double.parseDouble(fields[10]));
                        String PAR = "";

                        String line = String.format("%5s %5s %5s %5s %5s %5s %5s %5s", tempDate, SRad, TMax, TMin, RAIN, RHUM, WIND, TDEW);
                        pr.println(line);

                    }
                }
                in.close();

            } catch (MalformedURLException me) {
                me.printStackTrace();
                //return 21.4;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (pr != null) {
                pr.close();
            }
        }

    }

    public void copyWeatherFile(String localFile, String dssatPath) throws IOException {
        
        String finalOutputPath = DssatUtil.getInfo(AppKeys.DSSATFOLDER);
        finalOutputPath = finalOutputPath + PathGenerator.getDirSeperator() + DssatUtil.getInfo(AppKeys.CROP);
        
        File fout = new File(dssatPath + PathGenerator.getDirSeperator() + localFile);
        
        File fin = new File(finalOutputPath + PathGenerator.getDirSeperator() + localFile);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fin));
        } catch (IOException e){
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(null, msg);
            e.printStackTrace();
        }
        
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(fout));
        } catch (IOException e){
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(null, msg);
            e.printStackTrace();
        }
        
        String line = null;
        while ((line = br.readLine()) != null) {
            bw.write(line + "\n");
        }
        br.close();
        bw.close();

    }



}
