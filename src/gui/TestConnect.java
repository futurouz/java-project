package gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import gui.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.History;

//import model.History;
import model.Person;

/**
 *
 * @author narut
 */
public class TestConnect {
     public static void main(String[] args) throws SQLException  {
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from Persons");
        ResultSet rs = ps.executeQuery();
        rs.next();
        String test = rs.getString("pername");
        System.out.println(test);
    }
}
