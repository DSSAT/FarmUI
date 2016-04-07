/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;


import static dssat.BaseFileSystem.fileName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rkmalik
 */
public class CultivaFileSystem extends BaseFileSystem {
    
    private static CultivaFileSystem instance = null;
    
     private CultivaFileSystem () {        
        fileName = null; 
        fileLocation = null;
        fileType = null;
        incache = new HashMap <String, String>();
        outcache = new HashMap <String, String>();
    }
    
    // This mehto// Update table will update the data for the required keys 
    // UpdateHashTable ()
    public static CultivaFileSystem getInstance ()
    {
        if (instance == null) {
            
            instance = new CultivaFileSystem();
        }
        return ((CultivaFileSystem)instance); 
    }
    
    public HashMap <String, String> ReadFromFile (String attributName) {
        //C:\DSSAT46\Genotype
        HashMap <String, String> locoutcache = new HashMap <String, String>();
        //StringBuilder filename = new StringBuilder ("C:\\DSSAT46\\Genotype\\");
        StringBuilder filename = new StringBuilder (".\\Genotype\\");
        filename.append(incache.get("CultivarFile"));
        File file = new File (filename.toString());
        FileReader fr = null;
         try {
             fr = new FileReader(file);
         } catch (FileNotFoundException ex) {
             Logger.getLogger(CultivaFileSystem.class.getName()).log(Level.SEVERE, null, ex);
         }
        BufferedReader br = new BufferedReader(fr);
        String line = new String ();
        
        try {
            while ((line = br.readLine()) != null) {
                if (line.length() <= 1)
                    continue;
                if ((line.charAt(0)== '!') || (line.charAt(0) == '$') || (line.charAt(0) == '*') || (line.charAt(0) == '@'))
                    continue;
                
                //line.sub
                String  varcode = line.substring(0, 6);
                String  varname;
                if (line.length() <= 24) {
                    varname = line.substring(7);
                } else 
                    varname = line.substring(7, 24);
                
                locoutcache.put(varcode, varname);
                //outcache.put(varcode, varname);
            }
        } catch (IOException e) {
            
            
        }       
        
        return locoutcache;        
    }
    
    public void WriteToFile () {
        
        
    }
    
    // This mehtod take a comma seperated key value pair, parse those keyvalue pairs and then updaate the cache
    public void UpdateCache (String keyvalue) {  
        String [] tokens  = keyvalue.split(",");        
        for (int i = 0; i < tokens.length; i++)
        {
            String key = tokens[i];
            i++;
            String value = tokens[i];
            
            
            System.out.printf(key + " " + value + " ---> ");
            incache.put(key, value);           
        }
               
    }
    
}
