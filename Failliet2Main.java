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
public class Failliet2Main {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String UPDATE_NAAR_BROUWER3
            = "update bieren set brouwerid = 3 where brouwerid = 2 and alcohol >= 8.5";
    private static final String UPDATE_NAAR_BROUWER4
            = "update bieren set brouwerid = 4 where brouwerid = 2";
    private static final String DELETE_BROUWER2
            = "delete from brouwers where id = 2";    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            statement.addBatch(UPDATE_NAAR_BROUWER3);
            statement.addBatch(UPDATE_NAAR_BROUWER4);
            statement.addBatch(DELETE_BROUWER2);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            //statement.executeBatch();
            int[]aantalGewijzigdeRecordsPerUpdate = statement.executeBatch();
            System.out.println(aantalGewijzigdeRecordsPerUpdate[0] + 
                    " bieren naar brouwer3 gestuurd");
            System.out.println(aantalGewijzigdeRecordsPerUpdate[1] +
                    " bieren naar brouwer4 gestuurd");
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }    
}
