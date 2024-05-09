/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.mydatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author hatice.kemence
 */
public class SQLiteConnectionTest {
    public static void main(String[] args){
        Connection connection = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Hatice Kemençe\\Desktop\\mydatabase\\haticeDatabase.db");
            
            System.out.println("Veritabanına başarılı bir şekilde bağlanıldı.");
            
            connection.close();
        } catch (Exception e){
            System.err.println("Bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                System.err.println("Bağlantı kapatma hatası: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
}
