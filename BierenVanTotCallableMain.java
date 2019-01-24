/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.math.BigDecimal;
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
public class BierenVanTotCallableMain {

        private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false" +
            "&noAccessToProcedureBodies=true";
        private static final String USER = "cursist";
        private static final String PASSWORD = "cursist11.";
        private static final String CALL = "{call BierenVanTotAlcohol(?,?)}";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Minimum alcohol%: ");
        BigDecimal minimum = scanner.nextBigDecimal();
        System.out.println("Maximum alcohol%: ");
        BigDecimal maximum = scanner.nextBigDecimal();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                CallableStatement statement = connection.prepareCall(CALL)){
            statement.setBigDecimal(1, minimum);
            statement.setBigDecimal(2, maximum);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getString("biernaam") + " " + 
                            resultSet.getString("brouwernaam") + " " + 
                            resultSet.getBigDecimal("alcohol"));
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
        
    }
    
}
