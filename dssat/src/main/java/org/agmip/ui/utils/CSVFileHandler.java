/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import com.opencsv.CSVReader;
//import static dssat.DSSATMain.LOGGER;
//import static dssat.DSSATMain.csvdirpath;
//import static dssat.DSSATMain.dirseprator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author rkmalik
 */
public class CSVFileHandler {

    public static HashMap<String, HashMap<String, HashMap<String, String>>> ReadSitesFile() {
        HashMap<String, HashMap<String, HashMap<String, String>>> pathstrings = new HashMap<String, HashMap<String, HashMap<String, String>>>();
        CSVReader reader;
        String[] nextLine;
        //LOGGER.log(Level.ALL, "Initializing the data from sites file.");

        try {
            String filepath = PathGenerator.getCurDirPath() + PathGenerator.getDirSeperator() + "sites" + PathGenerator.getDirSeperator() + "sitesinfo.sites";
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/sites/sitesinfo.sites")));
            while ((nextLine = reader.readNext()) != null) {

                String path = nextLine[1];
                if (path.trim().equalsIgnoreCase("sites")) {

                    if (nextLine[2] != null) {
                        String sitename = nextLine[2].trim();
                        HashMap<String, HashMap<String, String>> site = pathstrings.get(sitename);
                        if (site == null) {
                            site = new HashMap<>();
                            pathstrings.put(sitename, site);
                        }

                        if (nextLine[3] != null) {
                            // Now check for the another level
                            String blockname = nextLine[3].trim();
                            HashMap<String, String> blocks = site.get(blockname);
                            if (blocks == null) {
                                blocks = new HashMap<>();
                                site.put(blockname, blocks);
                            }

                            if (nextLine[4] != null) {
                                String zonename = nextLine[4].trim();
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

        //LOGGER.log(Level.ALL, "Initializing the data from sites file.");
        return pathstrings;
    }

    // Implemented
    public static ArrayList getCountyList_SoilDB() {
        Set<String> countySet = new HashSet<>();
        ArrayList<String> countyList = new ArrayList<>();
        CSVReader reader;
        String[] nextLine;
        //LOGGER.log(Level.ALL, "Initializing Soil DB Information...");
        try {
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/dssat_countywise_list_of_soils.csv")));
            while ((nextLine = reader.readNext()) != null) {
                String county = nextLine[1];
                if (county.isEmpty() == false) {
                    countySet.add(county);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
            for (String countyName : countySet) {
                countyList.add(countyName);
            }
         */
        countySet.stream().forEach((countyName) -> {
            countyList.add(countyName);
        });
        Collections.sort(countyList);
        //LOGGER.log(Level.ALL, "Initialized Soil DB Information. Returned the list of all the Counties.");
        return countyList;
    }

    // Implemented
    public static ArrayList getCountyBasedSoilList_SoilDB(String countyName) {

        //LOGGER.log(Level.ALL, "Initializing the list of soils in a county...");
        CSVReader reader;
        String[] nextLine;
        ArrayList<String> soilList = new ArrayList<String>();
        Set<String> soillistset = new HashSet<String>();
        try {
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/dssat_countywise_list_of_soils.csv")));
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
        //LOGGER.log(Level.ALL, "Returning the sorted list of soils in a county...");
        return soilList;
    }

    public static String getSoilid(String countyName, String soilName) {

        String soil = null;
        String[] nextLine;
        CSVReader reader;
        //ArrayList<String> soilList = new  ArrayList<String> ();
        //Set <String> soillistset = new  HashSet<String> ();        
        try {
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/dssat_countywise_list_of_soils.csv")));
            while ((nextLine = reader.readNext()) != null) {
                String county = nextLine[1];
                //String soil = nextLine[4];
                if ((county.isEmpty() == false && county.equals(countyName))
                        && soilName.equals(nextLine[4])) {
                    // Parse the String     
                    soil = nextLine[nextLine.length - 1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soil;
    }

    // Implemented
    public static ArrayList getCountyList_GlobalDB() {
        //LOGGER.log(Level.ALL, "Initializing the list of counies from the Global DB...");
        CSVReader reader;
        String[] nextLine;
        ArrayList<String> countyList = new ArrayList<String>();
        try {
            //LOGGER.log(Level.ALL, "Getting the Centroid Information from countries_centroid.csv file.");
            String filepath = PathGenerator.getCurDirPath() + PathGenerator.getDirSeperator() + "counties_centroid.csv";
            //LOGGER.log(Level.ALL, filepath);
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/counties_centroid.csv")));
            while ((nextLine = reader.readNext()) != null) {
                String state = nextLine[2];
                if (state.equals("FL")) {
                    countyList.add(nextLine[1]);
                }
            }
        } catch (Exception e) {
            //LOGGER.log(Level.ALL, "Failed to retrieve the list of Counties.");
            e.printStackTrace();
            return null;
        }
        //LOGGER.log(Level.ALL, "Successfully retrieved the list of Counties.");
        return countyList;
    }

    // Implemented
    public static void getCountyLocation_GlobalDB(String countyName, Location pos) {

        //LOGGER.log(Level.ALL, "Location Pos Object will be initialized with Centroid Position from Global DB...");
        CSVReader reader;
        String[] nextLine;

        try {

            String filepath = PathGenerator.getCsvDirPath() + PathGenerator.getDirSeperator() + "counties_centroid.csv";
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/counties_centroid.csv")));
            while ((nextLine = reader.readNext()) != null) {

                String state = nextLine[2];
                String county = nextLine[1];

                if (state.equals("FL") && county.equalsIgnoreCase(countyName)) {
                    String longitude = nextLine[11];

                    String latitude = nextLine[12];

                    pos.setLatitude(Double.parseDouble(latitude));
                    pos.setLongitude(Double.parseDouble(longitude));

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //LOGGER.log(Level.ALL, "Location Pos Object is initialized with Centroid Position from Global DB...");
    }

    // Implemented
    public static String getWeatherStationId_GlobalDB(String weatherstationname) {

        CSVReader reader;
        String[] nextLine;
        String location;

        try {
            //reader = new CSVReader(new FileReader(".\\data\\fawn_lookup.csv"));
            String filepath = PathGenerator.getCsvDirPath() + PathGenerator.getDirSeperator() + "fawn_lookup.csv";
            //reader = new CSVReader(new FileReader(filepath));
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/fawn_lookup.csv")));
            while ((nextLine = reader.readNext()) != null) {
                location = nextLine[2];
                location = location.toLowerCase();
                weatherstationname = weatherstationname.toLowerCase();
                if (location.equals(weatherstationname)) {
                    return nextLine[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Implemented
    public static ArrayList getWeatherStations_GlobalDB(Location loc) {

        double longleft = loc.getLongitude() - .75;
        double longright = loc.getLongitude() + .75;

        double latlower = loc.getLatitude() - .75;
        double latupper = loc.getLatitude() + .75;

        CSVReader reader;
        String[] nextLine;
        String location;
        ArrayList<String> weatherstinrange = new ArrayList<>();

        Double latitude;
        Double longitude;

        System.out.println("location Longitude = " + loc.getLongitude() + "  Location Latitude = " + loc.getLatitude());
        System.out.println("Lower Latitude = " + latlower + "  Upper Latitude = " + latupper);
        System.out.println("Left Longitude = " + longleft + "  Right Longitude = " + longright);

        try {
            String filepath = PathGenerator.getCsvDirPath() + PathGenerator.getDirSeperator() + "fawn_lookup.csv";
            reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/fawn_lookup.csv")));
            while ((nextLine = reader.readNext()) != null) {

                latitude = Double.parseDouble(nextLine[5]);
                longitude = Double.parseDouble(nextLine[6]);

                if (((longitude >= longleft) && (longitude <= longright))
                        && ((latitude >= latlower) && (latitude <= latupper))) {
                    String name = nextLine[2];

                    System.out.println("Longitude = " + longitude + "  Latitude = " + latitude);
                    weatherstinrange.add(name);
                }

                double dist = 0;
                double r = 6371;
                double radLatCentroid = Math.toRadians(loc.getLatitude());
                double radLongCentroid = Math.toRadians(loc.getLongitude());
                double radPoint = Math.toRadians(latitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherstinrange;
    }

    public static String getCropID(String cropName) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/dssat_crop_lookup.csv")));
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row[1].equalsIgnoreCase(cropName)) {
                    return row[0] + "," + row[3];
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getCulId(String filePath, String cultiVar) {

        try {
            InputStreamReader isr = new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/Genotype/" + filePath));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("!")) {
                    continue;
                }
                if (line.contains(cultiVar)) {
                    return line.split(" ")[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> cropNameList = null;
    private static HashMap<String, String> cropNameToFileNameMap = null;

    public static HashMap<String, String> getCropToFileNameMap() {

        if (cropNameToFileNameMap == null) {
            cropNameToFileNameMap = new HashMap<>();
            cropNameList = new ArrayList<> ();
            processCropFile();

        }
        return cropNameToFileNameMap;
    }

    public static ArrayList<String> getCropNameList() {

        if (cropNameList == null) {
            cropNameToFileNameMap = new HashMap<>();
            cropNameList = new ArrayList<> ();
            processCropFile();

        }

        return cropNameList;
    }

    private static void processCropFile() {
        String[] nextLine;
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(CSVFileHandler.class.getClass().getResourceAsStream("/properties/csv/dssat_crop_lookup.csv")));
            while ((nextLine = reader.readNext()) != null) {
                cropNameList.add(nextLine[1]);
                cropNameToFileNameMap.put(nextLine[1], nextLine[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Collections.sort(cropNameList);
    }

}
