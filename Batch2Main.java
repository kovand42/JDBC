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
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author andras.kovacs
 */
public class Batch2Main {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String UPDATE = 
            "update planten set verkoopprijs = verkoopprijs * (1 + ? / 100)" +
            " where verkoopprijs between ? and ?";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("% voor prijzen kleiner dan 100: ");
        BigDecimal percentageKleinerDan100 = scanner.nextBigDecimal();
        System.out.println("% voor prijzen vanaf 100: ");
        BigDecimal percentageVanaf100 = scanner.nextBigDecimal();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(UPDATE)){
            statement.setBigDecimal(1, percentageKleinerDan100);
            statement.setBigDecimal(2, BigDecimal.ZERO);
            statement.setBigDecimal(3, BigDecimal.valueOf(99.99));
            statement.addBatch();
            statement.setBigDecimal(1, percentageVanaf100);
            statement.setBigDecimal(2,BigDecimal.valueOf(100));
            statement.setBigDecimal(3, BigDecimal.valueOf(999_999));
            statement.addBatch();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            int[] aantalGewijzigdeRecordsPerUpdate = statement.executeBatch();
            System.out.println(aantalGewijzigdeRecordsPerUpdate[0] + 
                    " planten verhoogd met " + percentageKleinerDan100 + "%");
            System.out.println(aantalGewijzigdeRecordsPerUpdate[1] + 
                    " planten verhoogd met " + percentageVanaf100 + "%");
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
    
}
