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
public class VoorwaardenMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = 
            "select verkoopprijs from planten where id = ? for update";
    private static final String UPDATE = "update planten set verkoopprijs = ? where id = ?";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nummer plant: ");
        long id = scanner.nextLong();
        System.out.println("Nieuwe prijs:");
        BigDecimal nieuwePrijs = scanner.nextBigDecimal();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statementSelect = connection.prepareStatement(SELECT);
                PreparedStatement statementUpdate = connection.prepareStatement(UPDATE)){
            statementSelect.setLong(1, id);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statementSelect.executeQuery()){
                if(resultSet.next()){
                    if(nieuwePrijs.compareTo(resultSet.getBigDecimal("verkoopprijs")
                    .multiply(BigDecimal.valueOf(0.5)))<0){
                        System.out.println("Nieuwe prijs te laag");
                    }else{
                        statementUpdate.setBigDecimal(1, nieuwePrijs);
                        statementUpdate.setLong(2, id);
                        statementUpdate.executeUpdate();
                        connection.commit();
                    }
                }else{
                    System.out.println("Plant niet gevonden");
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }    
}
