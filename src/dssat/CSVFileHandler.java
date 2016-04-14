/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

import com.opencsv.CSVReader;
import static dssat.DSSATMain.LOGGER;
import static dssat.DSSATMain.csvdirpath;
import static dssat.DSSATMain.dirseprator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author rkmalik
 */
public class CSVFileHandler {
    
    public HashMap <String, HashMap<String, HashMap<String, String>>> ReadSitesFile ()
    {
        HashMap <String, HashMap<String, HashMap<String, String>>> pathstrings = new HashMap <String, HashMap<String, HashMap<String, String>>> ();
        CSVReader reader;
        String [] nextLine; 
        LOGGER.log(Level.ALL, "Initializing the data from sites file.");
        
        try {            
            String filepath = DSSATMain.curdirpath +dirseprator+ "sites"+dirseprator+"sitesinfo.sites"; 
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/sites/sitesinfo.sites")));
            while ((nextLine = reader.readNext()) != null) { 
                
                String path = nextLine[1];                               
                if (path.trim().equalsIgnoreCase("sites")) {
                    
                    if (nextLine[2] != null) {
                        String sitename = nextLine[2].trim();
                        HashMap<String, HashMap<String, String>> site = pathstrings.get(sitename);
                        if (site == null) {                        
                            site = new HashMap<String, HashMap<String, String>> ();
                            pathstrings.put(sitename, site);                        
                        }
                        
                        if (nextLine [3] != null) {
                            // Now check for the another level
                            String blockname = nextLine[3].trim ();
                            HashMap<String, String> blocks = site.get(blockname);
                            if (blocks == null) {                        
                                blocks = new HashMap<String, String>();
                                site.put(blockname, blocks); 
                            }
                            
                            if (nextLine [4] != null) {                                
                                String zonename = nextLine [4].trim();
                                String zone = blocks.get(zonename);

                                if (zone == null) {                        
                                    blocks.put(zonename, zonename);                        
                                }
                            }                           
                            
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        LOGGER.log(Level.ALL, "Initializing the data from sites file.");        
        return pathstrings;
    }
    
    // Implemented
    public ArrayList getCountyList_SoilDB () {
        Set <String> countySet = new  HashSet<String> ();
        ArrayList <String> countyList = new ArrayList<String>();
        CSVReader reader;
        String [] nextLine; 
        LOGGER.log(Level.ALL, "Initializing Soil DB Information...");
        try {
            
            //String filepath = csvdirpath +dirseprator+ "dssat_countywise_list_of_soils.csv";
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/dssat_countywise_list_of_soils.csv")));
            //reader = new CSVReader(new FileReader(filepath));
            //reader = new CSVReader(new FileReader(".\\data\\dssat_countywise_list_of_soils.csv"));
            
            while ((nextLine = reader.readNext()) != null) { 
                String county = nextLine[1];                               
                if (county.isEmpty() == false) { 
                    countySet.add(county);
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        
        for (String countyName : countySet) {
            countyList.add(countyName);            
        }        
        Collections.sort(countyList);
        LOGGER.log(Level.ALL, "Initialized Soil DB Information. Returned the list of all the Counties.");
        return countyList;
    }
      
    // Implemented
    public ArrayList getCountyBasedSoilList_SoilDB(String countyName) {
        
        LOGGER.log(Level.ALL, "Initializing the list of soils in a county...");
        
        CSVReader reader;
        String [] nextLine;
        ArrayList<String> soilList = new  ArrayList<String> ();
        Set <String> soillistset = new  HashSet<String> ();        
        try {
            //String filepath = DSSATMain.csvdirpath + "\\dssat_countywise_list_of_soils.csv";
            //reader = new CSVReader(new FileReader(".\\data\\dssat_countywise_list_of_soils.csv"));
            /*String filepath = csvdirpath +dirseprator+ "dssat_countywise_list_of_soils.csv";
            reader = new CSVReader(new FileReader(filepath));*/
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/dssat_countywise_list_of_soils.csv")));
            while ((nextLine = reader.readNext()) != null) { 
                String county = nextLine[1];                               
                if (county.isEmpty() == false && county.equals(countyName)) { 
                   soillistset.add(nextLine[4]);  
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        for (String soilname : soillistset) {
            soilname.trim();
            soilList.add(soilname);            
        }        
        Collections.sort(soilList);
        LOGGER.log(Level.ALL, "Returning the sorted list of soils in a county...");
        return soilList;        
    }
    
    // Implemented
    public ArrayList getCountyList_GlobalDB ()
    {
        LOGGER.log(Level.ALL, "Initializing the list of counies from the Global DB...");
        CSVReader reader;
        String [] nextLine;    
        ArrayList<String> countyList = new  ArrayList<String> ();
        //String query = "Select * from counties_centroid where state = 'FL'";
        try {
            LOGGER.log(Level.ALL, "Getting the Centroid Information from countries_centroid.csv file.");
            String filepath = csvdirpath +dirseprator+ "counties_centroid.csv";
            LOGGER.log(Level.ALL, filepath);
            //String fpath = ".\\data\\counties_centroid.csv";
            
            //reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/counties_centroid.csv")));
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/counties_centroid.csv")));
            while ((nextLine = reader.readNext()) != null) {                
                String state = nextLine[2];                               
                if (state.equals("FL")) { 
                    countyList.add(nextLine[1]);
                }
            }            
        } catch (Exception e) {
            LOGGER.log(Level.ALL, "Failed to retrieve the list of Counties.");
            e.printStackTrace();
            return null;
        }       
        LOGGER.log(Level.ALL, "Successfully retrieved the list of Counties.");
        return countyList;
    }
    
    // Implemented
    public void getCountyLocation_GlobalDB (String countyName, Location pos)  {
         
        LOGGER.log(Level.ALL, "Location Pos Object will be initialized with Centroid Position from Global DB...");
        CSVReader reader;
        String [] nextLine;        
        
        try {
            
            String filepath = DSSATMain.csvdirpath + DSSATMain.dirseprator + "counties_centroid.csv";
            //reader = new CSVReader(new FileReader(".\\data\\counties_centroid.csv"));
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/counties_centroid.csv")));
            while ((nextLine = reader.readNext()) != null) {
                
                String state = nextLine[2];
                String county = nextLine[1];
               
                if (state.equals("FL") && county.equalsIgnoreCase(countyName)) { 
                    String longitude = nextLine[11];
                    
                    String latitude = nextLine[12];
                    
                    pos.latitude = Double.parseDouble(latitude);
                    pos.longitude = Double.parseDouble(longitude);
                    
                    break;                    
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.ALL, "Location Pos Object is initialized with Centroid Position from Global DB...");
    }
    
    // Implemented
    public String getWeatherStationId_GlobalDB (String weatherstationname)
   {        

        CSVReader reader;
        String [] nextLine;
        String location;

        try {
            //reader = new CSVReader(new FileReader(".\\data\\fawn_lookup.csv"));
            String filepath = csvdirpath +dirseprator+ "fawn_lookup.csv";
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/fawn_lookup.csv")));
            while ((nextLine = reader.readNext()) != null) {
                location = nextLine[2];
                location = location.toLowerCase();
                weatherstationname = weatherstationname.toLowerCase();
                if (location.equals(weatherstationname))
                    return nextLine[0];
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
                
        return null;
    }
    
    // Implemented
    public ArrayList getWeatherStations_GlobalDB(Location loc) {        

        double longleft = loc.longitude - .75;
        double longright = loc.longitude + .75;

        
        double latlower = loc.latitude - .75; 
        double latupper = loc.latitude + .75;

        
        
        
        CSVReader reader;
        String [] nextLine;
        String location;
        ArrayList<String> weatherstinrange = new  ArrayList<String> ();
        
        Double latitude;
        Double longitude;        
        
        System.out.println ("location Longitude = " + loc.longitude +"  Location Latitude = " + loc.latitude);
        System.out.println ("Lower Latitude = " + latlower +"  Upper Latitude = " + latupper);
        System.out.println ("Left Longitude = " + longleft +"  Right Longitude = " + longright);
        
        try {
            //reader = new CSVReader(new FileReader(".\\data\\fawn_lookup.csv"));
            String filepath = csvdirpath +dirseprator+ "fawn_lookup.csv";
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/fawn_lookup.csv")));
            while ((nextLine = reader.readNext()) != null) {
                
                latitude = Double.parseDouble(nextLine[5]);
                longitude = Double.parseDouble(nextLine[6]);
                
                if (
                        ((longitude >= longleft) && (longitude <= longright)) && 
                        ((latitude >= latlower) && (latitude <= latupper)))
                {
                    String name = nextLine [2];
                    
                    System.out.println ("Longitude = " + longitude +"  Latitude = " + latitude);
                    weatherstinrange.add(name);
                } 
                
                
                
                double dist = 0;
                /*
                    Haversine
                    formula:	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
                    c = 2 ⋅ atan2( √a, √(1−a) )
                    d = R ⋅ c
                    where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
                    note that angles need to be in radians to pass to trig functions!                
                */
                double r = 6371;
                double radLatCentroid = Math.toRadians(loc.latitude);
                double radLongCentroid = Math.toRadians(loc.longitude); 
                
                double radPoint = Math.toRadians(latitude);
                
                //double  a = 
                
                
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }       

        return weatherstinrange;
    }   
    
    
}
