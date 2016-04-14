/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;
import com.opencsv.CSVReader;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.StringTokenizer;


/**
 *
 * @author rkmalik
 */
public class DSSATMain extends javax.swing.JFrame {
    public static final Logger LOGGER = Logger.getLogger(DSSATMain.class.getName());
    //public static final Level = Level.ALL;
    static WeatherFileSystem wthfile;
    static CultivaFileSystem cultivafile;      
    static int weatherstationid;
    public static String curdirpath;
    public static String csvdirpath;
    public static String datadir;
    public static String dirseprator;
    static HashMap<String, String> datamap = new HashMap<String, String> ();
    
    static IrrigationFertilizer irrigationframe;
    
    //static DBConnect global_db = null;    
    //static DBConnect soil_db = null;    
    //static DBConnect weather_historic_daily = null;
    static HashMap <String, String> cropname_culfilename = null;
    
    CalendarProgram calender = null;
    private Object FileChooserDemo;

    
    /**
     * Creates new form DSSATMain
     */
    public DSSATMain() { 
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("/dssat/images/Dripper.jpg"));
        initAbsoluteDataPath ();
        initLogger ();
        initComponents();
        IrrigationFertilizerPanel.setVisible(false);    
        initMyComponents();        
        initTextFields ();
        //initPlantingDate ();        
        initGlobalDBInfo (); 
        initSoilInfo ();        
        initWeatherHistoric ();        
        initCropInfo ();        
        initFertInfo ();        
        initFrameSize ();  
        initializeTree ();
        LOGGER.log(Level.ALL, "Application Initialized.");        
    }
    
    private void initializeTree ()
    {        
        //Read Project File and get the array of Paths
        CSVFileHandler csvfile = new CSVFileHandler ();
        HashMap <String, HashMap<String, HashMap<String, String>>> mymap = csvfile.ReadSitesFile ();
        jProjectExplorer.removeAll();
        // Read the sitesinfo.sites file and read the data from the file. 
        DefaultTreeModel model = (DefaultTreeModel) jProjectExplorer.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        
        root.setUserObject("Sites");
        model.nodeChanged(root);        

        //for (int sites = 0; sites < ; sites++) {
         for (Map.Entry<String,HashMap<String, HashMap<String, String>>> esites : mymap.entrySet()){
            
            String sitename = esites.getKey();
            HashMap<String, HashMap<String, String>> myblocks = esites.getValue();
            DefaultMutableTreeNode site = new DefaultMutableTreeNode(sitename);
            root.add(site);
            
            // Get Block count for site
            for (Map.Entry<String, HashMap<String, String>> eblocks : myblocks.entrySet()){                
                String blockname = eblocks.getKey();
                HashMap<String, String> myzones = eblocks.getValue();
                DefaultMutableTreeNode block = new DefaultMutableTreeNode(blockname);
                
                site.add(block);                
                for (Map.Entry<String, String> ezones : myzones.entrySet()){
                    String zonename = ezones.getKey();
                    DefaultMutableTreeNode zone = new DefaultMutableTreeNode(zonename);
                    block.add(zone);
                }                
            }
        }
    }
    
      
    private void initLogger ()
    {
        Handler fileHandler  = null;
        try {
            String logdir = curdirpath+dirseprator+"log";
            new File(logdir).mkdir(); 
            fileHandler  = new FileHandler(logdir + dirseprator+ "dssat.log");
            datadir = curdirpath+dirseprator+"data";
            new File(datadir).mkdir(); 
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
            LOGGER.log(Level.ALL, "Initializing all the Components of the Application....");
            
        } catch (IOException e) {            
            e.printStackTrace();            
        }        
    }
    private void initAbsoluteDataPath ()
    {   Path currentRelativePath = Paths.get("");
        curdirpath = currentRelativePath.toAbsolutePath().toString();
        dirseprator = File.separator;
        csvdirpath = curdirpath + dirseprator + "properties" + dirseprator + "csv";
        System.out.println (csvdirpath);        
    }
    
    private void initFertInfo () 
    {
        Date date = new Date ();
        jDateChooser3.setDate(date);
        LinkedList<String> fertmethod = new LinkedList<String> ();
        LinkedList<String> fertmaterial = new LinkedList<> ();
        LinkedList<String> plantingMethodsList = new LinkedList<> ();
        
        
        CSVReader reader;
        String [] nextLine;
        
        LOGGER.log(Level.ALL, "Initializing fertilization information.");
        try {
            LOGGER.log(Level.ALL, "Initializing Fertilizer info from " + "/dssat/properties/csv/dssat_codelookup.csv");
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/dssat_codelookup.csv")));
            while ((nextLine = reader.readNext()) != null) {
                //cropName.add(nextLine[1]);
                String codematerial = nextLine[2];
                String codemethod = nextLine[1];
                String plantingMethodHeader = nextLine [0];
                
                String description = new String ();
                // Check if the code is FE/AP Then Build the descripttion till end.
                if (codematerial.contains("FE")) {                    
                    for (int i = 3; i < nextLine.length; i++)
                        description = description+nextLine[i];
                    fertmaterial.add(description);
                }else if (codemethod.contains("AP")) {
                    
                    for (int i = 2; i < nextLine.length; i++)
                        description = description+nextLine[i];
                    fertmethod.add(description);
                }
                
                if (plantingMethodHeader.equals("Planting Material/Method")) {
                    String plantingMethodName = nextLine[2];
                    if (plantingMethodName.isEmpty() == false) {                        
                        plantingMethodsList.add(nextLine[2]);                        
                    }   
                }
            }            
        } catch (IOException e) {
             e.printStackTrace();
        }
        
        Collections.sort(plantingMethodsList);
        Collections.sort(fertmethod);
        Collections.sort(fertmaterial);
        int itemCount = jComboBoxPlantingMethod.getItemCount();
        for(int i=0;i<itemCount;i++){
            jComboBoxPlantingMethod.removeItemAt(0);
        }
        for (int i = 0; i < plantingMethodsList.size(); i++) {            
            jComboBoxPlantingMethod.addItem(plantingMethodsList.get(i));
        }
        for (int i = 0; i < fertmaterial.size(); i++)
        {
            jComboBoxFertMaterial.addItem(fertmaterial.get(i));    
        }
        
        for (int i = 0; i < fertmethod.size(); i++)
        {
            jComboBoxFertMethod.addItem(fertmethod.get(i));    
        }
        

        LOGGER.log(Level.ALL, "Initialized Fertilization Information");
    }
    
    private void initFrameSize ()
    {
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //this.setBounds(0,0,screenSize.width, screenSize.height);
        LOGGER.log(Level.ALL, "Initializing frame size.");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        LOGGER.log(Level.ALL, "Initialized frame size");
    }
    
    
    private void initMyComponents(){
        //jMenuBar1 = new CommonMenuBar();
        //setJMenuBar(jMenuBar1);
        //jMenuBar1.setVisible(true);        
        //jSplitPane1.setContinuousLayout(true);
        
    }
    
    private void initTextFields () {
        //jTextFieldOrgName.setDocument(new LimitedPlainDocument(2));        
        //jTextFieldSiteIndex.setDocument(new LimitedPlainDocument(2));        
        Date date = new Date ();
        jDateChooser1.setDate(date);  
        LOGGER.log(Level.ALL, "Initialized planting Date ....");
    }
    
    private void initCropInfo () {

        CSVReader reader;
        String [] nextLine;
        ArrayList <String> cropName = new ArrayList <String> ();
        cropname_culfilename = new HashMap <String, String> () ;
        //String filepath = csvdirpath + dirseprator+"dssat_crop_lookup.csv";
        LOGGER.log(Level.ALL, "Initializing crop information.");
        try {
            reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/dssat/properties/csv/dssat_crop_lookup.csv")));            
            while ((nextLine = reader.readNext()) != null) {
                cropName.add(nextLine[1]);
                cropname_culfilename.put(nextLine[1], nextLine[3]);
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Sort the arrayList and Initialize the comboboxes.
        Collections.sort(cropName);
        for (int i = 0; i <cropName.size(); i++)
        {
            jComboBoxPrevCropList.addItem(cropName.get(i));
            jComboBoxCropList.addItem(cropName.get(i)); 
        }   

        LOGGER.log(Level.ALL, "Initialized crop information.");
        initCultivar ();
    }
    
    private void initCultivar ()
    {
        // TODO add your handling code here:
        // TODO add your handling code here:

        LOGGER.log(Level.ALL, "Initializing cultivar information.");
        String cropName = (String)jComboBoxCropList.getSelectedItem();
        HashMap <String, String> cultivarHashMap = null; 
        
        StringBuilder cultivadata = new StringBuilder ();
        
        cultivadata.append ("Crop Name,");        
        cultivadata.append(cropName);
        
        
        cultivadata.append (",CultivarFile,");  
        String culfileName = cropname_culfilename.get(cropName);        
        cultivadata.append(culfileName); 
        
        System.out.println ("Crop Name" + cropName);
        
        if (cultivafile == null)
            cultivafile = CultivaFileSystem.getInstance();
         
        cultivafile.UpdateCache(cultivadata.toString());         
        cultivarHashMap = cultivafile.ReadFromFile("VAR-NAME");
        
        ArrayList<String> cultivalist = new ArrayList<String> ();
        Set keys = cultivarHashMap.keySet();
        Iterator itr = keys.iterator();
 
        String key;
        String value;
        while(itr.hasNext())
        {
            key = (String)itr.next();
            value = cultivarHashMap.get(key);
            cultivalist.add(value);
        }
        Collections.sort(cultivalist);        
        int itemCount = jComboBoxCultivar.getItemCount();
        
        for(int i=0;i<itemCount;i++){
            jComboBoxCultivar.removeItemAt(0);
        }
        
        for (int i = 0; i < cultivalist.size(); i++) {
            key = cultivalist.get(i);
            String cultivarname = key.trim();
            System.out.printf (cultivarname + "->");
            if (cultivarname.length()>0)
                jComboBoxCultivar.addItem(cultivarname);
        }
        LOGGER.log(Level.ALL, "Initialized cultivar information.");
    }
    
    private void initSoilInfo () {
        
        //soil_db = new DBConnect (ServerDetails.SERVER_NUM_RW, ServerDetails.soil_dbname);
        LOGGER.log(Level.ALL, "Initializing soil information....");
        CSVFileHandler csvfile = new CSVFileHandler ();
        
        // From the global db initialize the county comboBox
        ArrayList <String> soilList = null;
        ArrayList <String> countyList = null;
        countyList = csvfile.getCountyList_SoilDB();
        Collections.sort(countyList);
        
        /*for (int i = 0; i < countyList.size(); i++) {            
            String countyName = countyList.get(i);
            countyName = countyName.substring(0, 1).toUpperCase() + countyName.substring(1).toLowerCase(); 
          soilInfoCountyCombobox.addItem(countyName);     
        }*/
        // String countyName = (String) soilInfoCountyCombobox.getSelectedItem();
        // soilList = csvfile.getCountyBasedSoilList_SoilDB(countyList.get(0));
        String countName = (String)countyNameGlobal.getSelectedItem();        
        soilList = csvfile.getCountyBasedSoilList_SoilDB(countName);
        

        for (int i = 0; i < soilList.size(); i++) {
          soilSeriesCombobox.addItem(soilList.get(i));            
        }
        LOGGER.log(Level.ALL, "Initialized soil information....");
              
    }
    
    
    private void initWeatherHistoric  ()
    {
        //weather_historic_daily = new DBConnect (ServerDetails.SERVER_NUM_RONLY, ServerDetails.weather_historic_daily_dbname);
    }
    
    private void initGlobalDBInfo  ()
    {        
        LOGGER.log(Level.ALL, "Initializing Global Data information....");
        CSVFileHandler csvfile = new CSVFileHandler ();
        // From the global db initialize the county comboBox
        ArrayList <String> countyList = null;
        countyList = csvfile.getCountyList_GlobalDB();
        if (countyList == null) {
            LOGGER.log(Level.ALL, "County List is not initialized. Exiting the application!");
            System.exit(0);
            return;
        }
        Collections.sort(countyList);
        for (int i = 0; i < countyList.size(); i++) {
            String countyName = countyList.get(i);
            countyName = countyName.substring(0, 1).toUpperCase() + countyName.substring(1).toLowerCase(); 
            
            countyNameGlobal.addItem(countyName);            
        }
        
        LOGGER.log(Level.ALL, "Added the county names.");
        
        // By default get the first item in the list and get the Co-Ordinates of the at item and populate the 
        // weather stations based on the first item in the combobox 

        Location countypos = new Location ();
        csvfile.getCountyLocation_GlobalDB (countyList.get(0), countypos); 
                 
        ArrayList<String> weatherstations = null;
        weatherstations = csvfile.getWeatherStations_GlobalDB(countypos);
        Collections.sort(weatherstations);
        
        int itemCount = weatherStComboBox.getItemCount();
        for(int i=0;i<itemCount;i++){
            weatherStComboBox.removeItemAt(0);
        }
        
        System.out.println("Total Number of Fawn Weather Stations = " + weatherstations.size() + " for " + countyList.get(0));
        
        //weatherStComboBox.
        for (int i = 0; i < weatherstations.size(); i++) {
            
            String wtstationname = weatherstations.get(i);
            wtstationname = wtstationname.substring(0, 1).toUpperCase() + wtstationname.substring(1).toLowerCase(); 
            
            System.out.println (wtstationname);
            weatherStComboBox.addItem(wtstationname);            
        }
        
        LOGGER.log(Level.ALL, "Initialized Fawn Weather stations.");
        LOGGER.log(Level.ALL, "Initialized Global Data information....");
 
    }
    
    
    //********Class to limit the number of characters a user can enter into a field.*********
    public class LimitedPlainDocument extends javax.swing.text.PlainDocument {

         private int maxLen = -1;

         /** Creates a new instance of LimitedPlainDocument */
         public LimitedPlainDocument() {
         }

         public LimitedPlainDocument(int maxLen) {
              this.maxLen = maxLen;
         }

         public void insertString(int param, String str, javax.swing.text.AttributeSet attributeSet) throws javax.swing.text.BadLocationException {
              if (str != null && maxLen > 0 && this.getLength() + str.length() > maxLen) {
                   java.awt.Toolkit.getDefaultToolkit().beep();
                   return;
              }

              super.insertString(param, str, attributeSet);
         }

    }
//    public String getOrgName () {
//        return jTextFieldOrgName.getText();
//    }
//    public String getSiteIndex () {
//        return jTextFieldSiteIndex.getText();
//        
//    }
    
    public String getCountyName () {
        return (String)countyNameGlobal.getSelectedItem ();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jMainPanel = new javax.swing.JPanel();
        FieldPanel = new javax.swing.JPanel();
        GeneralInformation = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jSiteNameText = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        countyNameGlobal = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jBlockNameText = new javax.swing.JTextField();
        jZoneNameText = new javax.swing.JTextField();
        WeatherInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        weatherStComboBox = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        SoilInfo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        soilSeriesCombobox = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        CropInfo = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxPrevCropList = new javax.swing.JComboBox();
        jComboBoxCultivar = new javax.swing.JComboBox();
        jComboBoxCropList = new javax.swing.JComboBox();
        BedSystemInfo = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxPlantingMethod = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jNextButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        IrrigationFertilizerPanel = new javax.swing.JPanel();
        pIrrigationPanel = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        pFertilizerPanel = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jTextRatePerApplication = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jComboBoxFertMaterial = new javax.swing.JComboBox();
        jComboBoxFertMethod = new javax.swing.JComboBox();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel63 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jFertilizerInfoTable = new javax.swing.JTable();
        bAdd = new javax.swing.JButton();
        bUpdate = new javax.swing.JButton();
        bDelete = new javax.swing.JButton();
        warningMessaage = new javax.swing.JLabel();
        pButtonPanel = new javax.swing.JPanel();
        bFinishButton = new javax.swing.JButton();
        bBackButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jProjectExplorer = new javax.swing.JTree();
        jMenuBarFields = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jOpenMenuItem = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuFields = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuIrrigationFertilizer = new javax.swing.JMenu();
        jMenuItemGoToIrrigationFertilizer = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItemOptions = new javax.swing.JMenuItem();
        jMenuRun = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSplitPane1MouseDragged(evt);
            }
        });

        jMainPanel.setAutoscrolls(true);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel27.setText("Site Name:");

        jSiteNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSiteNameTextActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel25.setText("Planting Date :");

        jDateChooser1.setDateFormatString("MMM, dd, yyyy");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel23.setText("Your Location :");

        countyNameGlobal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countyNameGlobalActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel28.setText("Block Name:");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel30.setText("Zone: ");

        javax.swing.GroupLayout GeneralInformationLayout = new javax.swing.GroupLayout(GeneralInformation);
        GeneralInformation.setLayout(GeneralInformationLayout);
        GeneralInformationLayout.setHorizontalGroup(
            GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GeneralInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSiteNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBlockNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jZoneNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72)
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(countyNameGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        GeneralInformationLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel23, jLabel25});

        GeneralInformationLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel27, jLabel28, jLabel30});

        GeneralInformationLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jBlockNameText, jSiteNameText, jZoneNameText});

        GeneralInformationLayout.setVerticalGroup(
            GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GeneralInformationLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(GeneralInformationLayout.createSequentialGroup()
                        .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jSiteNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jBlockNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GeneralInformationLayout.createSequentialGroup()
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(countyNameGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(GeneralInformationLayout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel23))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GeneralInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jZoneNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        GeneralInformationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel27, jLabel28, jLabel30});

        GeneralInformationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBlockNameText, jSiteNameText, jZoneNameText});

        WeatherInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Weather Station", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 51, 204))); // NOI18N
        WeatherInfo.setPreferredSize(new java.awt.Dimension(620, 193));

        jLabel1.setText("Nearest FAWN Weather Station");
        jLabel1.setToolTipText("");

        weatherStComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weatherStComboBoxActionPerformed(evt);
            }
        });

        jButton3.setText("View Weather File");

        javax.swing.GroupLayout WeatherInfoLayout = new javax.swing.GroupLayout(WeatherInfo);
        WeatherInfo.setLayout(WeatherInfoLayout);
        WeatherInfoLayout.setHorizontalGroup(
            WeatherInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeatherInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(136, 136, 136)
                .addGroup(WeatherInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(weatherStComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(193, Short.MAX_VALUE))
        );
        WeatherInfoLayout.setVerticalGroup(
            WeatherInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeatherInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WeatherInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeatherInfoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3))
                    .addComponent(weatherStComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        SoilInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Soil Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 51, 204))); // NOI18N
        SoilInfo.setPreferredSize(new java.awt.Dimension(620, 193));

        jLabel4.setText("Soil Series Name");

        jButton4.setText("View Soil File");

        javax.swing.GroupLayout SoilInfoLayout = new javax.swing.GroupLayout(SoilInfo);
        SoilInfo.setLayout(SoilInfoLayout);
        SoilInfoLayout.setHorizontalGroup(
            SoilInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SoilInfoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addGap(32, 32, 32)
                .addGroup(SoilInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(soilSeriesCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(389, Short.MAX_VALUE))
        );
        SoilInfoLayout.setVerticalGroup(
            SoilInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SoilInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SoilInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soilSeriesCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        CropInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Crop", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 51, 204))); // NOI18N
        CropInfo.setPreferredSize(new java.awt.Dimension(620, 193));

        jLabel5.setText("Previous Crop From List");

        jLabel6.setText("Crop Name");

        jLabel7.setText("Cultivar");

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

        javax.swing.GroupLayout CropInfoLayout = new javax.swing.GroupLayout(CropInfo);
        CropInfo.setLayout(CropInfoLayout);
        CropInfoLayout.setHorizontalGroup(
            CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CropInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxCropList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxPrevCropList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxCultivar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(418, Short.MAX_VALUE))
        );

        CropInfoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, jLabel6, jLabel7});

        CropInfoLayout.setVerticalGroup(
            CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CropInfoLayout.createSequentialGroup()
                .addGroup(CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxPrevCropList, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCropList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CropInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCultivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        CropInfoLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxCropList, jComboBoxCultivar, jComboBoxPrevCropList, jLabel5, jLabel6, jLabel7});

        BedSystemInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bed System", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 51, 204))); // NOI18N
        BedSystemInfo.setPreferredSize(new java.awt.Dimension(620, 193));

        jLabel11.setText("Bed Width");

        jTextField2.setColumns(10);

        jLabel24.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel24.setText("Inches");

        jLabel13.setText("Plastic Mulch Color");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Black", "Silver", "White", " " }));

        jLabel12.setText("Bed Height");

        jTextField6.setColumns(10);

        jLabel29.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel29.setText("Inches");

        jLabel14.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Planting");

        jLabel15.setText("Planting Method");

        jComboBoxPlantingMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel17.setText("Planting Depth");

        jTextField4.setColumns(5);

        jLabel20.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel20.setText("Inches");

        jLabel16.setText("Planting Spacing in Row");

        jTextField3.setColumns(5);

        jLabel19.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel19.setText("Feet");

        jLabel18.setText("Row Spacing");

        jTextField5.setColumns(5);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel21.setText("Inches");

        javax.swing.GroupLayout BedSystemInfoLayout = new javax.swing.GroupLayout(BedSystemInfo);
        BedSystemInfo.setLayout(BedSystemInfoLayout);
        BedSystemInfoLayout.setHorizontalGroup(
            BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BedSystemInfoLayout.createSequentialGroup()
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addGap(68, 68, 68)
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jComboBoxPlantingMethod, 0, 82, Short.MAX_VALUE)
                                .addGap(12, 12, 12)))
                        .addGap(56, 56, 56)
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(27, 27, 27)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))
                            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20)))
                        .addGap(100, 100, 100))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BedSystemInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(236, 236, 236))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BedSystemInfoLayout.createSequentialGroup()
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(54, 54, 54)
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jTextField6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel29))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, BedSystemInfoLayout.createSequentialGroup()
                                .addComponent(jTextField2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)))
                        .addGap(35, 35, 35)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(83, 83, 83))))
        );
        BedSystemInfoLayout.setVerticalGroup(
            BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BedSystemInfoLayout.createSequentialGroup()
                .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BedSystemInfoLayout.createSequentialGroup()
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))))
                .addGap(24, 24, 24)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17)
                    .addComponent(jComboBoxPlantingMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BedSystemInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        BedSystemInfoLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxPlantingMethod, jLabel15, jLabel16, jLabel17, jLabel18, jLabel19, jLabel20, jLabel21, jTextField3, jTextField4, jTextField5});

        jNextButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dssat/images/Next-icon.png"))); // NOI18N
        jNextButton.setText("Next");
        jNextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jNextButtonMouseClicked(evt);
            }
        });
        jNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNextButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Edit Form");

        jButton2.setText("Setup Sites");

        javax.swing.GroupLayout FieldPanelLayout = new javax.swing.GroupLayout(FieldPanel);
        FieldPanel.setLayout(FieldPanelLayout);
        FieldPanelLayout.setHorizontalGroup(
            FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GeneralInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(73, 73, 73)
                .addComponent(jNextButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(FieldPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CropInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(WeatherInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BedSystemInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SoilInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        FieldPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BedSystemInfo, CropInfo, SoilInfo, WeatherInfo});

        FieldPanelLayout.setVerticalGroup(
            FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GeneralInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FieldPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FieldPanelLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(jNextButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(WeatherInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SoilInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CropInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addComponent(BedSystemInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        FieldPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BedSystemInfo, CropInfo});

        pIrrigationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Irrigation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 0, 255))); // NOI18N
        pIrrigationPanel.setName("Dripper Information"); // NOI18N

        jLabel49.setText("Dripper Rate");

        jLabel50.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel50.setText("gal/Hour");

        jLabel51.setText("Dripper Distance");

        jLabel52.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel52.setText("Feet");

        jLabel53.setText("Dripper Emitter Offset  From Bed Center Line");

        jLabel54.setText("Dripper Emitter depth from Surface");

        jLabel55.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel55.setText("Inches");

        jLabel56.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel56.setText("Inches");

        jLabel57.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel57.setText("Inches");

        jLabel58.setText("Irrigation Depth");

        javax.swing.GroupLayout pIrrigationPanelLayout = new javax.swing.GroupLayout(pIrrigationPanel);
        pIrrigationPanel.setLayout(pIrrigationPanelLayout);
        pIrrigationPanelLayout.setHorizontalGroup(
            pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIrrigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        pIrrigationPanelLayout.setVerticalGroup(
            pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIrrigationPanelLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(18, 18, 18)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addGap(18, 18, 18)
                .addGroup(pIrrigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addContainerGap(193, Short.MAX_VALUE))
        );

        pFertilizerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fertilizer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel59.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel59.setText("lb/Acre");

        jTextRatePerApplication.setColumns(9);

        jLabel60.setText("Rate Per Application");

        jLabel61.setText("Fertilization Material");

        jLabel62.setText("Fertilization Method");

        jLabel63.setText("Fertilization Date");

        jFertilizerInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Fertilization Date", "Fertilization Material", "Fertilization Method", "Rate Per Application"
            }
        ));
        jFertilizerInfoTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jFertilizerInfoTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jFertilizerInfoTable);

        bAdd.setText("Add");
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });

        bUpdate.setText("Edit");
        bUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUpdateActionPerformed(evt);
            }
        });

        bDelete.setText("Delete");
        bDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDeleteActionPerformed(evt);
            }
        });

        warningMessaage.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout pFertilizerPanelLayout = new javax.swing.GroupLayout(pFertilizerPanel);
        pFertilizerPanel.setLayout(pFertilizerPanelLayout);
        pFertilizerPanelLayout.setHorizontalGroup(
            pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                        .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel63)
                                    .addComponent(jLabel60)))
                            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(bAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(bUpdate)))
                        .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                                        .addComponent(jTextRatePerApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(55, 55, 55))
                                    .addComponent(jComboBoxFertMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxFertMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(28, 28, 28))
                            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(bDelete)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pFertilizerPanelLayout.setVerticalGroup(
            pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addGap(18, 18, 18)
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pFertilizerPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jComboBoxFertMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFertMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(jTextRatePerApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59))
                .addGap(22, 22, 22)
                .addGroup(pFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAdd)
                    .addComponent(bUpdate)
                    .addComponent(bDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(343, 343, 343))
        );

        bFinishButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bFinishButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dssat/images/Ok-icon-4.png"))); // NOI18N
        bFinishButton.setText("Finish");
        bFinishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFinishButtonActionPerformed(evt);
            }
        });

        bBackButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dssat/images/rsz_back-icon.png"))); // NOI18N
        bBackButton.setText("Back");
        bBackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bBackButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pButtonPanelLayout = new javax.swing.GroupLayout(pButtonPanel);
        pButtonPanel.setLayout(pButtonPanelLayout);
        pButtonPanelLayout.setHorizontalGroup(
            pButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bBackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bFinishButton)
                .addContainerGap())
        );
        pButtonPanelLayout.setVerticalGroup(
            pButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pButtonPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBackButton)
                    .addComponent(bFinishButton))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout IrrigationFertilizerPanelLayout = new javax.swing.GroupLayout(IrrigationFertilizerPanel);
        IrrigationFertilizerPanel.setLayout(IrrigationFertilizerPanelLayout);
        IrrigationFertilizerPanelLayout.setHorizontalGroup(
            IrrigationFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IrrigationFertilizerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IrrigationFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IrrigationFertilizerPanelLayout.createSequentialGroup()
                        .addComponent(pIrrigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pFertilizerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IrrigationFertilizerPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap())
        );
        IrrigationFertilizerPanelLayout.setVerticalGroup(
            IrrigationFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IrrigationFertilizerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IrrigationFertilizerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pIrrigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pFertilizerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        IrrigationFertilizerPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pFertilizerPanel, pIrrigationPanel});

        javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(jMainPanel);
        jMainPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jMainPanelLayout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(IrrigationFertilizerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
        );
        jMainPanelLayout.setVerticalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FieldPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jMainPanelLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(IrrigationFertilizerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jSplitPane1.setRightComponent(jMainPanel);

        jProjectExplorer.setBackground(new java.awt.Color(240, 240, 240));
        jProjectExplorer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jProjectExplorer.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jProjectExplorer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jProjectExplorerMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jProjectExplorer);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jMenuFile.setText("File");
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("New Site");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItem2);

        jOpenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jOpenMenuItem.setText("Open Site");
        jOpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenMenuItemActionPerformed(evt);
            }
        });
        jMenuFile.add(jOpenMenuItem);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Save Site");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItem4);

        jMenuItemSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSaveAs.setText("Save Site As");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSaveAs);
        jMenuFile.add(jSeparator1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Close Site");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItem5);
        jMenuFile.add(jSeparator2);

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBarFields.add(jMenuFile);

        jMenuFields.setText("Fields");
        jMenuFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFieldsActionPerformed(evt);
            }
        });

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem6.setText("Go to Fields");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenuFields.add(jMenuItem6);

        jMenuBarFields.add(jMenuFields);

        jMenuIrrigationFertilizer.setText("Irrigation/Fertilizer");
        jMenuIrrigationFertilizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuIrrigationFertilizerActionPerformed(evt);
            }
        });

        jMenuItemGoToIrrigationFertilizer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemGoToIrrigationFertilizer.setText("Go to Irrigation/Fertilizer");
        jMenuItemGoToIrrigationFertilizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGoToIrrigationFertilizerActionPerformed(evt);
            }
        });
        jMenuIrrigationFertilizer.add(jMenuItemGoToIrrigationFertilizer);

        jMenuBarFields.add(jMenuIrrigationFertilizer);

        jMenuTools.setText("Tools");

        jMenuItemOptions.setText("Options");
        jMenuTools.add(jMenuItemOptions);

        jMenuBarFields.add(jMenuTools);

        jMenuRun.setText("Run");

        jMenuItem1.setText("View Weather File");
        jMenuRun.add(jMenuItem1);

        jMenuBarFields.add(jMenuRun);

        setJMenuBar(jMenuBarFields);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1322, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void countyNameGlobalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countyNameGlobalActionPerformed
        // TODO add your handling code here:

        LOGGER.log(Level.ALL, "Method Name:countyNameGlobalActionPerformed" + "Initializing information based on county Name.");

        String countyName = (String) countyNameGlobal.getSelectedItem();

        // By default get the first item in the list and get the Co-Ordinates of the at item and populate the
        // weather stations based on the first item in the combobox

        int itemCount = weatherStComboBox.getItemCount();
        
         
        
        for(int i=0;i<itemCount;i++){
            weatherStComboBox.removeItemAt(0);
        }

        CSVFileHandler csvfilehandler = new CSVFileHandler ();
        Location countypos = new Location ();
        csvfilehandler.getCountyLocation_GlobalDB (countyName, countypos);

        ArrayList<String> weatherstations = null;
        weatherstations = csvfilehandler.getWeatherStations_GlobalDB(countypos);
        Collections.sort(weatherstations);
        
        System.out.println("Total Number of Fawn Weather Stations = " + weatherstations.size() + " for " + countyName);
        
        //weatherStComboBox.
        for (int i = 0; i < weatherstations.size(); i++) {
            String wtstationname = weatherstations.get(i);
            wtstationname = wtstationname.substring(0, 1).toUpperCase() + wtstationname.substring(1).toLowerCase(); 
            
            System.out.println (wtstationname);
            weatherStComboBox.addItem(wtstationname);
        }
        
        ArrayList <String> soilList = null;
        //String countyName = (String) soilInfoCountyCombobox.getSelectedItem();
        //soilList = soil_db.getCountyBasedSoilList_SoilDB(countyName);
        //CSVFileHandler csvfilehandler = new CSVFileHandler();
        soilList = csvfilehandler.getCountyBasedSoilList_SoilDB(countyName);

        int soilitemCount = soilSeriesCombobox.getItemCount();
        for(int i=0;i<soilitemCount;i++){
            soilSeriesCombobox.removeItemAt(0);
        }

        for (int i = 0; i < soilList.size(); i++) {
            //System.out.print(soilList.get(i));
            soilSeriesCombobox.addItem(soilList.get(i));
        }
        
        
        
    }//GEN-LAST:event_countyNameGlobalActionPerformed

    private void weatherStComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weatherStComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_weatherStComboBoxActionPerformed

    private void jNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNextButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jNextButtonActionPerformed

    private void jNextButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jNextButtonMouseClicked
        UpdateFile ();
        FieldPanel.setVisible(false);
        IrrigationFertilizerPanel.setVisible(true);      
   
    }//GEN-LAST:event_jNextButtonMouseClicked

    private void jComboBoxCropListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCropListActionPerformed
        
        
    }//GEN-LAST:event_jComboBoxCropListActionPerformed

    private void jComboBoxCropListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxCropListItemStateChanged
        // TODO add your handling code here:
        // TODO add your handling code here:
        
        if (evt.getStateChange()==ItemEvent.SELECTED) {
            initCultivar ();
           /* String cropName = (String)jComboBoxCropList.getSelectedItem();
            HashMap <String, String> cultivarHashMap = null; 

            StringBuilder cultivadata = new StringBuilder ();

            cultivadata.append ("Crop Name,");        
            cultivadata.append(cropName);


            cultivadata.append (",CultivarFile,");  
            String culfileName = cropname_culfilename.get(cropName);        
            cultivadata.append(culfileName); 

            System.out.println ("Crop Name" + cropName);

            if (cultivafile == null)
                cultivafile = CultivaFileSystem.getInstance();

            cultivafile.UpdateCache(cultivadata.toString());         
            cultivarHashMap = cultivafile.ReadFromFile("VAR-NAME");

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
            } */
            
        }  
    }//GEN-LAST:event_jComboBoxCropListItemStateChanged

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void bFinishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFinishButtonActionPerformed
        // TODO add your handling code here:
        LOGGER.log(Level.ALL, "Creating a Weather file after Finish Button is pressed ...");
        String keyvalue = "dssat, dssat1";
        if (wthfile == null)
        wthfile = WeatherFileSystem.getInstance();
        wthfile.UpdateCache(keyvalue);
        wthfile.WriteToFile();
        LOGGER.log(Level.ALL, "Created a Weather file after Finish Button is pressed ...");
    }//GEN-LAST:event_bFinishButtonActionPerformed

    private void bBackButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBackButtonMouseClicked
        // TODO add your handling code here:

        IrrigationFertilizerPanel.setVisible(false);
        FieldPanel.setVisible(true);

    }//GEN-LAST:event_bBackButtonMouseClicked

    private void jFertilizerInfoTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFertilizerInfoTableMouseClicked
        // TODO add your handling code here:
        warningMessaage.setText("");
        DefaultTableModel model = (DefaultTableModel)jFertilizerInfoTable.getModel();
        Date date = new Date (model.getValueAt(jFertilizerInfoTable.getSelectedRow(), 0).toString());
        jDateChooser1.setDate(date);
        jComboBoxFertMaterial.setSelectedItem(model.getValueAt(jFertilizerInfoTable.getSelectedRow(), 1).toString());
        jComboBoxFertMethod.setSelectedItem(model.getValueAt(jFertilizerInfoTable.getSelectedRow(), 2).toString());
        jTextRatePerApplication.setText(model.getValueAt(jFertilizerInfoTable.getSelectedRow(), 3).toString());

    }//GEN-LAST:event_jFertilizerInfoTableMouseClicked

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed
        // TODO add your handling code here:
        LOGGER.log(Level.ALL, "Adding the new line in the Fertilization table...");
        warningMessaage.setText("");
        DefaultTableModel model = (DefaultTableModel)jFertilizerInfoTable.getModel();
        if (jTextRatePerApplication.getText().trim().equals("")){
            warningMessaage.setText("Rate Per Application should not be left blank.");
            return;
        } else if (jDateChooser1.getDate().toString().trim().equals("")){
            warningMessaage.setText("Fertilization Date should not be left blank.");
            return;
        }
        model.insertRow(0,new Object [] {jDateChooser1.getDate().toString(), jComboBoxFertMaterial.getSelectedItem().toString(), jComboBoxFertMethod.getSelectedItem().toString(), jTextRatePerApplication.getText()});
        LOGGER.log(Level.ALL, "Added the new line in the Fertilization table...");
    }//GEN-LAST:event_bAddActionPerformed

    private void bUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUpdateActionPerformed
        // TODO add your handling code here:
        LOGGER.log(Level.ALL, "Updating the information from Fertilization table...");
        warningMessaage.setText("");
        DefaultTableModel model = (DefaultTableModel)jFertilizerInfoTable.getModel();
        if (jFertilizerInfoTable.getSelectedRow()==-1) {

            if (jFertilizerInfoTable.getRowCount()==0)
            {
                warningMessaage.setText("Fertilization Table is empty.");
            } else  {

                warningMessaage.setText("Row is not selected from Fertilization Table.");
            }
        } else {

            if (jTextRatePerApplication.getText().trim().equals("")){
                warningMessaage.setText("Rate Per Application should not be left blank.");
                return;
            } else if (jDateChooser1.getDate().toString().trim().equals("")){
                warningMessaage.setText("Fertilization Date should not be left blank.");
                return;
            }

            model.setValueAt(jDateChooser1.getDate().toString(), jFertilizerInfoTable.getSelectedRow(), 0);
            model.setValueAt(jComboBoxFertMaterial.getSelectedItem().toString(), jFertilizerInfoTable.getSelectedRow(), 1);
            model.setValueAt(jComboBoxFertMethod.getSelectedItem().toString(), jFertilizerInfoTable.getSelectedRow(), 2);
            model.setValueAt(jTextRatePerApplication.getText(), jFertilizerInfoTable.getSelectedRow(), 3);
        }
        
        LOGGER.log(Level.ALL, "Updated the information from Fertilization table...");
    }//GEN-LAST:event_bUpdateActionPerformed

    private void bDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteActionPerformed
        // TODO add your handling code here:
        
        LOGGER.log(Level.ALL, "Removing the information from Fertilization table...");
        
        warningMessaage.setText("");
        DefaultTableModel model = (DefaultTableModel)jFertilizerInfoTable.getModel();
        if (jFertilizerInfoTable.getSelectedRow()==-1) {

            if (jFertilizerInfoTable.getRowCount()==0)
            {
                warningMessaage.setText("Fertilization Table is empty.");
            } else  {

                warningMessaage.setText("Row is not selected from Fertilization Table.");
            }
        } else {
            model.removeRow(jFertilizerInfoTable.getSelectedRow());
        }
        
        LOGGER.log(Level.ALL, "Removed the information from Fertilization table...");
    }//GEN-LAST:event_bDeleteActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jSiteNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSiteNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSiteNameTextActionPerformed

    private void jOpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenMenuItemActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser ();
        fc.setCurrentDirectory(new File("."));
        if (evt.getSource() == jOpenMenuItem) {

            int returnVal = fc.showOpenDialog(jMenuFile);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                //log.append("Opening: " + file.getName() + "." + newline);
            } 
        }else if (evt.getSource() == jMenuItemSaveAs) {
                //log.append("Open command cancelled by user." + newline);
            int returnVal = fc.showSaveDialog(jMenuFile);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                //log.append("Saving: " + file.getName() + "." + newline);
            } else {
                //log.append("Save command cancelled by user." + newline);
            }
        }
    }//GEN-LAST:event_jOpenMenuItemActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser ();
        fc.setCurrentDirectory(new File("."));
        if (evt.getSource() == jMenuItemSaveAs) {
                //log.append("Open command cancelled by user." + newline);
            int returnVal = fc.showSaveDialog(jMenuFile);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                //log.append("Saving: " + file.getName() + "." + newline);
            } else {
                //log.append("Save command cancelled by user." + newline);
            }
        }
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        // TODO add your handling code here:
        
        
        //FileOperations.WriteToSiteFile (this);
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuIrrigationFertilizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIrrigationFertilizerActionPerformed
        // TODO add your handling code here:
       
        
    }//GEN-LAST:event_jMenuIrrigationFertilizerActionPerformed

    private void jMenuFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFieldsActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenuFieldsActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
         IrrigationFertilizerPanel.setVisible(false);
        FieldPanel.setVisible(true); 
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItemGoToIrrigationFertilizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGoToIrrigationFertilizerActionPerformed
        // TODO add your handling code here:
        UpdateFile ();
        FieldPanel.setVisible(false);
        IrrigationFertilizerPanel.setVisible(true);   
    }//GEN-LAST:event_jMenuItemGoToIrrigationFertilizerActionPerformed

    private void jProjectExplorerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jProjectExplorerMousePressed
        // TODO add your handling code here:
        if ( SwingUtilities.isRightMouseButton ( evt ) )
        {
           displayPopupOptions (evt);
        } 
        else if (SwingUtilities.isLeftMouseButton(evt))
        {
            saveSiteFile (evt);
            openSiteFile (evt);
            
        }
    }//GEN-LAST:event_jProjectExplorerMousePressed
    
    
    private void openSiteFile (java.awt.event.MouseEvent evt)
    {
        
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) jProjectExplorer.getLastSelectedPathComponent();
         if (node == null) 
            return;   
        
        String filename = node.getUserObject().toString();        
        // Open the zone1 file 
        String file = DSSATMain.curdirpath +dirseprator+"sites\\site1\\Alachua\\Block1\\" + filename + ".data";
        String name;
        String value;
        // Open the zone1 file 
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
//            for(String line = br.readLine(); line != null; line = br.readLine()) {
//                // process the line.
//                StringTokenizer tokenizer = StringTokenizer(line,":",false);
//                tokenizer.
//                System.out.println(line);
//            }
        } catch (IOException e){
            
        }
    }
    
    // This method will be useful while saving the data to the file whil closing the application.
    private String getTreeText(TreeModel model, Object object, String indent) {
        String myRow = indent + object + "\n";
        for (int i = 0; i < model.getChildCount(object); i++) {
            myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
        }
        return myRow;
    }
    
    private void saveSiteFile (java.awt.event.MouseEvent evt)
    {
        // Open the zone1 file 
        
        
        
        
    }
    
    private void displayPopupOptions (java.awt.event.MouseEvent evt)
    {
        TreePath path = jProjectExplorer.getPathForLocation (evt.getX (), evt.getY ());
        Rectangle pathBounds = jProjectExplorer.getUI ().getPathBounds ( jProjectExplorer, path );

        int childcount = jProjectExplorer.getModel().getChildCount(jProjectExplorer.getModel().getRoot());
        LOGGER.log(Level.ALL, path.toString());
        LOGGER.log(Level.ALL, "Total Numbe of Child nodes = " , childcount);
        if ( pathBounds != null && pathBounds.contains ( evt.getX (), evt.getY () ) )
        {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)jProjectExplorer.getModel().getRoot();
            // Checked if the selection is made on the root node then
            // Then we give the options to add an new Block or Save the block
            //root.getParent()

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent ();

            // We are right clicking on the root node
            if (path.getLastPathComponent() == root) {
                //Integer (childcount)
                JPopupMenu menu = new JPopupMenu ();
                menu.add ( new JMenuItem ("New Site") );
                menu.show ( jProjectExplorer, pathBounds.x, pathBounds.y + pathBounds.height );                        
            } 
            // Check if this is a block node
            // We need to check if the current one is the child of the root node then this is the 
            // Block node. 

            // We are right clicking on any of the site node
            else if (node.getParent() == root)
            {
                JPopupMenu menu = new JPopupMenu ();
                menu.add ( new JMenuItem ("New Block") );
                menu.add ( new JMenuItem ( "Save Site" ) );
                menu.show ( jProjectExplorer, pathBounds.x, pathBounds.y + pathBounds.height ); 

            }

            // We are right clicking on any of the block node
            else if (node.getParent().getParent() == root)
            {
                JPopupMenu menu = new JPopupMenu ();
                menu.add ( new JMenuItem ("New Zone") );
                menu.add ( new JMenuItem ( "Save Block" ) );
                menu.show ( jProjectExplorer, pathBounds.x, pathBounds.y + pathBounds.height ); 

            }

            // We are right clicking on any of the zone node
            else if (node.getParent().getParent().getParent() == root)
            {
                JPopupMenu menu = new JPopupMenu ();
                menu.add ( new JMenuItem ("Open"));
                menu.add ( new JMenuItem ("Copy"));
                menu.add ( new JMenuItem ("Paste"));
                menu.add ( new JMenuItem ("Save"));
                menu.show ( jProjectExplorer, pathBounds.x, pathBounds.y + pathBounds.height ); 

            }

        }
    }
    private void jSplitPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSplitPane1MouseDragged
        // TODO add your handling code here:
        
