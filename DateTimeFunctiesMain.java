/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andras.kovacs
 */
public class DateTimeFunctiesMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = 
            "select geboorte, voornaam, familienaam from werknemers " +
            "where {fn month(geboorte)} = {fn month({fn curdate()})} " +
            "order by {fn dayofmonth(geboorte)}";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery(SELECT)){
                while(resultSet.next()){
                    System.out.println(resultSet.getDate("geboorte") + " " + 
                            resultSet.getString("voornaam") + " " + 
                            resultSet.getString("familienaam"));
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }    
}
