/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author andras.kovacs
 */
public class BierenVerwijderen {

    private static final String URL = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select alcohol, naam from bieren where alcohol between ? and ?" +
            " order by alcohol, naam";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("minimum alcohol%: ");
        BigDecimal minAlcohol = new Scanner(System.in).nextBigDecimal();
        System.out.println("maximum alcohol%:");
        BigDecimal maxAlcohol = new Scanner(System.in).nextBigDecimal();
        try (Connection connection
                = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setBigDecimal(1, minAlcohol);
            statement.setBigDecimal(2, maxAlcohol);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getBigDecimal("alcohol") + " " +
                            resultSet.getString("naam"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

}
