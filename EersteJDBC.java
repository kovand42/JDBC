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
import java.util.Scanner;

/**
 *
 * @author andras.kovacs
 */
public class EersteJDBC {

    private static final String URL = "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = "select naam from leveranciers where woonplaats = ?";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Woonplaats: ");
        String woonplaats = new Scanner(System.in).nextLine();
        try (Connection connection
                = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setString(1, woonplaats);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getString("naam"));
                }
                
//                int kinderen = resultSet.getInt("aantalKinderen");
//                System.out.println(resultSet.wasNull() ? "onbekend"  : kinderen);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

}
