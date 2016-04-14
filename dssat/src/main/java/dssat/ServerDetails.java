/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dssat;

/**
 *
 * @author rkmalik
 */
public class ServerDetails {
    
    //final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  	
    //final String DB_URL_SERVER = "jdbc:mysql://osg.ufl.edu:3310/test";
    public static final int    SERVER_NUM_RONLY = 1;
    public static final String DB_URL_SERVER1 = "jdbc:mysql://10.36.8.44:3310/";	
    public static final String USER_SERVER1 = "jin";
    public static final String PASS_SERVER1 = "newuserwu";
    
    
    
    
    public final static String global_dbname = "global";
    //public final static String global_tablename = "dssat_countywise_list_of_soils";
    

    public final static String soil_dbname = "ageng_secct";
    //public final static String soil_tablename = "dssat_countywise_list_of_soils";
    

    public final static String weather_historic_daily_dbname = "weather_historic_daily";
    
    
    public static final int    SERVER_NUM_RW = 2;
    public static final String DB_URL_SERVER2 = "jdbc:mysql://10.227.242.217:3306/";	
    public static final String USER_SERVER2 = "if-svc-secc";
    public static final String PASS_SERVER2 = "fe?a72U4En+Z";
    
    

}
