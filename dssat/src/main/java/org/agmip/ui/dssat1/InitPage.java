/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.dssat1;

/**
 *
 * @author rohit
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

public class InitPage extends JFrame {

    private JButton enterButton;
    private JLabel siteLabel;
    private JTextField siteTextField;
    private JLabel blockLabel;
    private JTextField blockTextField;
    private JLabel zoneLabel;
    private JTextField zoneTextField;
    private JPanel inputPanel;
    private JLabel errorLabel;
    private JLabel background;

    public InitPage() {

        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setAlwaysOnTop(true);

        setLayout(new BorderLayout());
        URL url = getClass().getResource("/images/InterfaceTitle.jpg");
        background = new JLabel(new ImageIcon(url));
        add(background);
        background.setLayout(null);
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        siteLabel = new JLabel("Farm  :");
        siteTextField = new JTextField(18);
        blockLabel = new JLabel("Block :");
        blockTextField = new JTextField(18);
        zoneLabel = new JLabel("Zone :");
        zoneTextField = new JTextField(18);
        enterButton = new JButton("Enter");
        errorLabel = new JLabel("Please enter all the details marked in Red");
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);
        inputPanel.add(siteLabel);
        inputPanel.add(siteTextField);
        inputPanel.add(blockLabel);
        inputPanel.add(blockTextField);
        inputPanel.add(zoneLabel);
        inputPanel.add(zoneTextField);
        inputPanel.add(enterButton);
        inputPanel.add(errorLabel);
        background.add(inputPanel);
        inputPanel.setBounds(150, 400, 260, 200);
        inputPanel.setOpaque(false);
        setResizable(false);
        getRootPane().setDefaultButton(enterButton);
        initActions();
    }

    private void initActions() {
        enterButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                siteLabel.setForeground(Color.black);
                blockLabel.setForeground(Color.black);
                zoneLabel.setForeground(Color.black);

                if (isInvalidData()) {
                    errorLabel.setVisible(true);

                    if (siteTextField.getText().length() < 2) {
                        siteLabel.setForeground(Color.red);
                        errorLabel.setForeground(Color.red);
                        errorLabel.setText("Site  name need minimum Two characters !! ");
                        return;
                    }

                    if (blockTextField.getText().length() < 2) {
                        blockLabel.setForeground(Color.red);
                        errorLabel.setForeground(Color.red);
                        errorLabel.setText("Block name need minimum Two characters !! ");
                        return;
                    }

                    if (zoneTextField.getText().length() < 2) {
                        zoneLabel.setForeground(Color.red);
                        errorLabel.setForeground(Color.red);
                        errorLabel.setText("Zone need minimum Two characters !! ");
                        return;
                    }

                } else {
                    errorLabel.setText("Please wait while Initializing the application... !! ");
                    errorLabel.setForeground(Color.blue);
                    errorLabel.setVisible(true);
                    final GeneralInfo main = new GeneralInfo(siteTextField.getText() + ";" + blockTextField.getText() + ";" + zoneTextField.getText());
                    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                    executor.schedule(new Runnable() {

                        @Override
                        public void run() {
                            setVisible(false);
                            main.setVisible(true);
                        }
                    }, 1, TimeUnit.SECONDS);

                }
            }
        });
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InitPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InitPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InitPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InitPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        new InitPage();
    }

    private boolean isInvalidData() {
        if (siteTextField.getText().length() < 2
                || blockTextField.getText().length() < 2
                || zoneTextField.getText().length() < 2) {
            return true;
        } else {
            return false;
        }
    }
}
