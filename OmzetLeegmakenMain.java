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
public class OmzetLeegmakenMain {

    private static final String URL
            = "jdbc:mysql://localhost/bieren?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String BEGIN_SELECT = "select id, naam, omzet from brouwers where id in (";

    /**
     * @param args the command line arguments
     */
    private static Set<Integer> vraagIds (){
        Set<Integer> nummers = new LinkedHashSet<>();
        Scanner scanner = new Scanner(System.in);
        for (int nummer; (nummer = scanner.nextInt()) != 0;) {
            if (nummer < 0) {
                System.out.println("brouwersnummer moet positief zijn");
            } else if (nummers.contains(nummer)) {
                System.out.println("Nummer al in de lijst");
            } else {
                nummers.add(nummer);
            }
        }
        return nummers;
    }
    private static void nietGevondenIds(Set<Integer> ids, Connection connection)
    throws SQLException {
        StringBuffer selectSQL = new StringBuffer(BEGIN_SELECT);
        for(int id : ids){
            selectSQL.append("?,");
        }
        selectSQL.setCharAt(selectSQL.length() - 1, ')');
        try(PreparedStatement statement = connection.prepareStatement(selectSQL.toString())){
            int index = 1;
            for(int id : ids){
                statement.setInt(index++, id);
            }
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    ids.remove(resultSet.getInt("id"));
                }
            }
            System.out.print("Volgende nummers werden niet gevonden:");
            for(int id : ids){
                System.out.print(id + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        System.out.println("Tik brouwernummer, einding met 0:");
        Set<Integer> nummers = vraagIds();
        StringBuilder select = new StringBuilder(BEGIN_SELECT);
        for (int teller = 0; teller != nummers.size(); teller++) {
            select.append("?,");
        }
        select.setCharAt(select.length() - 1, ')');
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(select.toString())) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            int index = 1;
            for (int nummer : nummers) {
                statement.setInt(index++, nummer);
                String strIndex = Integer.toString(index);
                System.out.println("index++: " + strIndex + " ;nummer: " + nummer);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
//                    int id = resultSet.getInt("id");
                    System.out.println(resultSet.getInt("id") + " " + resultSet.getString("naam"));
//                    String naam = resultSet.getString("naam");
//                    System.out.print(resultSet.getString(' ' + "naam") + ' ');
                    long omzet = resultSet.getLong("omzet");
                    System.out.println(resultSet.wasNull() ? "onbekend omzet" : omzet);
                    System.out.println();
                }
            }catch(Exception ex){
               // System.out.println("brouwersnummer niet gevonden ");
            }
                    nietGevondenIds(nummers, connection);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

    }
}
