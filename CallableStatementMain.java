/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author andras.kovacs
 */
public class CallableStatementMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false" +
            "&noAccessToProcedureBodies=true";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String CALL = "{call PlantenMetEenWoord(?)}";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Woord: ");
        String woord = new Scanner(System.in).nextLine();
        try(Connection connection = DriverManager.getConnection(URL,USER, PASSWORD);
                CallableStatement statement = connection.prepareCall(CALL)){
            statement.setString(1, '%' + woord + '%');
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getString("naam"));
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }

}
