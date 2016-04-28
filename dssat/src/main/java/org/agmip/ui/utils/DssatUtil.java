/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import java.util.HashMap;

/**
 *
 * @author rohit
 */
public class DssatUtil {
    
    public static HashMap<String, String> appDataMap = new HashMap<> ();
    
    // Expose contains key from this utility to the outside files
    public static boolean containsKey (String key) {
        return appDataMap.containsKey(key);
    }
    
    public static void updateInfo(String key, String value) {
        appDataMap.put(key, value);
    }

    public static String getInfo (String key) {
        return appDataMap.get(key);
    }
    
}
