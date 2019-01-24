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
public class IsolationMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select id from soorten where naam = ?";
    private static final String INSERT = "insert into soorten(naam) values (?)";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Soortnaam: ");
        String soortNaam = new Scanner(System.in).nextLine();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statementSelect = connection.prepareStatement(SELECT);
                PreparedStatement statementInsert = connection.prepareStatement(INSERT)){
            statementSelect.setString(1, soortNaam);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try{
                statementInsert.executeUpdate();
                connection.commit();
            }catch(SQLException ex){
                statementSelect.setString(1, soortNaam);
                try(ResultSet resultSet = statementSelect.executeQuery()){
                    if(resultSet.next()){
                        System.out.println("Soort met deze naam bestaat al");
                    }else{
                        ex.printStackTrace(System.err);
                    }
                }    
            }
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}
