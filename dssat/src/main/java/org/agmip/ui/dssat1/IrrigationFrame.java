/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.dssat1;

import org.agmip.ui.tablecell.CellButtonEditor;
import com.opencsv.CSVReader;
import com.toedter.calendar.JDateChooserCellEditor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.agmip.ui.tablecell.CellButtonRenderer;
import org.agmip.ui.tablecell.CellCalendarRenderer;
import org.agmip.ui.tablecell.CellComboBoxEditor;
import org.agmip.ui.tablecell.CellComboBoxRenderer;
import org.agmip.ui.tablecell.CellTimeEditor;
import org.agmip.ui.tablecell.CellTimeRenderer;
import org.agmip.ui.utils.AppKeys;
import static org.agmip.ui.utils.AppKeys.FERT_AMOUNT_RATE;
import static org.agmip.ui.utils.AppKeys.FERT_APP_CODE;
import static org.agmip.ui.utils.AppKeys.FERT_DATE;
import static org.agmip.ui.utils.AppKeys.FERT_DEPTH;
import org.agmip.ui.utils.Couple;
import org.agmip.ui.utils.DssatUtil;
import org.agmip.ui.utils.DssatXFileGenerator;
import org.agmip.ui.utils.ReadAndInitIrrigationFertInfo;
import org.agmip.ui.utils.WeatherFileSystem;
import static org.agmip.ui.utils.AppKeys.FERT_MATERIAL_CODE;
import static org.agmip.ui.utils.AppKeys.IRR_DATE;
import static org.agmip.ui.utils.AppKeys.IRR_DEPTH;
import static org.agmip.ui.utils.AppKeys.IRR_DURATION;
import static org.agmip.ui.utils.AppKeys.IRR_EFF;
import static org.agmip.ui.utils.AppKeys.IRR_INTERVAL;
import static org.agmip.ui.utils.AppKeys.IRR_NUMBER;
import static org.agmip.ui.utils.AppKeys.IRR_OFFSET;
import static org.agmip.ui.utils.AppKeys.IRR_OPER;
import static org.agmip.ui.utils.AppKeys.IRR_ROW_COUNT;
import static org.agmip.ui.utils.AppKeys.IRR_SPACING;
import static org.agmip.ui.utils.AppKeys.IRR_START_TIME;
import static org.agmip.ui.utils.AppKeys.IRR_VALUE;
import org.agmip.ui.utils.FrameTracker;

/**
 *
 * @author rohit
 */
public class IrrigationFrame extends javax.swing.JFrame {

    
    
