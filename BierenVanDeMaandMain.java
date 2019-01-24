/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

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
public class BierenVanDeMaandMain {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select verkochtsinds, naam from bieren "
            + "where {fn month(verkochtsinds)} = ? "
            + "order by verkochtsinds";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Maandnummer: ");
        int maand = new Scanner(System.in).nextInt();
        if (maand >= 1 && maand <= 12) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement statement = connection.prepareStatement(SELECT)) {
                statement.setInt(1, maand);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println(resultSet.getDate("verkochtsinds") + " "
                                + resultSet.getString("naam"));
                    }
                }
                connection.commit();
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }else{
            System.out.println("Tik een getal tussen 1 en 12");
        }
    }

}
