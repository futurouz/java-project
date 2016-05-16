/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author kunanan
 */
public class ConnectionDB {
       public static Connection getConnection() {
             Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ou","ou","123");
           
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException except) {
            except.printStackTrace();
        }

        return conn;
           
       }

}
