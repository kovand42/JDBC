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
public class FaillietMain {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String UPDATE_NAAR_BROUWER2
            = "update bieren set brouwerid = 2 where brouwerid = 1 and alcohol >= 8.5";
    private static final String UPDATE_NAAR_BROUWER3
            = "update bieren set brouwerid = 3 where brouwerid = 1";
    private static final String DELETE_BROUWER1
            = "delete from brouwers where id = 1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(UPDATE_NAAR_BROUWER2);
            statement.executeUpdate(UPDATE_NAAR_BROUWER3);
            statement.executeUpdate(DELETE_BROUWER1);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
