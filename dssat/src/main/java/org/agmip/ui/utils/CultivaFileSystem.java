/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rkmalik
 */
public class CultivaFileSystem {
    
    private static CultivaFileSystem instance = null;
    private static HashMap <String, String> incache = new HashMap <>();
    private static HashMap <String, String> outcache= new HashMap <>(); 
    
    
    public static HashMap <String, String> ReadFromFile (String attributName) {
        //C:\DSSAT46\Genotype
        HashMap <String, String> locoutcache = new HashMap <String, String>();
        StringBuilder filename = new StringBuilder ("/properties/Genotype/");
        //StringBuilder filename = new StringBuilder (".\\Genotype\\");        
        System.out.println("\nCultivar File Name: " + incache.get("CultivarFile"));
        filename.append(incache.get("CultivarFile"));
        File file = new File (filename.toString());
        InputStreamReader fr = null;
         try {
            // fr = new FileReader(file);
             System.out.println("File To open : " + filename.toString());
             fr = new InputStreamReader(CultivaFileSystem.class.getClass().getResourceAsStream(filename.toString()));
             //reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/properties/csv/dssat_crop_lookup.csv")));            
             
         } catch (Exception ex) {
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

    
    // This mehtod take a comma seperated key value pair, parse those keyvalue pairs and then updaate the cache
    public static void UpdateCache (String keyvalue) {  
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
    
    public static void updatedCache (String key, String value) {
        incache.put(key, value);
    }
}
