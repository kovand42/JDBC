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
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author andras.kovacs
 */
public class OptimaalGebruikMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String BEGIN_SELECT = "select id, naam from planten where id in (";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Tik plantnummers, einding met 0");
        Set<Long> nummers = new LinkedHashSet<>();
        Scanner scanner = new Scanner(System.in);
        for(long nummer; (nummer = scanner.nextLong()) !=0;){
            nummers.add(nummer);
        }
        StringBuilder select = new StringBuilder(BEGIN_SELECT);
        for(int teller = 0; teller != nummers.size(); teller++){
            select.append("?,");
        }
        select.setCharAt(select.length()-1, ')');//  "0" naar ")"
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(select.toString())){
            int index = 1;
            for(long nummer : nummers){
                statement.setLong(index++, nummer);
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getLong("id") + ":" + 
                            resultSet.getString("naam"));
                }
            }
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }

}
