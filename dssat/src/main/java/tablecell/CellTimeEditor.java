/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat.tablecell;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Piyush
 */
public class CellTimeEditor extends AbstractCellEditor implements TableCellEditor {
    JSpinner timeSpinner = new JSpinner();
    JSpinner.DateEditor timeEditor;
    public CellTimeEditor(){
        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeEditor = new JSpinner.DateEditor(timeSpinner,"HH:mm");
        timeSpinner.setEditor(timeEditor);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            timeSpinner.setBackground(table.getSelectionBackground());
        } else {
            timeSpinner.setBackground(table.getBackground());
        }
        return timeSpinner;
    }

    @Override
    public Object getCellEditorValue() {
        return timeEditor.getFormat().format(timeSpinner.getValue());
    }
}
