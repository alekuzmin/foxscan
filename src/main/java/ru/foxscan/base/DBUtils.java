package ru.foxscan.base;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils implements AutoCloseable{
    private static final String urlDb = "jdbc:sqlite:/Users/16701823/IdeaProjects/foxscan/src/main/resources/monDB.db";
    private static final String className = "org.sqlite.JDBC";
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;


    public static void writeResponseResult(String serviceName, String masterSystem, String success, int statusCode, int leadTime) {

        String status = "Normal";

        if (statusCode != 200) {
            status = "Error";
        }
        if(statusCode == 200 && success.equals("false")){
            status = "Disconnect";
        }

        try {
            writeStatusLog(masterSystem, status);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql_mon = "INSERT INTO mon (service_name, master_system, success, status_code, lead_time) VALUES " +
                "('" + serviceName + "', '" + masterSystem + "', '" + success + "', " + statusCode + ", " + leadTime + ");";
        try {
            statement.execute(sql_mon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeStatusLog(String masterSystem, String status) throws SQLException {

        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long curMillis = System.currentTimeMillis();
        String start = sdfFull.format(new Date(curMillis));
        String end = sdfFull.format(new Date(curMillis + (long) 5 * 60 * 1000));

        String sql_insert_full = "INSERT INTO status_log (master_system, status, start, end) VALUES " +
                "('" + masterSystem + "', '" + status + "', '" + start + "', '" + end +"');";

        String sql_update = "UPDATE status_log SET end = '" + start + "' WHERE id in (select id FROM status_log WHERE master_system = '" + masterSystem + "' ORDER BY id DESC LIMIT 1);";


            if (isStatusOrDayDifferent(masterSystem, status, start)){
                statement.execute(sql_insert_full);
            } else {
                statement.execute(sql_update);
            }
    }

    public static Boolean isStatusOrDayDifferent(String masterSystem, String status, String start){
        String sql = "SELECT status, start FROM status_log WHERE master_system = '" + masterSystem + "' ORDER BY id DESC LIMIT 1;";
        try {
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                return true;
            } else {
                return !resultSet.getString("status").equals(status) || !resultSet.getString("start").split(" ")[0].equals(start.split(" ")[0]);
            }
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


    @Override
    public void close() throws Exception {
        System.out.println("close connection");
            connection.close();
            statement.close();
    }
}
