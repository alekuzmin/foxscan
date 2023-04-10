package ru.foxscan.base;

import java.sql.*;

public class DBUtils {
    private static final String urlDb = "jdbc:sqlite:C:/Users/ALEX/IdeaProjects/foxscan/src/main/resources/monDB.db";
    private static final String className = "org.sqlite.JDBC";
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;


    public static void writeResponseResult(String serviceName, String masterSystem, int statusCode, int leadTime) {
        String sql = "INSERT INTO mon (service_name, master_system, status_code, lead_time) VALUES " +
                "('" + serviceName + "', '" + masterSystem + "', " + statusCode + ", " + leadTime + ");";
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void openConnection(){
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(urlDb);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
