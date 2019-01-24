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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author andras.kovacs
 */
public class DateTime1Main {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select indienst, voornaam, familienaam" + 
            " from werknemers where indienst >= ? order by indienst";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("Datum vanaf (dd/mm/yyyy):");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y");
        LocalDate datum = LocalDate.parse(new Scanner(System.in).nextLine(), formatter);
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(SELECT)){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement.setDate(1, java.sql.Date.valueOf(datum));
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        System.out.println(resultSet.getDate("indienst") + " " +
                                resultSet.getString("voornaam") + " " +
                                resultSet.getString("familienaam"));
                    }
                }
            connection.commit();                    
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
    
}
