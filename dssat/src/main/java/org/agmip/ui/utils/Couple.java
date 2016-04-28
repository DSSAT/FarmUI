/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

/**
 *
 * @author rohit
 */
public class Couple {
    
    String method;
    String code;

    public String getMethod() {
        return method;
    }

    public String getCode() {
        return code;
    }

    public Couple(String method, String code) {
        this.method = method;
        this.code = code;
    }

    @Override
    public String toString() {
        return method;
    }

    public String[] converToArray(Couple[] array) {
        String[] stringArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            stringArray[i] = array[i].method;
        }
        return stringArray;
    }
    
}
