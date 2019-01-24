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
import java.time.LocalDate;

class Werknemer {

    private long id;
    private String voornaam;
    private String familienaam;
    private LocalDate geboorte;
    private LocalDate inDienst;

    public Werknemer(long id, String voornaam, String familienaam, LocalDate geboorte, LocalDate inDienst) {
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.geboorte = geboorte;
        this.inDienst = inDienst;
    }

    @Override
    public String toString() {
        return voornaam + ' ' + familienaam;
    }

    public boolean isJarig() {
        LocalDate vaandag = LocalDate.now();
        return geboorte.getMonth() == vaandag.getMonth() && geboorte.getDayOfMonth() == vaandag.getDayOfMonth();
    }
}

/**
 *
 * @author andras.kovacs
 */
public class JDBCenOOMain {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select id, voornaam, familienaam, geboorte, indienst from werknemers ";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery(SELECT)) {
                while (resultSet.next()) {
                    Werknemer werknemer = new Werknemer(resultSet.getLong("id"),
                            resultSet.getString("voornaam"), resultSet.getString("familienaam"),
                            resultSet.getDate("geboorte").toLocalDate(), resultSet.getDate("indienst").toLocalDate());
                    System.out.println(werknemer);
                    if(werknemer.isJarig()){
                        System.out.print(" JARIG");
                    }
                    System.out.println();
                }
            }
            connection.commit();
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }

}
