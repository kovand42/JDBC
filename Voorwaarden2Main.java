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
public class Voorwaarden2Main {

    private static final String URL
            = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT
            = "select verkoopprijs from planten where id = ?";
    private static final String UPDATE = "update planten set verkoopprijs = ? where id = ? "
            + "and ? > verkoopprijs / 2";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nummer plant: ");
        long id = scanner.nextLong();
        System.out.println("Nieuwe prijs:");
        BigDecimal nieuwePrijs = scanner.nextBigDecimal();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statementUpdate = connection.prepareStatement(UPDATE);
                PreparedStatement statementSelect = connection.prepareStatement(SELECT)) {
            statementUpdate.setBigDecimal(1, nieuwePrijs);
            statementUpdate.setLong(2, id);
            statementUpdate.setBigDecimal(3, nieuwePrijs);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            int aantalAngepasteRecords = statementUpdate.executeUpdate();
            if (aantalAngepasteRecords == 0) {
                statementSelect.setLong(1, id);
                try (ResultSet resultSet = statementSelect.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Nieuw prijs te laag");
                    } else {
                        System.out.println("Plant niet gevonden");
                    }
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
