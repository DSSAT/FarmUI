package dssat.tablecell;

import dssat.DSSATMain;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import static sun.misc.ClassFileTransformer.add;

class CellButtonPane extends JPanel {

    private JButton delete;
    private String state;

    public CellButtonPane() {
        setLayout(new GridBagLayout());
        delete = new JButton("Delete");
        delete.setActionCommand("delete");

        add(delete);

    }

    public void addActionListener(ActionListener listener) {
        delete.addActionListener(listener);
    }

    public String getState() {
        return state;
    }
}

public class CellButtonRenderer extends DefaultTableCellRenderer {

    private CellButtonPane acceptRejectPane;

    public CellButtonRenderer() {
        acceptRejectPane = new CellButtonPane();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            acceptRejectPane.setBackground(table.getSelectionBackground());
        } else {
            acceptRejectPane.setBackground(table.getBackground());
        }
        return acceptRejectPane;
    }
}

