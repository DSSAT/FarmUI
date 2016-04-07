/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template wthfile, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

import com.opencsv.CSVReader;
//import static dssat.DSSATMain.soil_db;
import java.awt.Toolkit;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rkmalik
 */
public class IrrigationFertilizer extends javax.swing.JFrame {

    javax.swing.JFrame dssatmain = null;
    static WeatherFileSystem wthfile = null;
    //private static DBConnect soil_db = null; 
    
    /**
     * Creates new form IrrigationFertilizer
     */

    public IrrigationFertilizer(javax.swing.JFrame mainframe) {
        if (dssatmain == null)
            dssatmain = mainframe;
        
        wthfile = null;
               this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       this.pack();
        this.setVisible(true);
        
//        if (mainobj)
//            mainobj = mainclass;
        initComponents();
        initIrrigationDate ();
        initMyComponents();
        
        initFertInfo ();
        initFrameSize ();
    }
    
    private void initMyComponents(){
        jMenuBar1 = new CommonMenuBar();
        setJMenuBar(jMenuBar1);        
    }
      
    private void initIrrigationDate ()
    {
        Date date = new Date ();
        jDateChooser1.setDate(date);
    }
    private void initFrameSize ()
    {
       // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       //this.setBounds(0,0,screenSize.width, screenSize.height);
        
       this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       this.pack();
       this.setVisible(true);

        //pack();
    }
     
