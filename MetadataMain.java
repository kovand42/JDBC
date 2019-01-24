/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eerstejdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andras.kovacs
 */
public class MetadataMain {

    private static final String URL = 
            "jdbc:mysql://localhost/tuincentrum?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist11.";
    private static final String SELECT = 
            "select id, voornaam, indienst from werknemers ";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT)){
            ResultSetMetaData metaData = resultSet.getMetaData();
            for(int index = 1; index <= metaData.getColumnCount(); index++){
                System.out.println(metaData.getColumnName(index) + ' ' +
                        metaData.getColumnTypeName(index));
            }
            DatabaseMetaData metadata = connection.getMetaData();
            System.out.println(metadata.getDriverName() + ' ' +
                    metadata.getDriverMajorVersion() + ' ' + 
                    metadata.getDriverMinorVersion() +"\n" +
                    metadata.getDatabaseProductName() + ' ' +
                    metadata.getDatabaseMajorVersion() + ' ' +
                    metadata.getDatabaseMinorVersion());
        }catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
    
}
