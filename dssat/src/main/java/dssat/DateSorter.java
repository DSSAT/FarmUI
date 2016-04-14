/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piyush
 */
public class DateSorter implements Comparator {

    int colIndex;

    public DateSorter(int colIndex) {
        this.colIndex = colIndex;
    }

    public int compare(Object a, Object b) {
        try {
            Vector v1 = (Vector) a;
            Vector v2 = (Vector) b;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            if(v1 == null && v2 !=null)
                return -1;
            if(v1!=null && v2 == null)
                return 1;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);
            if(o1 == null && o2 == null)
                return 0;
            if(o1 == null && o2 !=null)
                return 1;
            if(o1!=null && o2 == null)
                return -1;
            if (df.parse(o1.toString()).after(df.parse(o2.toString()))) {
                return 1;
            } else {
                return -1;
            }
        } catch (ParseException ex) {
            Logger.getLogger(DateSorter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

}
