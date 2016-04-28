/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.tablecell;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Rohit Kumar Malik
 */
public class CellComboBoxEditor extends DefaultCellEditor{
    public CellComboBoxEditor(String[] items){
        super(new JComboBox(items));
    }
}