    private void initFertInfo () 
    {
  
        LinkedList<String> fertmethod = new LinkedList<String> ();
        LinkedList<String> fertmaterial = new LinkedList<> ();
        
        CSVReader reader;
        String [] nextLine;

        try {
            reader = new CSVReader(new FileReader(".\\data\\dssat_codelookup.csv"));
            while ((nextLine = reader.readNext()) != null) {
                //cropName.add(nextLine[1]);
                String codematerial = nextLine[2];
                String codemethod = nextLine[1];
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
            }            
        } catch (IOException e) {}
        
        
        Collections.sort(fertmethod);
        Collections.sort(fertmaterial);
        for (int i = 0; i < fertmaterial.size(); i++)
        {
            jComboBoxFertMaterial.addItem(fertmaterial.get(i));    
        }
        
        for (int i = 0; i < fertmethod.size(); i++)
        {
            jComboBoxFertMethod.addItem(fertmethod.get(i));    
        }
    }
       
    
/*private void initFertInfo () 
    {
        soil_db = new DBConnect (ServerDetails.SERVER_NUM_RW, ServerDetails.soil_dbname);
        StringBuilder searchSpec = new StringBuilder ("");
        searchSpec.append("SELECT Description, Code FROM dssat_codelookup WHERE Code LIKE \"FE0%\"  OR  Code LIKE \"AP0%\"  ORDER BY Code ASC");
        
        System.out.println (searchSpec);
        ResultSet result = soil_db.Execute (ServerDetails.soil_dbname, searchSpec.toString());
        
        LinkedList<String> fertmethod = new LinkedList<String> ();
        LinkedList<String> fertmaterial = new LinkedList<> ();
        
        CSVReader reader;
        String [] nextLine;

        try {
            reader = new CSVReader(new FileReader(".\\data\\dssat_codelookup.csv"));
            while ((nextLine = reader.readNext()) != null) {
                //cropName.add(nextLine[1]);
                System.out.println("");
                String code = nextLine[2];
                String description = new String ();
                // Check if the code is FE/AP Then Build the descripttion till end.
                if (code.contains("FE")) {                    
                    for (int i = 3; i < nextLine.length; i++)
                        description = description+nextLine[i];
                    fertmaterial.add(description);
                    System.out.println("Method : " + description);
                }else if (code.contains("AP")) {
                    
                    for (int i = 3; i < nextLine.length; i++)
                        description = description+nextLine[i];
                    fertmethod.add(description);
                    System.out.println("Method : " + description);
                }
            }            
        } catch (IOException e) {}
        
        
        
        try {
                while (result.next()) {
                    String fertandmatcode = result.getString("Code");
                    String description = result.getString("Description");
                    
                    
                    if (description.isEmpty() == false){                        
                        if (fertandmatcode.contains("FE")) {   
                            //fertmaterial.add(description);
                            //jComboBoxFertMaterial.addItem(description);
                        } else if (fertandmatcode.contains("AP")){
                            //jComboBoxFertMethod.addItem(description);
                            //fertmethod.add(description);
                        } 
                    }
                }                
                 
             } catch (SQLException e) {                 
                 e.printStackTrace();
             }
        Collections.sort(fertmethod);
        Collections.sort(fertmaterial);
        for (int i = 0; i < fertmaterial.size(); i++)
        {
            jComboBoxFertMaterial.addItem(fertmaterial.get(i));    
        }
        
        for (int i = 0; i < fertmethod.size(); i++)
        {
            jComboBoxFertMethod.addItem(fertmethod.get(i));    
        }
    }*/  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel3 = new javax.swing.JPanel();
        bFinishButton = new javax.swing.JButton();
        bBackButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextRatePerApplication = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBoxFertMaterial = new javax.swing.JComboBox();
        jComboBoxFertMethod = new javax.swing.JComboBox();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jFertilizerInfoTable = new javax.swing.JTable();
        bAdd = new javax.swing.JButton();
        bUpdate = new javax.swing.JButton();
        bDelete = new javax.swing.JButton();
        warningMessaage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);

        jInternalFrame1.setVisible(true);

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bBackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bFinishButton)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBackButton)
                    .addComponent(bFinishButton))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Irrigation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel1.setName("Dripper Information"); // NOI18N

        jLabel1.setText("Dripper Rate");

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel2.setText("gal/Hour");

        jLabel3.setText("Dripper Distance");

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel4.setText("Feet");

        jLabel5.setText("Dripper Emitter Offset  From Bed Center Line");

        jLabel6.setText("Dripper Emitter depth from Surface");

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel7.setText("Inches");

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel8.setText("Inches");

        jLabel14.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel14.setText("Inches");

        jLabel15.setText("Irrigation Depth");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(193, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fertilizer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 3, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel11.setText("lb/Acre");

        jTextRatePerApplication.setColumns(9);

        jLabel10.setText("Rate Per Application");

        jLabel12.setText("Fertilization Material");

        jLabel13.setText("Fertilization Method");

        jLabel16.setText("Fertilization Date");

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
        jScrollPane1.setViewportView(jFertilizerInfoTable);

        bAdd.setText("Add");
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });

        bUpdate.setText("Update");
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel10)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(bAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(bUpdate)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jTextRatePerApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(55, 55, 55))
                                    .addComponent(jComboBoxFertMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxFertMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(28, 28, 28))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(bDelete)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jComboBoxFertMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFertMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextRatePerApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAdd)
                    .addComponent(bUpdate)
                    .addComponent(bDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(343, 343, 343))
        );

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Dripper Information");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bBackButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBackButtonMouseClicked
        // TODO add your handling code here:
        
        this.setVisible(false);
        //jMenuBar1.setVisible(true);
        if (dssatmain != null)
            dssatmain.setVisible(true);
    }//GEN-LAST:event_bBackButtonMouseClicked

    private void bFinishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFinishButtonActionPerformed
        // TODO add your handling code here:
        
        String keyvalue = "dssat, dssat1";
        if (wthfile == null)
            wthfile = WeatherFileSystem.getInstance();
        
        
        wthfile.UpdateCache(keyvalue);
        wthfile.WriteToFile();
    }//GEN-LAST:event_bFinishButtonActionPerformed

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_bAddActionPerformed

    private void bUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUpdateActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_bUpdateActionPerformed

    private void bDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_bDeleteActionPerformed

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
            java.util.logging.Logger.getLogger(IrrigationFertilizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IrrigationFertilizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IrrigationFertilizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IrrigationFertilizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IrrigationFertilizer(null).setVisible(true);
            }
        });
    }
    private CommonMenuBar jMenuBar1;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bBackButton;
    private javax.swing.JButton bDelete;
    private javax.swing.JButton bFinishButton;
    private javax.swing.JButton bUpdate;
    private javax.swing.JComboBox jComboBoxFertMaterial;
    private javax.swing.JComboBox jComboBoxFertMethod;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JTable jFertilizerInfoTable;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextRatePerApplication;
    private javax.swing.JLabel warningMessaage;
    // End of variables declaration//GEN-END:variables
}
