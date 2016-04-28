package org.agmip.ui.tablecell;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CellCalendarRenderer extends JDateChooser implements TableCellRenderer {

    Date inDate;
    
    public CellCalendarRenderer(Date d){
        super(d);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub

        if (value instanceof Date) {
            this.setDate((Date) value);
        } else if (value instanceof Calendar) {
            this.setCalendar((Calendar) value);
        }
        return this;
    }
}