//         BasicSplitPaneDivider l_divider = jSplitPane1.getDivider();
//        @Override
//        public void mouseDragged(MouseEvent evt) {
//          Dimension l_pane_size = getSize();
//          if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
//            int l_new_loc = getDividerLocation() + e.getX();
//            if (l_new_loc >= 0 && l_new_loc <= l_pane_size.width) {
//              setDividerLocation(l_new_loc);
//            }
//          } else {
//            int l_new_loc = getDividerLocation() + e.getY();
//            if (l_new_loc >= 0 && l_new_loc <= l_pane_size.height) {
//              setDividerLocation(l_new_loc);
//            }
//          }
//        }
    }//GEN-LAST:event_jSplitPane1MouseDragged

    private void UpdateFile () {
        
        LOGGER.log(Level.ALL, "Updating the cache with the latest information...");
        
        StringBuilder   weatherdata = new StringBuilder ("");
        if (wthfile == null)
            wthfile = WeatherFileSystem.getInstance();
        
        weatherdata.append ("Site,");
        weatherdata.append(jSiteNameText.getText() + ",");
        
        Date date = jDateChooser1.getDate();        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        
        Integer day =  new Integer(calendar.get(Calendar.DATE));
        String daystr = day.toString();
        
        Integer year = new Integer (calendar.get(Calendar.YEAR));
        String yearstr = year.toString();
        
        Integer month = new Integer (calendar.get(Calendar.MONTH));
        String monthstr = month.toString();
                     
        
        //System.out.println ("Hello This is rohit - " + day + "//" + month + "//"+  year);
        CSVFileHandler filehandler = new CSVFileHandler ();
        weatherdata.append ("PlantingMonth,");
        //weatherdata.append ((String) jPlantingMonthComboBox.getSelectedItem() + ",");
        weatherdata.append (monthstr + ",");
        
        weatherdata.append ("PlantingDay,");
        //weatherdata.append ((String) jPlantingDayComboBox.getSelectedItem() + ",");
        weatherdata.append (daystr + ",");
        
        weatherdata.append ("PlantingYear,");
        //weatherdata.append ((String) jPlantingYearComboBox.getSelectedItem() + ",");
        weatherdata.append (yearstr + ",");
        
        weatherdata.append ("FawnWeatherStation,");
        String watherstation = (String) weatherStComboBox.getSelectedItem();
        weatherdata.append (watherstation + ",");  
        
        weatherdata.append ("StationLocationId,");        
        weatherdata.append(filehandler.getWeatherStationId_GlobalDB (watherstation));
        
        System.out.println("Weather Data -" + weatherdata);
        wthfile.UpdateCache(weatherdata.toString());         
        
        LOGGER.log(Level.ALL, "Updated the cache with the latest information...");
    }
    
    /*private void UpdateFile () {
        
        StringBuilder   weatherdata = new StringBuilder ("");
        if (wthfile == null)
            wthfile = WeatherFileSystem.getInstance();
        
        weatherdata.append ("Organization,");
        weatherdata.append(jTextFieldOrgName.getText() + ",");
        
        weatherdata.append ("SiteIndex,");
        weatherdata.append (jTextFieldSiteIndex.getText() + ",");
        
        Date date = jDateChooser1.getDate();        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        
        Integer day =  new Integer(calendar.get(Calendar.DATE));
        String daystr = day.toString();
        
        Integer year = new Integer (calendar.get(Calendar.YEAR));
        String yearstr = year.toString();
        
        Integer month = new Integer (calendar.get(Calendar.MONTH));
        String monthstr = month.toString();
                     
        
        //System.out.println ("Hello This is rohit - " + day + "//" + month + "//"+  year);
        
        weatherdata.append ("PlantingMonth,");
        //weatherdata.append ((String) jPlantingMonthComboBox.getSelectedItem() + ",");
        weatherdata.append (monthstr + ",");
        
        weatherdata.append ("PlantingDay,");
        //weatherdata.append ((String) jPlantingDayComboBox.getSelectedItem() + ",");
        weatherdata.append (daystr + ",");
        
        weatherdata.append ("PlantingYear,");
        //weatherdata.append ((String) jPlantingYearComboBox.getSelectedItem() + ",");
        weatherdata.append (yearstr + ",");
        
        weatherdata.append ("FawnWeatherStation,");
        String watherstation = (String) weatherStComboBox.getSelectedItem();
        weatherdata.append (watherstation + ",");  
        
        weatherdata.append ("StationLocationId,");        
        weatherdata.append(global_db.getWeatherStationId_GlobalDB (watherstation));
        
        System.out.println("Weather Data -" + weatherdata);
        wthfile.UpdateCache(weatherdata.toString());  
        
        
        
    }*/
    
    /*static DBConnect getDBConnection (String dbName){
        DBConnect db = null;
        if (dbName.equals("global")) {
            
            if (global_db == null) {
                
                global_db = new DBConnect (ServerDetails.DB_URL_SERVER1, ServerDetails.USER_SERVER1, ServerDetails.PASS_SERVER1, global_dbname);
            }
            
            db = global_db; 
            
        } else if (dbName.equals("weather_historic_daily")){
             if (weather_historic_daily == null) {
                
                weather_historic_daily = new DBConnect (ServerDetails.DB_URL_SERVER1, ServerDetails.USER_SERVER1, ServerDetails.PASS_SERVER1, weather_historic_daily_dbname);
            }
            db = weather_historic_daily;    
        } 
        
       return db; 
    }*/
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DSSATMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DSSATMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DSSATMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DSSATMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DSSATMain().setVisible(true);
            }
        });
    }
    
    private CommonMenuBar jMenuBar1;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BedSystemInfo;
    private javax.swing.JPanel CropInfo;
    private javax.swing.JPanel FieldPanel;
    private javax.swing.JPanel GeneralInformation;
    private javax.swing.JPanel IrrigationFertilizerPanel;
    private javax.swing.JPanel SoilInfo;
    private javax.swing.JPanel WeatherInfo;
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bBackButton;
    private javax.swing.JButton bDelete;
    private javax.swing.JButton bFinishButton;
    private javax.swing.JButton bUpdate;
    private javax.swing.JComboBox countyNameGlobal;
    private javax.swing.JTextField jBlockNameText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBoxCropList;
    private javax.swing.JComboBox jComboBoxCultivar;
    private javax.swing.JComboBox jComboBoxFertMaterial;
    private javax.swing.JComboBox jComboBoxFertMethod;
    private javax.swing.JComboBox jComboBoxPlantingMethod;
    private javax.swing.JComboBox jComboBoxPrevCropList;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JTable jFertilizerInfoTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jMainPanel;
    private javax.swing.JMenuBar jMenuBarFields;
    private javax.swing.JMenu jMenuFields;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuIrrigationFertilizer;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemGoToIrrigationFertilizer;
    private javax.swing.JMenuItem jMenuItemOptions;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JMenu jMenuRun;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JButton jNextButton;
    private javax.swing.JMenuItem jOpenMenuItem;
    private javax.swing.JTree jProjectExplorer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTextField jSiteNameText;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextRatePerApplication;
    private javax.swing.JTextField jZoneNameText;
    private javax.swing.JPanel pButtonPanel;
    private javax.swing.JPanel pFertilizerPanel;
    private javax.swing.JPanel pIrrigationPanel;
    private javax.swing.JComboBox soilSeriesCombobox;
    private javax.swing.JLabel warningMessaage;
    private javax.swing.JComboBox weatherStComboBox;
    // End of variables declaration//GEN-END:variables
}
