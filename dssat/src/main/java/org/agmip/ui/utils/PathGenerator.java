/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author rohit
 */
public class PathGenerator {

    private static String curDirPath;
    private static String csvDirPath;
    private static String culFilePath;
    private static String dataDir;

    private static final String dirSeperator = File.separator;

    private static String dssatInstallationFolder = "";
    private static String weatherDirectory = "";
    private static String weatherFilePath = "";
    private static String cropDirectory = "";
    private static String soilFilePath;

    private static ArrayList<File> translatorOutputPath;

    public static ArrayList<File> getTranslatorOutputPath() {
        return translatorOutputPath;
    }

    public static void setTranslatorOutputPath(ArrayList<File> translatorOutputPath) {
        PathGenerator.translatorOutputPath = translatorOutputPath;
    }

    public static String getSoilFilePath() {
        if (soilFilePath == null || soilFilePath.isEmpty()) {
            setSoilFilePath(null);
        }
        return soilFilePath;
    }

    public static void setSoilFilePath(String soilId) {

        if (soilId == null || soilId.isEmpty()) {
            soilId = DssatUtil.getInfo(AppKeys.SOIL_ID);
        }

        PathGenerator.soilFilePath = cropDirectory + dirSeperator + soilId + ".SOL";
    }

    public static String getWeatherDirectory() {
        if (weatherDirectory == null || weatherDirectory.isEmpty()) {
            setWeatherDirectory();
        }
        return weatherDirectory;
    }

    private static void setWeatherDirectory() {
        PathGenerator.weatherDirectory = dssatInstallationFolder + dirSeperator
                + "Weather";
    }

    public static String getWeatherFilePath() {
        if (weatherFilePath != null && !weatherFilePath.isEmpty()) {
            return weatherFilePath;
        }
        setWeatherFilePath();
        return weatherFilePath;
    }

    private static void setWeatherFilePath() {
        PathGenerator.weatherFilePath = getWeatherDirectory() + dirSeperator + DssatUtil.getInfo(AppKeys.STATION_CODE) + ".WTH";
    }

    public static String getCropDirectory() {
        if (cropDirectory == null || cropDirectory.isEmpty()) {
            setCropDirectory();
        }
        return cropDirectory;
    }

    public static void setCropDirectory() {
        PathGenerator.cropDirectory = dssatInstallationFolder + dirSeperator + DssatUtil.getInfo("Crop");;
    }

    public static String getDssatInstallationFolder() {
        return dssatInstallationFolder;
    }

    public static void setDssatInstallationFolder(String dssatInstallationFolder) {
        PathGenerator.dssatInstallationFolder = dssatInstallationFolder;
    }

    public static String getDataDir() {
        return dataDir;
    }

    public static String getCurDirPath() {
        return curDirPath;
    }

    public static String getCsvDirPath() {
        return csvDirPath;
    }

    public static String getCulFilePath() {
        return culFilePath;
    }

    public static String getDirSeperator() {
        return dirSeperator;
    }

    public static void initAbsoluteDataPath() {
        Path currentRelativePath = Paths.get("");
        curDirPath = currentRelativePath.toAbsolutePath().toString();
        csvDirPath = curDirPath + dirSeperator + "src\\main\\resources\\properties" + dirSeperator + "csv";
        culFilePath = curDirPath + dirSeperator + "src\\main\\resources\\properties" + dirSeperator + "Genotype";
        dataDir = curDirPath + dirSeperator + "data";

        // Check if data directory is availbale or not 
        File dir = new File(dataDir);
        if (!dir.exists()) {
            boolean result = false;

            try {
                dir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
        }
    }

}
