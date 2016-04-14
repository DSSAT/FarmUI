/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author rkmalik
 */
public class CommonMenuBar extends JMenuBar{
    JMenu jMenu1;
    JMenu jMenu2;
    JMenu jMenu3;
    
    JMenuItem jMenuItem1_4;
    JMenuItem jMenuItem1_1;
    JMenuItem jMenuItem1_2;
    JMenuItem jMenuItem1_3;   
    
    JMenuItem jMenuItem2;
    
    public CommonMenuBar(){
        super();
        initComponents();
        
    }
    
    public void initComponents(){
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1_4 = new javax.swing.JMenuItem();
        jMenuItem1_1 = new javax.swing.JMenuItem();
        jMenuItem1_2 = new javax.swing.JMenuItem(); 
        jMenuItem1_3 = new javax.swing.JMenuItem();         
        
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        setForeground(new java.awt.Color(102, 255, 255));

        jMenu1.setForeground(new java.awt.Color(0, 153, 153));
        jMenu1.setText("File");

      
        jMenuItem1_1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1_1.setText("New Project...");
        jMenuItem1_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1_1);
        
        jMenuItem1_2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1_2.setText("Open Project...");
        jMenuItem1_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1_2);
        
        jMenuItem1_3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1_3.setText("Save Project...");
        jMenuItem1_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1_3);

        jMenuItem1_4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1_4.setText("Exit");
        jMenuItem1_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1_4);
        

        add(jMenu1);

        jMenu2.setForeground(new java.awt.Color(0, 153, 153));
        jMenu2.setText("Fields");
        add(jMenu2);

        jMenu3.setForeground(new java.awt.Color(0, 153, 153));
        jMenu3.setText("Help");

        jMenuItem2.setText("About XBuild");
        jMenu3.add(jMenuItem2);

        add(jMenu3);
        
        setVisible(true);
    }
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        
        System.exit(0);
    }   
}
