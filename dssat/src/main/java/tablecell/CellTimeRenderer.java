/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat.tablecell;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Piyush
 */
public class CellTimeRenderer implements TableCellRenderer{
    JSpinner timeSpinner;    
    public CellTimeRenderer(Date d){
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner,"HH:mm");
        timeSpinner.setEditor(timeEditor);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub

        if (value instanceof Date) {
            timeSpinner.setValue((Date) value);
        }
        return timeSpinner;
    }
}
