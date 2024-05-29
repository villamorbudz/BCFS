package com.javlovers.bcfs.Screens.BackEnd.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements DBConnection{

    public static final String URL = "jdbc:mysql://192.168.110.250:3306/bcfs";
    public static final String USERNAME = "client";
    public static final String PASSSWORD = "";

    public Connection getConnection(){
        Connection c = null;
        try{
            c= DriverManager.getConnection(URL, USERNAME,PASSSWORD);
            System.out.println("DB Connection Success");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return c;
    }


}