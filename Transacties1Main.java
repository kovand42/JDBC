/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andras.kovacs
 */
public class Transacties1Main {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String UPDATE_PRIJS_10_PROCENT = 
            "update planten set verkoopprijs = verkoopprijs * 1.1 where verkoopprijs >= 100";
    private static final String UPDATE_PRIJS_5_PROCENT =
            "update planten set verkoopprijs = verkoopprijs * 1.05 where verkoopprijs < 100";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);
            statement.executeUpdate(UPDATE_PRIJS_10_PROCENT);
            statement.executeUpdate(UPDATE_PRIJS_5_PROCENT);
            connection.commit();
        }catch(SQLException ex){
                ex.printStackTrace(System.err);
                }
    }

}
