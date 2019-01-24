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
public class OmzetMain {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select br.naam, count(bi.id) as aantal_bieren from brouwers as br "
            + "inner join bieren as bi on br.id = bi.brouwerid where omzet is null"
            + " group by br.naam";
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
                    System.out.println(resultSet.getString("naam") + ' ' +
                            resultSet.getInt("aantal_bieren"));
                }
            }
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }
    }

}
