package com.example.kurs;

import java.sql.*;

public class DBConnector {

    public static String urlConnection = "jdbc:mysql://127.0.0.1:3306/kur";
    public static String userConnection = "root";
    public static String passwordConnection = "";
    public static Connection connection;


    public static ResultSet executeQuery(String query) throws SQLException {
        connection = DriverManager.getConnection(urlConnection, userConnection, passwordConnection);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static void updateQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    public static void insertQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

}