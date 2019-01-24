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
import java.sql.Statement;

/**
 *
 * @author andras.kovacs
 */
public class OptimalAansprekenMain {
//####################TRAAG------------TRAAG------------TRAAG-----------TRAAG--------------TRAAG################\\
    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT_RODE_PLANTEN = 
            "select naam, leverancierid from planten where kleur = 'rood'";
    private static final String SELECT_LEVERANCIER = 
            "select naam from leveranciers where id = ?";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statementPlanten = connection.createStatement();
                PreparedStatement statementLeverancier = connection.prepareStatement(SELECT_LEVERANCIER)){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSetPlanten = statementPlanten.executeQuery(SELECT_RODE_PLANTEN)){
                while(resultSetPlanten.next()){
                    System.out.print(resultSetPlanten.getString("naam") + ' ');
                    statementLeverancier.setLong(1, resultSetPlanten.getLong("leverancierid"));
                    try(ResultSet resultSetLeverancier = statementLeverancier.executeQuery()){
                        System.out.println(resultSetLeverancier.next() ?
                                resultSetLeverancier.getString("naam"):"leverancier niet gevonden");
                    }
                }
            }
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }

}
