/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

import java.util.HashMap;

/**
 *
 * @author rkmalik
 */
public abstract class BaseFileSystem {
    
    
    protected static HashMap<String, String> incache;
    protected static HashMap<String, String> outcache;

    protected static String fileName;
    protected static String fileLocation;
    protected static String fileType;  
    
    
    abstract void WriteToFile ();
    abstract HashMap <String, String> ReadFromFile (String attributName);
    abstract void UpdateCache (String keyvalue);
    
}