    /**
     * Creates new form IrrigationFrame
     */
    public IrrigationFrame() {
        
        initComponents();
        
        Couple[] fertMatArray = ReadAndInitIrrigationFertInfo.getFertmaterial().toArray(new Couple[0]);
        Couple[] fertMethArray = ReadAndInitIrrigationFertInfo.getFertmethod().toArray(new Couple[0]);
        Couple[] irriMethArray = ReadAndInitIrrigationFertInfo.getIrrigationMethod().toArray(new Couple[0]);
        fertilizerTable.getColumn("Fertilization Material").setCellRenderer(new CellComboBoxRenderer(new Couple("", "").converToArray(fertMatArray)));
        fertilizerTable.getColumn("Fertilization Material").setCellEditor(new CellComboBoxEditor(new Couple("", "").converToArray(fertMatArray)));


        
        fertilizerTable.getColumn("Fertilization Method").setCellRenderer(new CellComboBoxRenderer(new Couple("", "").converToArray(fertMethArray)));
        fertilizerTable.getColumn("Fertilization Method").setCellEditor(new CellComboBoxEditor(new Couple("", "").converToArray(fertMethArray)));
        
        fertilizerTable.getColumn("Delete").setCellRenderer(new CellButtonRenderer());
        fertilizerTable.getColumn("Delete").setCellEditor(new CellButtonEditor(delete, fertilizerTable));

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        try {
//            Date plantingDate = dateFormat.parse(DssatUtil.getInfo(AppKeys.PLANTING_DATE)); 
//            plantingDate = new Date ();
//            fertilizerTable.getColumn("Fertilization Date").setCellRenderer(plantingDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        
        
        fertilizerTable.getColumn("Fertilization Date").setCellEditor(new JDateChooserCellEditor());
        
        
        irrigationTable.getColumn("Date").setCellRenderer(new CellCalendarRenderer(new Date()));
        irrigationTable.getColumn("Date").setCellEditor(new JDateChooserCellEditor());
        irrigationTable.getColumn("Irrigation Method").setCellRenderer(new CellComboBoxRenderer(new Couple("", "").converToArray(irriMethArray)));
        irrigationTable.getColumn("Irrigation Method").setCellEditor(new CellComboBoxEditor(new Couple("", "").converToArray(irriMethArray)));
        irrigationTable.getColumn("Start Time(HH:MM)").setCellRenderer(new CellTimeRenderer(new Date()));
        irrigationTable.getColumn("Start Time(HH:MM)").setCellEditor(new CellTimeEditor());
        irrigationTable.getColumn("Delete").setCellRenderer(new CellButtonRenderer());
        irrigationTable.getColumn("Delete").setCellEditor(new CellButtonEditor(delete, irrigationTable));
        
        setLocation(400, 50);
        setResizable(false);        
    }
    
    Action delete = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable) e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            ((DefaultTableModel) table.getModel()).removeRow(modelRow);
        }
    };
 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel49 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        irrigationDepthET = new javax.swing.JTextField();
        dripEfficiency = new javax.swing.JTextField();
        dripDepthET1 = new javax.swing.JTextField();
        dripOffsetET = new javax.swing.JTextField();
        dripSpacingET = new javax.swing.JTextField();
        dripRateET = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        fertilizerTable = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        irrigationLabel = new javax.swing.JLabel();
        fertilizerLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jMsgLabelIrrigation = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        irrigationTable = new javax.swing.JTable();
        warningMessaage = new javax.swing.JLabel();
        bBackButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonGroup.add(jRadioButton1);
        jRadioButton1.setText("Auto Irrigation");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Scheduled Irrigation");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel49.setText("Dripper Rate (0.3-3 ml/sec)");
        jLabel49.setToolTipText("Valid Irrigation rate 0.3-3 ml/sec.");
        jLabel49.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel49.setMinimumSize(new java.awt.Dimension(258, 26));
        jLabel49.setPreferredSize(new java.awt.Dimension(258, 26));

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel51.setText("Drip Emitter Spacing (Inches)");
        jLabel51.setToolTipText("Valid Emitter Spacing");
        jLabel51.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel51.setMinimumSize(new java.awt.Dimension(258, 26));
        jLabel51.setPreferredSize(new java.awt.Dimension(258, 26));

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel53.setText("Dripper Emitter Offset  From Bed Center Line (0-8 Inches)");
        jLabel53.setToolTipText("Valid Dripper Emitter Offset 0-8 inches or less than half of Bed Width.");
        jLabel53.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel53.setMinimumSize(new java.awt.Dimension(258, 26));
        jLabel53.setPreferredSize(new java.awt.Dimension(258, 26));

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel54.setText("Dripper Emitter depth from Surface (0-8 inches)");
        jLabel54.setToolTipText("Valid Dripper Emitter depth from surface is 0-8 inches.");
        jLabel54.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel54.setMinimumSize(new java.awt.Dimension(258, 26));
        jLabel54.setPreferredSize(new java.awt.Dimension(258, 26));

        jLabel1.setText("Dripper Efficieny (3-99 in %)");
        jLabel1.setToolTipText("Valid Dripper efficiency 3%-99%.");
        jLabel1.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel1.setPreferredSize(new java.awt.Dimension(93, 26));

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel58.setText("Irrigation Depth (6-8 inches)");
        jLabel58.setToolTipText("Valid Irrigation depth 6-18 inches.");
        jLabel58.setMaximumSize(new java.awt.Dimension(258, 26));
        jLabel58.setMinimumSize(new java.awt.Dimension(258, 26));
        jLabel58.setPreferredSize(new java.awt.Dimension(258, 26));

        irrigationDepthET.setText("6.0");
        irrigationDepthET.setToolTipText("Valid Irrigation depth 6-18 inches.");
        irrigationDepthET.setMaximumSize(new java.awt.Dimension(160, 26));
        irrigationDepthET.setMinimumSize(new java.awt.Dimension(160, 26));
        irrigationDepthET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irrigationDepthETActionPerformed(evt);
            }
        });

        dripEfficiency.setText("3.0");
        dripEfficiency.setToolTipText("Valid Dripper efficiency 3%-99%.");
        dripEfficiency.setMaximumSize(new java.awt.Dimension(160, 26));
        dripEfficiency.setMinimumSize(new java.awt.Dimension(160, 26));

        dripDepthET1.setText("0.0");
        dripDepthET1.setToolTipText("Valid Dripper Emitter depth from surface is 0-8 inches.");
        dripDepthET1.setMaximumSize(new java.awt.Dimension(160, 26));
        dripDepthET1.setMinimumSize(new java.awt.Dimension(160, 26));

        dripOffsetET.setText("0.0");
        dripOffsetET.setToolTipText("Valid Dripper Emitter Offset 0-8 inches or less than half of Bed Width.");
        dripOffsetET.setMaximumSize(new java.awt.Dimension(160, 26));
        dripOffsetET.setMinimumSize(new java.awt.Dimension(160, 26));

        dripSpacingET.setText("1.0");
        dripSpacingET.setToolTipText("Valid Emitter Spacing");
        dripSpacingET.setMaximumSize(new java.awt.Dimension(160, 26));
        dripSpacingET.setMinimumSize(new java.awt.Dimension(160, 26));
        dripSpacingET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dripSpacingETActionPerformed(evt);
            }
        });

        dripRateET.setText("0.3");
        dripRateET.setToolTipText("Valid Irrigation rate 0.3-3 ml/sec.");
        dripRateET.setMaximumSize(new java.awt.Dimension(160, 26));
        dripRateET.setMinimumSize(new java.awt.Dimension(160, 26));

        fertilizerTable.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fertilizerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fertilization Date", "Fertilization Material", "Fertilization Method", "Fertilizer Depth(inches)", "Rate Per Application", "Delete"
            }
        ));
        fertilizerTable.setMinimumSize(new java.awt.Dimension(105, 0));
        fertilizerTable.setPreferredSize(new java.awt.Dimension(452, 113));
        fertilizerTable.setRowHeight(30);
        jScrollPane3.setViewportView(fertilizerTable);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Add Row");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        irrigationLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        irrigationLabel.setForeground(new java.awt.Color(51, 0, 204));
        irrigationLabel.setText("Click \"Add Row\" Button to start inserting row in the table");

        fertilizerLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fertilizerLabel.setForeground(new java.awt.Color(0, 0, 204));
        fertilizerLabel.setText("Click \"Add Row\" Button to start inserting row in the table");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Add Row");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        irrigationTable.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        irrigationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Irrigation Method", "Water Table Depth", "Start Time(HH:MM)", "Duration(min)", "Interval(min)", "Event Times", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        irrigationTable.setPreferredSize(new java.awt.Dimension(452, 113));
        irrigationTable.setRowHeight(30);
        irrigationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                irrigationTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(irrigationTable);

        warningMessaage.setForeground(new java.awt.Color(255, 0, 0));

        bBackButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bBackButton.setText("Back");
        bBackButton.setMaximumSize(new java.awt.Dimension(67, 25));
        bBackButton.setMinimumSize(new java.awt.Dimension(67, 25));
        bBackButton.setPreferredSize(new java.awt.Dimension(67, 25));
        bBackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bBackButtonMouseClicked(evt);
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(bBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dripRateET, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dripSpacingET, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(262, 262, 262))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(161, 161, 161))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dripOffsetET, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dripDepthET1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dripEfficiency, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(irrigationDepthET, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(179, 179, 179))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton2)
                        .addGap(51, 51, 51)
                        .addComponent(jMsgLabelIrrigation, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(218, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(fertilizerLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(27, 27, 27)
                                .addComponent(irrigationLabel))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jMsgLabelIrrigation, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dripRateET, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dripSpacingET, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dripOffsetET, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dripDepthET1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dripEfficiency, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(irrigationDepthET, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(irrigationLabel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warningMessaage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(fertilizerLabel))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(206, 206, 206))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void irrigationDepthETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_irrigationDepthETActionPerformed
      
    }//GEN-LAST:event_irrigationDepthETActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        irrigationTable.setEnabled(true);
        irrigationLabel.setEnabled(true);
        jButton3.setEnabled(true);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        irrigationTable.setEnabled(false);
        irrigationLabel.setEnabled(false);
        jButton3.setEnabled(false);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private boolean ValidateFram2Data() {

        boolean isSuccess = true;
        String str = new String("");

        str = irrigationDepthET.getText();
        if (str.trim().isEmpty()) {
            jLabel58.setForeground(Color.red);
            isSuccess = false;
        } else {
            jLabel58.setForeground(Color.BLACK);

        }

        str = dripDepthET1.getText();
        if (str.trim().isEmpty()) {
            jLabel54.setForeground(Color.red);
            isSuccess = false;
        } else {
            jLabel54.setForeground(Color.BLACK);

        }

        str = dripOffsetET.getText();
        if (str.trim().isEmpty()) {
            jLabel53.setForeground(Color.red);
            isSuccess = false;
        } else {
            jLabel53.setForeground(Color.BLACK);
        }

        str = dripRateET.getText();
        if (str.trim().isEmpty()) {
            jLabel49.setForeground(Color.red);
            isSuccess = false;
        } else {
            jLabel49.setForeground(Color.BLACK);
        }

        str = dripSpacingET.getText();
        if (str.trim().isEmpty()) {
            jLabel51.setForeground(Color.red);
            isSuccess = false;
        } else {
            jLabel51.setForeground(Color.BLACK);
        }

        //Update cahce with fertilizer data
        DefaultTableModel model = (DefaultTableModel) fertilizerTable.getModel();
        DssatUtil.updateInfo(AppKeys.FERT_ROW_COUNT, String.valueOf(model.getRowCount()));

        if (validateFertilizerTable(model)) return false;
        
        
        for (int i = 0; i < model.getRowCount(); i++) {
            int j = 0;
            Date date = (Date) model.getValueAt(i, j);
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String[] d = format.format(date).split("[.]");
            String date_S = d[0] + d[1] + d[2];
            long jd = Integer.parseInt(d[0].substring(2)) * 1000 + dayOfYear(d);

            j++;
            String mat = model.getValueAt(i, j).toString();
            String matCode = null;
            
            // Check if the material value is null or not {
            if (mat!=null && ReadAndInitIrrigationFertInfo.getFertMaterialVsCode().containsKey(mat)) {
                matCode = ReadAndInitIrrigationFertInfo.getFertMaterialVsCode().get(mat);
            }

            j++;
            String meth = model.getValueAt(i, j).toString();
            String methCode = null;
            
            if (meth!=null && ReadAndInitIrrigationFertInfo.getFertMethodVsCode().containsKey(meth)) {
                methCode = ReadAndInitIrrigationFertInfo.getFertMethodVsCode().get(meth);
            }

            j++;            
            String fertilizationDepth = model.getValueAt(i,j).toString ();
            
            j++;
            String rate = model.getValueAt(i, j).toString();

            DssatUtil.updateInfo(FERT_DATE+i, date_S);
            DssatUtil.updateInfo(FERT_APP_CODE+i, methCode);
            DssatUtil.updateInfo(FERT_MATERIAL_CODE+i, matCode);
            DssatUtil.updateInfo(FERT_DEPTH, fertilizationDepth);
            DssatUtil.updateInfo(FERT_AMOUNT_RATE+i, rate);           


        }

        DssatUtil.updateInfo(IRR_EFF, dripEfficiency.getText());
        
        // Update cache with irrigation data
        model = (DefaultTableModel) irrigationTable.getModel();
        if (validateIrrigationTableData(model)) return false;
        
        DssatUtil.updateInfo(IRR_ROW_COUNT, String.valueOf(model.getRowCount()));
        DssatUtil.updateInfo(IRR_DEPTH, irrigationDepthET.getText());
        DssatUtil.updateInfo(AppKeys.DRIPPER_EMITTER_DEPTH, dripDepthET1.getText().trim());
        for (int i = 0; i < model.getRowCount(); i++) {
            int j = 0;
            Date date = (Date) model.getValueAt(i, j);
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String[] d = format.format(date).split("[.]");
            String date_S = d[0] + d[1] + d[2];
            long jd = Integer.parseInt(d[0].substring(2)) * 1000 + dayOfYear(d);

            j++;
            // get the Irrigation Method
            String mat = model.getValueAt(i, j).toString();
            String irrCode = null;
            for (Couple cu : ReadAndInitIrrigationFertInfo.getIrrigationMethod()) {
                if (cu.getMethod().equals(mat)) {
                    irrCode = cu.getCode();
                    break;
                }
            }

            j++;
            
            // Find the Water Table Detpsh
            String water_table_depth = model.getValueAt(i, j).toString();
            j++;
            
            String start_time;
             DateFormat dateFormat = new SimpleDateFormat("HH:mm");
             //dateFormat.format(new Date ());
            if (model.getValueAt(i, j)!=null) {
                start_time = model.getValueAt(i, j).toString();
            } else {
                start_time = dateFormat.format(new Date ());
            }
            j++;

            
            String duration = model.getValueAt(i, j).toString();
            j++;

            
            String interval = model.getValueAt(i, j).toString();
            j++;

            
            String event_time = model.getValueAt(i, j).toString();
            j++;


            DssatUtil.updateInfo(IRR_DATE+i, date_S);
            DssatUtil.updateInfo(IRR_OPER+i, irrCode);
            DssatUtil.updateInfo(IRR_VALUE+i, dripRateET.getText());
            DssatUtil.updateInfo(IRR_START_TIME+i, start_time);
            DssatUtil.updateInfo(IRR_DURATION+i, duration);
            DssatUtil.updateInfo(IRR_INTERVAL+i, interval);
            DssatUtil.updateInfo(IRR_NUMBER+i, event_time);
            DssatUtil.updateInfo(IRR_SPACING, dripSpacingET.getText());

            DssatUtil.updateInfo(IRR_OFFSET, dripOffsetET.getText());

        }
        return isSuccess;
    }

    private int dayOfYear(String[] d) {
        Calendar gc = Calendar.getInstance();
        gc.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        return gc.get(6);
    }
    
    
    private void bBackButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBackButtonMouseClicked
        if (FrameTracker.first!=null) {
            this.setVisible(false);
            FrameTracker.first.setVisible(true);
        }
    }//GEN-LAST:event_bBackButtonMouseClicked

    private void dripSpacingETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dripSpacingETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dripSpacingETActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) irrigationTable.getModel();
        if (validateIrrigationTableData(model)) return;
        Vector rowData = new Vector();
        rowData.add(new Date());
        model.addRow(rowData);
    }//GEN-LAST:event_jButton3ActionPerformed

    private boolean validateIrrigationTableData(DefaultTableModel model) {
        int r = model.getRowCount();
        if (r > 0) {
            int c = model.getColumnCount();
            r--;
            for (int i = 0; i < c - 1; i++) {
                if (model.getValueAt(r, i) == null) {
                    if (i==3) continue;
                    irrigationLabel.setText("Please enter details for last row first !!");
                    irrigationLabel.setForeground(Color.red);
                    return true;
                }
            }
        }
        irrigationLabel.setText("Click \"Add Row\" Button to start inserting row in the table");
        irrigationLabel.setForeground(Color.blue);
        return false;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) fertilizerTable.getModel();
        if (validateFertilizerTable(model)) return;
        Vector rowData = new Vector();
        rowData.add(new Date());
        model.addRow(rowData);
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean validateFertilizerTable(DefaultTableModel model) {
        int r = model.getRowCount();
        if (r > 0) {
            int c = model.getColumnCount();
            r--;
            for (int i = 0; i < c - 1; i++) {
                if (model.getValueAt(r, i) == null) {
                    fertilizerLabel.setText("Please enter details for last row first !!");
                    fertilizerLabel.setForeground(Color.red);
                    return true;
                }
            }
        }
        fertilizerLabel.setText("Click \"Add Row\" Button to start inserting row in the table");
        fertilizerLabel.setForeground(Color.blue);
        return false;
    }

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        
        // Create the X File Here, If X File is not generated then Don't Proceed Further.
        
        // Before Creating the X File we need to validate the data in the frame
        ValidateFram2Data();
        
        try {
            DssatXFileGenerator.createXFileOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (FrameTracker.last == null) {
            FrameTracker.last = new ResultFrame();
        }

        this.setVisible(false);
        FrameTracker.last.setVisible(true);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void irrigationTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_irrigationTableMouseClicked
        // TODO add your handling code here:

        if (irrigationTable.getSelectedRow() >= 0) {
            warningMessaage.setText("");
            DefaultTableModel model = (DefaultTableModel) irrigationTable.getModel();
            Date date = new Date(model.getValueAt(irrigationTable.getSelectedRow(), 0).toString());
            //jPlantingDateChooser.setDate(date);
        }
    }//GEN-LAST:event_irrigationTableMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBackButton;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JTextField dripDepthET1;
    private javax.swing.JTextField dripEfficiency;
    private javax.swing.JTextField dripOffsetET;
    private javax.swing.JTextField dripRateET;
    private javax.swing.JTextField dripSpacingET;
    private javax.swing.JLabel fertilizerLabel;
    public static javax.swing.JTable fertilizerTable;
    private javax.swing.JTextField irrigationDepthET;
    private javax.swing.JLabel irrigationLabel;
    private javax.swing.JTable irrigationTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jMsgLabelIrrigation;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel warningMessaage;
    // End of variables declaration//GEN-END:variables
}
