package org.cheepskiesdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String url = "jdbc:mysql://cis3270project.mysql.database.azure.com:3306/cheepskies";

    private static final String user = "cis3270user";

    private static final String password = "Password!";


    public static Connection dbConnect() throws SQLException {

        return DriverManager.getConnection(url, user, password);

    }
}
