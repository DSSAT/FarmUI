package org.agmip.ui.tablecell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

public class CellButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private CellButtonPane acceptRejectPane;
    private Action action;
    JTable table;
    public CellButtonEditor(Action act, final JTable table) {
        acceptRejectPane = new CellButtonPane();
        action = act;
        acceptRejectPane.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel( table.getEditingRow() );
                fireEditingStopped();
                ActionEvent event = new ActionEvent(
                        table,
                        ActionEvent.ACTION_PERFORMED,
                        "" + row);
                action.actionPerformed(event);
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return acceptRejectPane.getState();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
		
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            acceptRejectPane.setBackground(table.getSelectionBackground());
        } else {
            acceptRejectPane.setBackground(table.getBackground());
        }
        return acceptRejectPane;
    }
}
