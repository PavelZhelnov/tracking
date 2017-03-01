package com.tmw.tracking.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by pzhelnov on 2/17/2017.
 */
public class JavaMysqlConnection {


    private String host = "162.253.124.32:3306"; // For running on localhost
    private String db = "sotrackc_tracking";
    private String userName = "sotrackc_project";
    private String password = "33_Vcklydc_'Y";

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn =  DriverManager.getConnection("jdbc:mysql://" + host + "/" + db+"?autoReconnect=true&amp;failOverReadOnly=false&amp;maxReconnects=10", userName, password);
            return conn;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}