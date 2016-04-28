/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.dssat1;

import com.opencsv.CSVReader;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.agmip.ui.utils.AppKeys;
import org.agmip.ui.utils.CSVFileHandler;
import org.agmip.ui.utils.Couple;
import org.agmip.ui.utils.CultivaFileSystem;
import org.agmip.ui.utils.DssatUtil;
import org.agmip.ui.utils.FrameTracker;
import org.agmip.ui.utils.Location;
import org.agmip.ui.utils.PathGenerator;
import org.agmip.ui.utils.ReadAndInitIrrigationFertInfo;
import org.agmip.ui.utils.SiteListFileManager;
import org.agmip.ui.utils.SoilDataGenerator;
import org.agmip.ui.utils.WeatherFileSystem;

/**
 *
 * @author rohit
 */
public class GeneralInfo extends javax.swing.JFrame {
    
    /**
     * Creates new form GeneralInfo
     */
    public GeneralInfo(String siteName) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("/images/Dripper.jpg"));
        initComponents();
        
        PathGenerator.initAbsoluteDataPath();
        ReadAndInitIrrigationFertInfo.initFertInfo();
        
        initPlantingMethod();
        Date date = new Date();
        jPlantingDateChooser.setDate(date);
        initGlobalDBInfo();
        initSoilInfo ();
        initCropInfo ();


        setSiteName(siteName);
        setLocation(400, 50);
        setResizable(false);
        
    }
    
    public void setSiteName(String siteName) {
        String[] str = siteName.split(";");
        jSiteNameText.setText(str[0]);
        jBlockNameText.setText(str[1]);
        jZoneNameText.setText(str[2]);

        String site = str[0];        
        String block = str[1];

        String siteCode = site.toUpperCase().substring(0, 2) + "" + block.toUpperCase().substring(0, 2);
        DssatUtil.updateInfo(AppKeys.STATION_CODE, siteCode);
    }
    
     private void initGlobalDBInfo() {
        //LOGGER.log(Level.ALL, "Initializing Global Data information....");
        //CSVFileHandler csvfile = new CSVFileHandler ();
        // From the global db initialize the county comboBox
        ArrayList<String> countyList = null;
        countyList = CSVFileHandler.getCountyList_GlobalDB();
        if (countyList == null) {
            //LOGGER.log(Level.ALL, "County List is not initialized. Exiting the application!");
            System.exit(0);
            return;
        }
        Collections.sort(countyList);
        for (int i = 0; i < countyList.size(); i++) {
            String countyName = countyList.get(i);
            countyName = countyName.substring(0, 1).toUpperCase() + countyName.substring(1).toLowerCase();

            countyNameGlobal.addItem(countyName);
        }

        //LOGGER.log(Level.ALL, "Added the county names.");

        // By default get the first item in the list and get the Co-Ordinates of the at item and populate the 
        // weather stations based on the first item in the combobox 
        Location countypos = new Location();
        CSVFileHandler.getCountyLocation_GlobalDB(countyList.get(0), countypos);

        ArrayList<String> weatherstations = null;
        weatherstations = CSVFileHandler.getWeatherStations_GlobalDB(countypos);
        Collections.sort(weatherstations);

        int itemCount = weatherStComboBox.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            weatherStComboBox.removeItemAt(0);
        }

        System.out.println("Total Number of Fawn Weather Stations = " + weatherstations.size() + " for " + countyList.get(0));

        //weatherStComboBox.
        for (int i = 0; i < weatherstations.size(); i++) {

            String wtstationname = weatherstations.get(i);
            wtstationname = wtstationname.substring(0, 1).toUpperCase() + wtstationname.substring(1).toLowerCase();

            System.out.println(wtstationname);
            weatherStComboBox.addItem(wtstationname);
        }

        //LOGGER.log(Level.ALL, "Initialized Fawn Weather stations.");
        //LOGGER.log(Level.ALL, "Initialized Global Data information....");

    }
     
    private void initSoilInfo() {

        //LOGGER.log(Level.ALL, "Initializing soil information....");
        // From the global db initialize the county comboBox
        ArrayList<String> soilList = null;
        ArrayList<String> countyList = null;
        countyList = CSVFileHandler.getCountyList_SoilDB();
        Collections.sort(countyList);

        String countName = (String) countyNameGlobal.getSelectedItem();
        soilList = CSVFileHandler.getCountyBasedSoilList_SoilDB(countName);

        for (int i = 0; i < soilList.size(); i++) {
            soilSeriesCombobox.addItem(soilList.get(i));
        }
        //LOGGER.log(Level.ALL, "Initialized soil information....");

    }
    
    private void initCropInfo() {

        for (String cropName : CSVFileHandler.getCropNameList()) {
            jComboBoxPrevCropList.addItem(cropName);
            jComboBoxCropList.addItem(cropName);
        }

        //LOGGER.log(Level.ALL, "Initialized crop information.");
        //Check for C://DSSAT46
        File f = new File("C:\\DSSAT45");
        if (f.exists()) {
            dssatPath.setEditable(false);
            dssatPath.setText(f.getAbsolutePath());
            PathGenerator.setDssatInstallationFolder("C:\\DSSAT45");
        }

        initCultivar();
    }
    
    private void initCultivar() {

        //LOGGER.log(Level.ALL, "Initializing cultivar information.");
        String cropName = (String) jComboBoxCropList.getSelectedItem();
        HashMap<String, String> cultivarHashMap = null;

        CultivaFileSystem.updatedCache(AppKeys.CROP_NAME, cropName);
        CultivaFileSystem.updatedCache(AppKeys.CULTIVAR_FILE_NAME, CSVFileHandler.getCropToFileNameMap().get(cropName));
        cultivarHashMap = CultivaFileSystem.ReadFromFile("VAR-NAME");

        ArrayList<String> cultivalist = new ArrayList<String>();
        Set keys = cultivarHashMap.keySet();
        Iterator itr = keys.iterator();

        String key;
        String value;
        while (itr.hasNext()) {
            key = (String) itr.next();
            value = cultivarHashMap.get(key);
            cultivalist.add(value);
        }
        Collections.sort(cultivalist);
        int itemCount = jComboBoxCultivar.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            jComboBoxCultivar.removeItemAt(0);
        }

        for (int i = 0; i < cultivalist.size(); i++) {
            key = cultivalist.get(i);
            String cultivarname = key.trim();

            if (cultivarname.length() > 0) {
                jComboBoxCultivar.addItem(cultivarname);
            }
        }
        //LOGGER.log(Level.ALL, "Initialized cultivar information.");
    }

    private void initPlantingMethod() {
        jComboBoxPlantingMethod.removeAllItems();
        for (Couple p : ReadAndInitIrrigationFertInfo.getPlantingMethodsList()) {
            jComboBoxPlantingMethod.addItem(p.getMethod());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jBlockNameText = new javax.swing.JTextField();
        jBlockNameLabel = new javax.swing.JLabel();
        jZoneNameLabel = new javax.swing.JLabel();
        jZoneNameText = new javax.swing.JTextField();
        jPlantingDateLabel = new javax.swing.JLabel();
        jPlantingDateChooser = new com.toedter.calendar.JDateChooser();
        jLocationLabel = new javax.swing.JLabel();
        jSiteNameLabel = new javax.swing.JLabel();
        jSiteNameText = new javax.swing.JTextField();
        dssatLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jWSLabel = new javax.swing.JLabel();
        weatherStComboBox = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        soilSeriesCombobox = new javax.swing.JComboBox();
        jSoilLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jComboBoxPrevCropList = new javax.swing.JComboBox();
        jPrevCropLabel = new javax.swing.JLabel();
        jComboBoxCropList = new javax.swing.JComboBox();
        jCropNameLabel = new javax.swing.JLabel();
        jComboBoxCultivar = new javax.swing.JComboBox();
        jCultiVarLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        dssatPath = new javax.swing.JTextField();
        browse = new javax.swing.JButton();
        countyNameGlobal = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jBedWidthET = new javax.swing.JTextField();
        jBedWidthLabel = new javax.swing.JLabel();
        jBedHeightLabel = new javax.swing.JLabel();
        jBedHeightET = new javax.swing.JTextField();
        jRowSpacingLabel = new javax.swing.JLabel();
        bedRowSpacing = new javax.swing.JTextField();
        jPlasticLabel = new javax.swing.JLabel();
        jComboBoxPlastic = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jPlantSpaceLabel = new javax.swing.JLabel();
        plantingSpacingInRow = new javax.swing.JTextField();
        jComboBoxPlantingMethod = new javax.swing.JComboBox();
        jPlantingDepthEdit = new javax.swing.JTextField();
        jPlantSpaceLabel1 = new javax.swing.JLabel();
        jPlantingDepthLabel = new javax.swing.JLabel();
        messageDisplayLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(877, 790));

        jBlockNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBlockNameTextActionPerformed(evt);
            }
        });

        jBlockNameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBlockNameLabel.setText("Block Name*");

        jZoneNameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jZoneNameLabel.setText("Zone*");

        jZoneNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jZoneNameTextActionPerformed(evt);
            }
        });

        jPlantingDateLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlantingDateLabel.setText("Planting Date");

        jPlantingDateChooser.setDateFormatString("MMM, dd, yyyy");
        jPlantingDateChooser.setMinimumSize(new java.awt.Dimension(235, 26));

        jLocationLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLocationLabel.setText("Your Location");

        jSiteNameLabel.setText("Farm Name*");

        jSiteNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSiteNameTextActionPerformed(evt);
            }
        });

        dssatLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dssatLabel.setText("DSSAT Installation Folder");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Weather Information"));

        jWSLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jWSLabel.setText("Nearest FAWN Weather Station");
        jWSLabel.setToolTipText("");

        weatherStComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        weatherStComboBox.setMaximumSize(new java.awt.Dimension(235, 26));
        weatherStComboBox.setMinimumSize(new java.awt.Dimension(235, 26));
        weatherStComboBox.setPreferredSize(new java.awt.Dimension(235, 26));
        weatherStComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weatherStComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jWSLabel)
                    .addComponent(weatherStComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jWSLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherStComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Soil Information"));

        soilSeriesCombobox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        soilSeriesCombobox.setMaximumSize(new java.awt.Dimension(235, 26));
        soilSeriesCombobox.setMinimumSize(new java.awt.Dimension(235, 26));
        soilSeriesCombobox.setPreferredSize(new java.awt.Dimension(235, 26));

        jSoilLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jSoilLabel.setText("Soil Series Name");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soilSeriesCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSoilLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jSoilLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(soilSeriesCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Crop Information"));

        jComboBoxPrevCropList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxPrevCropList.setMaximumSize(new java.awt.Dimension(235, 26));
        jComboBoxPrevCropList.setMinimumSize(new java.awt.Dimension(235, 26));
        jComboBoxPrevCropList.setPreferredSize(new java.awt.Dimension(235, 26));

        jPrevCropLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPrevCropLabel.setText("Previous Crop From List");

        jComboBoxCropList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxCropList.setMaximumSize(new java.awt.Dimension(235, 26));
        jComboBoxCropList.setMinimumSize(new java.awt.Dimension(235, 26));
        jComboBoxCropList.setPreferredSize(new java.awt.Dimension(235, 26));
        jComboBoxCropList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxCropListItemStateChanged(evt);
            }
        });
        jComboBoxCropList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCropListActionPerformed(evt);
            }
        });

        jCropNameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCropNameLabel.setText("Crop Name");

        jComboBoxCultivar.setEditable(true);
        jComboBoxCultivar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxCultivar.setMaximumSize(new java.awt.Dimension(235, 26));
        jComboBoxCultivar.setMinimumSize(new java.awt.Dimension(235, 26));
        jComboBoxCultivar.setPreferredSize(new java.awt.Dimension(235, 26));

        jCultiVarLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCultiVarLabel.setText("Cultivar");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPrevCropLabel)
                    .addComponent(jCultiVarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCultivar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCropList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCropNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPrevCropList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPrevCropLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxPrevCropList, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCropNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxCropList, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCultiVarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxCultivar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        dssatPath.setEditable(false);
        dssatPath.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dssatPath.setMaximumSize(new java.awt.Dimension(235, 26));
        dssatPath.setMinimumSize(new java.awt.Dimension(235, 26));
        dssatPath.setPreferredSize(new java.awt.Dimension(235, 26));

        browse.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        browse.setText("Browse");
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        countyNameGlobal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        countyNameGlobal.setMaximumSize(new java.awt.Dimension(235, 26));
        countyNameGlobal.setMinimumSize(new java.awt.Dimension(235, 26));
        countyNameGlobal.setPreferredSize(new java.awt.Dimension(235, 26));
        countyNameGlobal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countyNameGlobalActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Bed System"));

        jBedWidthET.setColumns(10);
        jBedWidthET.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBedWidthET.setText("8.0");
        jBedWidthET.setToolTipText("Valid Bed Width 8-36 inches.");
        jBedWidthET.setMaximumSize(new java.awt.Dimension(160, 26));
        jBedWidthET.setMinimumSize(new java.awt.Dimension(160, 26));
        jBedWidthET.setPreferredSize(new java.awt.Dimension(160, 26));
        jBedWidthET.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jBedWidthETKeyTyped(evt);
            }
        });

        jBedWidthLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBedWidthLabel.setText("Bed Width* (8-36 inches)");
        jBedWidthLabel.setToolTipText("Valid Bed Width 8-36 inches.");
        jBedWidthLabel.setMaximumSize(new java.awt.Dimension(160, 26));
        jBedWidthLabel.setMinimumSize(new java.awt.Dimension(160, 26));
        jBedWidthLabel.setPreferredSize(new java.awt.Dimension(160, 26));

        jBedHeightLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBedHeightLabel.setText("Bed Height* (4-12 inches)");
        jBedHeightLabel.setToolTipText("Valid Bed Hight 4-12 inches.");
        jBedHeightLabel.setPreferredSize(new java.awt.Dimension(160, 26));

        jBedHeightET.setColumns(10);
        jBedHeightET.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBedHeightET.setText("4.0");
        jBedHeightET.setToolTipText("Valid Bed Hight 4-12 inches.");
        jBedHeightET.setMaximumSize(new java.awt.Dimension(160, 26));
        jBedHeightET.setMinimumSize(new java.awt.Dimension(160, 26));
        jBedHeightET.setPreferredSize(new java.awt.Dimension(160, 26));
        jBedHeightET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBedHeightETActionPerformed(evt);
            }
        });
        jBedHeightET.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jBedHeightETKeyTyped(evt);
            }
        });

        jRowSpacingLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRowSpacingLabel.setText("Row Spacing* (4-7 feet)");
        jRowSpacingLabel.setToolTipText("Valid Row Spacing 4-7 feet.");
        jRowSpacingLabel.setMaximumSize(new java.awt.Dimension(106, 26));
        jRowSpacingLabel.setMinimumSize(new java.awt.Dimension(106, 26));
        jRowSpacingLabel.setPreferredSize(new java.awt.Dimension(106, 26));

        bedRowSpacing.setColumns(5);
        bedRowSpacing.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bedRowSpacing.setText("4.0");
        bedRowSpacing.setToolTipText("Valid Row Spacing 4-7 feet.");
        bedRowSpacing.setMaximumSize(new java.awt.Dimension(160, 26));
        bedRowSpacing.setMinimumSize(new java.awt.Dimension(160, 26));
        bedRowSpacing.setPreferredSize(new java.awt.Dimension(160, 26));
        bedRowSpacing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bedRowSpacingActionPerformed(evt);
            }
        });

        jPlasticLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlasticLabel.setText("Plastic Mulch Color*");
        jPlasticLabel.setMaximumSize(new java.awt.Dimension(106, 26));
        jPlasticLabel.setMinimumSize(new java.awt.Dimension(106, 26));
        jPlasticLabel.setPreferredSize(new java.awt.Dimension(106, 26));

        jComboBoxPlastic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxPlastic.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Brown", "Red", "Black", "Gray", "Yellow", " " }));
        jComboBoxPlastic.setMaximumSize(new java.awt.Dimension(160, 26));
        jComboBoxPlastic.setMinimumSize(new java.awt.Dimension(160, 26));
        jComboBoxPlastic.setPreferredSize(new java.awt.Dimension(160, 26));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("-----Planting-----");

        jPlantSpaceLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlantSpaceLabel.setText("Planting Spacing in Row*(4-30 inches)");
        jPlantSpaceLabel.setToolTipText("Valid Planting Spacing is 4-30 inches");
        jPlantSpaceLabel.setMaximumSize(new java.awt.Dimension(160, 26));
        jPlantSpaceLabel.setMinimumSize(new java.awt.Dimension(160, 26));
        jPlantSpaceLabel.setPreferredSize(new java.awt.Dimension(160, 26));

        plantingSpacingInRow.setColumns(5);
        plantingSpacingInRow.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        plantingSpacingInRow.setText("4.0");
        plantingSpacingInRow.setToolTipText("Valid Planting Spacing 4-30 inches.");
        plantingSpacingInRow.setMaximumSize(new java.awt.Dimension(160, 26));
        plantingSpacingInRow.setMinimumSize(new java.awt.Dimension(160, 26));
        plantingSpacingInRow.setPreferredSize(new java.awt.Dimension(160, 26));

        jComboBoxPlantingMethod.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxPlantingMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        jComboBoxPlantingMethod.setMaximumSize(new java.awt.Dimension(160, 26));
        jComboBoxPlantingMethod.setMinimumSize(new java.awt.Dimension(160, 26));
        jComboBoxPlantingMethod.setPreferredSize(new java.awt.Dimension(160, 26));
        jComboBoxPlantingMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPlantingMethodActionPerformed(evt);
            }
        });

        jPlantingDepthEdit.setColumns(5);
        jPlantingDepthEdit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlantingDepthEdit.setText("0.0");
        jPlantingDepthEdit.setToolTipText("Valid Planting Depth 0-6 inches.");
        jPlantingDepthEdit.setMaximumSize(new java.awt.Dimension(160, 26));
        jPlantingDepthEdit.setMinimumSize(new java.awt.Dimension(160, 26));
        jPlantingDepthEdit.setPreferredSize(new java.awt.Dimension(160, 26));
        jPlantingDepthEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPlantingDepthEditActionPerformed(evt);
            }
        });

        jPlantSpaceLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlantSpaceLabel1.setText("Planting Depth*(0-6 inches)");
        jPlantSpaceLabel1.setToolTipText("Valid Planting Depth 0-6 inches.");
        jPlantSpaceLabel1.setMaximumSize(new java.awt.Dimension(160, 26));
        jPlantSpaceLabel1.setMinimumSize(new java.awt.Dimension(160, 26));
        jPlantSpaceLabel1.setPreferredSize(new java.awt.Dimension(160, 26));

        jPlantingDepthLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPlantingDepthLabel.setText("Planting Method*");
        jPlantingDepthLabel.setMaximumSize(new java.awt.Dimension(160, 26));
        jPlantingDepthLabel.setMinimumSize(new java.awt.Dimension(160, 26));
        jPlantingDepthLabel.setPreferredSize(new java.awt.Dimension(160, 26));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPlantSpaceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(plantingSpacingInRow, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPlantingDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxPlantingMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBedWidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBedHeightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRowSpacingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPlasticLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxPlastic, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bedRowSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBedHeightET, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBedWidthET, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPlantSpaceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPlantingDepthEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBedWidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBedWidthET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jBedHeightET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jBedHeightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRowSpacingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bedRowSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPlasticLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPlastic, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPlantSpaceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(plantingSpacingInRow, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPlantingDepthEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPlantSpaceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPlantingMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPlantingDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        messageDisplayLabel.setForeground(new java.awt.Color(255, 51, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DSSAT 2D");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(144, 144, 144)
                                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jBlockNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jBlockNameText, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSiteNameText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jZoneNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSiteNameLabel)
                                    .addComponent(jZoneNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(94, 94, 94)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dssatPath, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(countyNameGlobal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPlantingDateLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPlantingDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLocationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(102, 102, 102))
                                            .addComponent(dssatLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(browse)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(messageDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(messageDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSiteNameLabel)
                            .addComponent(jPlantingDateLabel))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSiteNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPlantingDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jBlockNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBlockNameText))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLocationLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countyNameGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jZoneNameLabel)
                                .addComponent(dssatLabel))
                            .addComponent(browse))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jZoneNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dssatPath, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nextButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(940, Short.MAX_VALUE))
        );

        jPanel5.getAccessibleContext().setAccessibleName("Soil Information");

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBlockNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBlockNameTextActionPerformed
        // TODO add your handling code here:
        //nextButtonActionPerformed(evt);
    }//GEN-LAST:event_jBlockNameTextActionPerformed

    private void jSiteNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSiteNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSiteNameTextActionPerformed

    private void jZoneNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jZoneNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jZoneNameTextActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        messageDisplayLabel.setText("");
        try {
            double rowSpacing = Double.parseDouble(bedRowSpacing.getText());
            
            // Validation
            if (rowSpacing < 4 || rowSpacing > 7) {
                messageDisplayLabel.setText("Valid ROW SPACING 4-7 feet.");
                return;
            }

            // Convert feet to inches 
            rowSpacing = rowSpacing * 12;
            
            double bedWidth = Double.parseDouble(jBedWidthET.getText());
            if (rowSpacing < bedWidth) {
                messageDisplayLabel.setText("ROW SPACING should be more than BED WIDTH.");
                return;
            }
            
            if (bedWidth<8 || bedWidth > 36) {
                messageDisplayLabel.setText("Valid BED WIDTH is 8-36 inches.");
                return;
            }
            
            double bedHeight = Double.parseDouble(jBedHeightET.getText());
            if (bedHeight < 4 || bedHeight > 12) {
                messageDisplayLabel.setText("Valid BED HEIGHT is 4-12 inches.");
                return;
            }

            bedWidth*=2.54;
            bedHeight*=2.54;

            // Validation for plant Spacing 
            double plantingSpacing = Double.parseDouble(plantingSpacingInRow.getText());
            if (plantingSpacing < 4 || plantingSpacing > 30) {
                messageDisplayLabel.setText("Valid PLANTING SPACING is 4-30 inches.");
                return;
            }
              
            double plantingDepth = Double.parseDouble(jPlantingDepthEdit.getText());
            if (plantingDepth > 6) {
                messageDisplayLabel.setText("Valid PLANTING DEPTH is 0-6 inches.");
                return;
            }
        } catch (NumberFormatException e) {
            messageDisplayLabel.setText("Please enter numerical values in bed width and row spacing");
            return;
        }
        if (PathGenerator.getDssatInstallationFolder().length() < 1) {
            messageDisplayLabel.setText("Please select DSSAT Installation folder");
            return;
        }

        if (UpdateFile() == true) {

        } else {
            messageDisplayLabel.setText("Please update necessary information.");
        }

        if (FrameTracker.second == null) {
            FrameTracker.second = new IrrigationFrame();
        }
       
        // Create weather file
        WeatherFileSystem.WriteToFile();
        SoilDataGenerator.GenerateSoilData();
        
        FrameTracker.first = this;
        this.setVisible(false);
        FrameTracker.second.setVisible(true);
    }//GEN-LAST:event_nextButtonActionPerformed

    private boolean UpdateFile() {

        //LOGGER.log(Level.ALL, "Updating the cache with the latest information...");

        //StringBuilder weatherdata = new StringBuilder("");
        String str = new String("");
        boolean isSuccess = true;


        str = jSiteNameText.getText();

        if (!str.trim().isEmpty()) {
            DssatUtil.updateInfo(AppKeys.FARM, str);
            jSiteNameLabel.setForeground(Color.BLACK);
        } else {
            jSiteNameLabel.setForeground(Color.RED);
            isSuccess = false;
        }
        
        
        str = jBlockNameText.getText();
        if (!str.trim().isEmpty()) {
            jBlockNameLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.BLOCK, str);
            
        } else {
            jBlockNameLabel.setForeground(Color.red);
            isSuccess = false;
        }
        
        str = jZoneNameText.getText();
        if (!str.trim().isEmpty()) {
            jZoneNameLabel.setForeground(Color.BLACK);
        } else {
            jZoneNameLabel.setForeground(Color.red);
            isSuccess = false;
            DssatUtil.updateInfo(AppKeys.ZONE, str);
        }

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        str = format.format(jPlantingDateChooser.getDate());
        if (str.trim().isEmpty()) {
            jPlantingDateLabel.setForeground(Color.red);
            isSuccess = false;
        } else {
            jPlantingDateLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.PLANTING_DATE, str);
        }

        DssatUtil.updateInfo(AppKeys.LOCATION, countyNameGlobal.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.WEATHER_STATION, weatherStComboBox.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.SOIL, soilSeriesCombobox.getSelectedItem().toString());
        
        // This Updates the Soil Code
        String soilName = DssatUtil.getInfo(AppKeys.SOIL);
        String countyName = DssatUtil.getInfo(AppKeys.LOCATION);

        
        
        DssatUtil.updateInfo(AppKeys.PREVIOUS_CROP, jComboBoxPrevCropList.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.CROP, jComboBoxCropList.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.CULTIVAR, jComboBoxCultivar.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.DSSATFOLDER, dssatPath.getText());
        
        PathGenerator.setCropDirectory();
        // Pass the soild name to the method and get back the Soil Id
        String soilId = CSVFileHandler.getSoilid(countyName, soilName);
        DssatUtil.updateInfo(AppKeys.SOIL_ID, soilId);
        PathGenerator.setSoilFilePath(soilId);
        


        str = jBedWidthET.getText();
        double cmval = Double.parseDouble(str);
        cmval*=2.54;
        if (str.trim().isEmpty()) {
            jBedWidthLabel.setForeground(Color.red);
            isSuccess = false;
        } else {
            jBedWidthLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.BED_WIDTH, String.valueOf(cmval));
        }
        DssatUtil.updateInfo(AppKeys.CROP, jComboBoxCropList.getSelectedItem().toString());

        str = jBedHeightET.getText();
        cmval = Double.parseDouble(str);
        cmval*=2.54;
        if (str.trim().isEmpty()) {
            jBedWidthLabel.setForeground(Color.red);
            isSuccess = false;
        } else {
            jBedWidthLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.BED_HEIGHT, String.valueOf(cmval));
        }

        str = jComboBoxPlastic.getSelectedItem().toString();
        
        /*if (str.equals("Black")) {
            str="0.03";
        } else if (str.equals("Silver")) {
            str="0.39";
        } else if (str.equals("White")) {
            str="0.48";
        } else {
            str="";
        }*/
        
        if (str.equals("Brown")) {
            str="0.13";
        } else if (str.equals("Red")) {
            str="0.14";
        } else if (str.equals("Black")) {
            str="0.09";
        } else if (str.equals("Gray")) {
            str="0.13";
        } else if (str.equals("Yellow")) {
            str="0.17";
        } else {
            str="";
        }
        
        DssatUtil.updateInfo(AppKeys.PLASTIC_MULCH_COLOR, str);
        String plantingMethod = jComboBoxPlantingMethod.getSelectedItem().toString();
        DssatUtil.updateInfo(AppKeys.PLANTING_METHOD, plantingMethod);
        DssatUtil.updateInfo(AppKeys.PLANTING_METHOD_CODE, ReadAndInitIrrigationFertInfo.getPlantingMethodToCodeMap().get(plantingMethod).trim());
       
        str = jPlantingDepthEdit.getText();
        double depth = 0.0;
        try {
            depth = Double.parseDouble(str);
            depth = depth * 2.54;
            
            // The translator is considering this depth in MM So I am multiplying by 10
            jRowSpacingLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.PLANTING_DEPTH, String.valueOf(depth*10));
        } catch (NumberFormatException e) {
            jRowSpacingLabel.setForeground(Color.red);
            isSuccess = false;
        }


        str = bedRowSpacing.getText();
        double bedRowSpacingInFeet = 0.0;
        double bedRowSpacingInMeter = 0.0;
        try {
            bedRowSpacingInFeet = Double.parseDouble(str);
            bedRowSpacingInMeter = bedRowSpacingInFeet * 0.3048;
            jRowSpacingLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.BED_ROW_SPACING, String.valueOf(bedRowSpacingInFeet));
        } catch (NumberFormatException e) {
            jRowSpacingLabel.setForeground(Color.red);
            if(jComboBoxPlantingMethod.getSelectedIndex() != 7)
                isSuccess = false;
        }

        str = plantingSpacingInRow.getText();
        double plantingSpacingInRowInches = 0.0;
        double plantingSpacingInRowCm = 0.0;
        double plantingSpacingInRowMeter = 0.0;
        try {
            plantingSpacingInRowInches = Double.parseDouble(str);
            plantingSpacingInRowCm = plantingSpacingInRowInches * 2.54;
            plantingSpacingInRowMeter = plantingSpacingInRowInches * 0.0254;
            jRowSpacingLabel.setForeground(Color.BLACK);
            DssatUtil.updateInfo(AppKeys.PLANTING_ROW_SPACING, String.valueOf(plantingSpacingInRowCm));
        } catch (NumberFormatException e) {
            jRowSpacingLabel.setForeground(Color.red);
            isSuccess = false;
        }

        String[] crop = CSVFileHandler.getCropID(jComboBoxCropList.getSelectedItem().toString()).split(",");
        String culFile = crop[1];
        String cropCode = crop[0];
        DssatUtil.updateInfo(AppKeys.CROP_ID, cropCode);


        String culCode = CSVFileHandler.getCulId(culFile, jComboBoxCultivar.getSelectedItem().toString());
        DssatUtil.updateInfo(AppKeys.DSSAT_CULTIVAR_ID, culCode);

       
        double ppop = 1 / (plantingSpacingInRowMeter * bedRowSpacingInMeter);
        double ppoe = 1 / (plantingSpacingInRowMeter * bedRowSpacingInMeter);
        
        DssatUtil.updateInfo(AppKeys.PLANT_POPULATION_AT_SEEDING, String.valueOf(ppop));
        DssatUtil.updateInfo(AppKeys.PLANT_POPULATION_AT_EMERGENCE, String.valueOf(ppoe));
        String weatherStation = (String) weatherStComboBox.getSelectedItem();
        DssatUtil.updateInfo(AppKeys.WEATHER_STATION, weatherStation);
        DssatUtil.updateInfo(AppKeys.STATION_LOCATION_ID, CSVFileHandler.getWeatherStationId_GlobalDB(weatherStation));

        SiteListFileManager.setSiteListFile();
        
        return isSuccess;
    }
    
    private void jBedWidthETKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBedWidthETKeyTyped
        // TODO add your handling code here:
        

    }//GEN-LAST:event_jBedWidthETKeyTyped

    private void jBedHeightETKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBedHeightETKeyTyped

    }//GEN-LAST:event_jBedHeightETKeyTyped

    private void jBedHeightETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBedHeightETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBedHeightETActionPerformed

    private void bedRowSpacingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bedRowSpacingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bedRowSpacingActionPerformed

    private void jComboBoxPlantingMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPlantingMethodActionPerformed
        // TODO add your handling code here:
        int id = jComboBoxPlantingMethod.getSelectedIndex();
        if(id == 7){ //transplants
            jPlantingDepthLabel.setText("Planting Method");
        }else{
            jPlantingDepthLabel.setText("Planting Method*");
        }
    }//GEN-LAST:event_jComboBoxPlantingMethodActionPerformed

    private void jPlantingDepthEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPlantingDepthEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPlantingDepthEditActionPerformed

    private void countyNameGlobalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countyNameGlobalActionPerformed
        // TODO add your handling code here:

        System.out.println ("countyNameGlobalActionPerformed");
        //LOGGER.log(Level.ALL, "Method Name:countyNameGlobalActionPerformed" + "Initializing information based on county Name.");

        String countyName = (String) countyNameGlobal.getSelectedItem();

        // By default get the first item in the list and get the Co-Ordinates of the at item and populate the
        // weather stations based on the first item in the combobox
        int itemCount = weatherStComboBox.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            weatherStComboBox.removeItemAt(0);
        }

        //CSVFileHandler csvfilehandler = new CSVFileHandler ();
        Location countypos = new Location();
        CSVFileHandler.getCountyLocation_GlobalDB(countyName, countypos);

        ArrayList<String> weatherstations = null;
        weatherstations = CSVFileHandler.getWeatherStations_GlobalDB(countypos);
        Collections.sort(weatherstations);

        System.out.println("Total Number of Fawn Weather Stations = " + weatherstations.size() + " for " + countyName);

        //weatherStComboBox.
        for (int i = 0; i < weatherstations.size(); i++) {
            String wtstationname = weatherstations.get(i);
            wtstationname = wtstationname.substring(0, 1).toUpperCase() + wtstationname.substring(1).toLowerCase();

            System.out.println(wtstationname);
            weatherStComboBox.addItem(wtstationname);
        }
        
        if (weatherstations.size()>0) {
            weatherStComboBox.setSelectedIndex(0);
        }

        ArrayList<String> soilList = null;
        soilList = CSVFileHandler.getCountyBasedSoilList_SoilDB(countyName);

        int soilitemCount = soilSeriesCombobox.getItemCount();
        for (int i = 0; i < soilitemCount; i++) {
            soilSeriesCombobox.removeItemAt(0);
        }

        for (int i = 0; i < soilList.size(); i++) {
            //System.out.print(soilList.get(i));
            soilSeriesCombobox.addItem(soilList.get(i));
        }
        
        if (soilList.size()>0) {
            soilSeriesCombobox.setSelectedIndex(0);
        }

    }//GEN-LAST:event_countyNameGlobalActionPerformed

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        
               
        JFileChooser jc = new JFileChooser();
        jc.setCurrentDirectory(new File("C:\\"));
        jc.setDialogTitle("Select DSSAT installation");
        jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jc.setAcceptAllFileFilterUsed(false);
        if (jc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            PathGenerator.setDssatInstallationFolder(jc.getSelectedFile().getAbsolutePath());
            dssatPath.setText(PathGenerator.getDssatInstallationFolder());
        }
        
    }//GEN-LAST:event_browseActionPerformed

    private void weatherStComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weatherStComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_weatherStComboBoxActionPerformed

    private void jComboBoxCropListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxCropListItemStateChanged
        // TODO add your handling code here:
        // TODO add your handling code here:

        HashMap<String, String> cropToFileNameMap = CSVFileHandler.getCropToFileNameMap();
        
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            //initCultivar();
             String cropName = (String)jComboBoxCropList.getSelectedItem();
            HashMap <String, String> cultivarHashMap = null;

            StringBuilder cultivadata = new StringBuilder ();

            cultivadata.append ("Crop Name,");
            cultivadata.append(cropName);

            cultivadata.append (",CultivarFile,");
            String culfileName = cropToFileNameMap.get(cropName);
            cultivadata.append(culfileName);

            System.out.println ("Crop Name" + cropName);

            

            CultivaFileSystem.UpdateCache(cultivadata.toString());
            cultivarHashMap = CultivaFileSystem.ReadFromFile("VAR-NAME");

            ArrayList<String> cultivalist = new ArrayList<String> ();
            Set keys = cultivarHashMap.keySet();
            Iterator itr = keys.iterator();

            String key;
            String value;
            while(itr.hasNext())
            {
                key = (String)itr.next();
                String val = cultivarHashMap.get(key);
                cultivalist.add(val);
            }
            Collections.sort(cultivalist);

            int itemCount = jComboBoxCultivar.getItemCount();
            for(int i=0;i<itemCount;i++){
                jComboBoxCultivar.removeItemAt(0);
            }

            for (int i = 0; i < cultivalist.size(); i++) {
                key = cultivalist.get(i);
                key = key.trim();
                jComboBoxCultivar.addItem(key);
            } 

        }
    }//GEN-LAST:event_jComboBoxCropListItemStateChanged

    private void jComboBoxCropListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCropListActionPerformed

    }//GEN-LAST:event_jComboBoxCropListActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bedRowSpacing;
    private javax.swing.JButton browse;
    private javax.swing.JComboBox countyNameGlobal;
    private javax.swing.JLabel dssatLabel;
    private javax.swing.JTextField dssatPath;
    private javax.swing.JTextField jBedHeightET;
    private javax.swing.JLabel jBedHeightLabel;
    private javax.swing.JTextField jBedWidthET;
    private javax.swing.JLabel jBedWidthLabel;
    private javax.swing.JLabel jBlockNameLabel;
    private javax.swing.JTextField jBlockNameText;
    private javax.swing.JComboBox jComboBoxCropList;
    private javax.swing.JComboBox jComboBoxCultivar;
    private javax.swing.JComboBox jComboBoxPlantingMethod;
    private javax.swing.JComboBox jComboBoxPlastic;
    private javax.swing.JComboBox jComboBoxPrevCropList;
    private javax.swing.JLabel jCropNameLabel;
    private javax.swing.JLabel jCultiVarLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLocationLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jPlantSpaceLabel;
    private javax.swing.JLabel jPlantSpaceLabel1;
    private com.toedter.calendar.JDateChooser jPlantingDateChooser;
    private javax.swing.JLabel jPlantingDateLabel;
    private javax.swing.JTextField jPlantingDepthEdit;
    private javax.swing.JLabel jPlantingDepthLabel;
    private javax.swing.JLabel jPlasticLabel;
    private javax.swing.JLabel jPrevCropLabel;
    private javax.swing.JLabel jRowSpacingLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSiteNameLabel;
    private javax.swing.JTextField jSiteNameText;
    private javax.swing.JLabel jSoilLabel;
    private javax.swing.JLabel jWSLabel;
    private javax.swing.JLabel jZoneNameLabel;
    private javax.swing.JTextField jZoneNameText;
    private javax.swing.JLabel messageDisplayLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField plantingSpacingInRow;
    private javax.swing.JComboBox soilSeriesCombobox;
    private javax.swing.JComboBox weatherStComboBox;
    // End of variables declaration//GEN-END:variables
}
