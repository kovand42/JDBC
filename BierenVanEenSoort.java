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
public class BierenVanEenSoort {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT_SOORT_MET_NAAM = "select id from soorten where naam = ?";
    private static final String SELECT_BIEREN_VAN_EEN_SOORT = 
            "select naam from bieren where soortid = ?";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Soortnaam: ");
        String soortNaam = new Scanner(System.in).nextLine();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statementSoort = connection.prepareStatement(SELECT_SOORT_MET_NAAM)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            statementSoort.setString(1, soortNaam);
            int soortId = 0;
            try (ResultSet resultSetSoort = statementSoort.executeQuery()) {
                if(resultSetSoort.next()){
                    soortId = resultSetSoort.getInt("id");
                }
            } 
            if(soortId == 0){
                System.out.println("Niet gevonden");
            }else{
                try(PreparedStatement statementBieren = connection.prepareStatement(SELECT_BIEREN_VAN_EEN_SOORT)){
                    statementBieren.setInt(1, soortId);
                    try(ResultSet resultSetBieren = statementBieren.executeQuery()){
                        while(resultSetBieren.next()){
                            System.out.println(resultSetBieren.getString("naam"));
                        }
                    }
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
