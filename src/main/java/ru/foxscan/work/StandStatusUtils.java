package ru.foxscan.work;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class StandStatusUtils {

    private static final String urlDb = "jdbc:sqlite:/Users/16701823/IdeaProjects/foxscan/src/main/resources/monDB.db";
    private static final String className = "org.sqlite.JDBC";


    public static String getYaxisNames() {

        StringBuilder builder = new StringBuilder();
        long curMillis = System.currentTimeMillis();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");

        for (int i = 2; i < 7; i++) {
            builder.append("\"");
            builder.append(formatDate.format(new Date(curMillis - i * 24 * 60 * 60 * 1000)));
            builder.append("\"");
            if (i != 6) builder.append(",");
        }
        return builder.toString();
    }

    //Формируем массив с датой последних 7-и дней
    public static String[] getLastDaysArr() {

        long curMillis = System.currentTimeMillis();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");
        String[] result = new String[7];
        for (int i = 0; i < result.length; i++) {
            result[i] = formatDate.format(new Date(curMillis - i * 24 * 60 * 60 * 1000));
        }
        return result;

    }

    public static String getStatDataAll(String masterSystem) throws IOException, ParseException, SQLException, ClassNotFoundException {

        String[] lines = getLastDaysArr();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {

            sb.append(getStatDataByDate2(lines[i], i, masterSystem));
        }

        return sb.toString();


    }

    public static String getStatDataByDate2(String day, int i, String masterSystem) throws IOException, SQLException, ClassNotFoundException {
        //String[] lines = getLogData();
        String[] lines = getStatusLogData(masterSystem);

        StringBuilder sb = new StringBuilder();
        StringBuilder standInData = new StringBuilder();
        StringBuilder disconnectData = new StringBuilder();
        StringBuilder normalData = new StringBuilder();
        for (int z = 0; z < lines.length; z++) {
            String[] line = lines[z].split(" - ");
            if (line[0].substring(0, 5).equals(day) && line[1].equals("N")) {
                standInData.append("[");
                standInData.append(getDecimalTime(line[2]) + "," + getDecimalTime(line[0]));
                standInData.append(",'");
                standInData.append(line[2].substring(6, 11) + " - " + line[0].substring(6, 11) + "'],");
            }
            if (line[0].substring(0, 5).equals(day) && line[1].equals("Z")) {
                disconnectData.append("[");
                disconnectData.append(getDecimalTime(line[2]) + "," + getDecimalTime(line[0]));
                disconnectData.append(",'");
                disconnectData.append(line[2].substring(6, 11) + " - " + line[0].substring(6, 11) + "'],");
            }
            if (line[0].substring(0, 5).equals(day) && line[1].equals("Y")) {
                normalData.append("[");
                normalData.append(getDecimalTime(line[2]) + "," + getDecimalTime(line[0]));
                normalData.append(",'");
                normalData.append(line[2].substring(6, 11) + " - " + line[0].substring(6, 11) + "'],");
            }
        }

        if (!standInData.toString().equals("")) {
            sb.append("formStatusRect([");
            sb.append(standInData);
            sb.append("],");
            sb.append(i);
            sb.append(",");
            sb.append("'standIn'");
            sb.append(");\n");
        }
        if (!disconnectData.toString().equals("")) {
            sb.append("formStatusRect([");
            sb.append(disconnectData);
            sb.append("],");
            sb.append(i);
            sb.append(",");
            sb.append("'disconnect'");
            sb.append(");\n");
        }
        if (!normalData.toString().equals("")) {
            sb.append("formStatusRect([");
            sb.append(normalData);
            sb.append("],");
            sb.append(i);
            sb.append(",");
            sb.append("'normal'");
            sb.append(");\n");
        }

        return sb.toString();
    }

    public static String getDecimalTime(String s) {

        int h = Integer.parseInt(s.substring(6, 8));
        double l = Integer.parseInt(s.substring(9, 11)) / 60.0;
        return Objects.toString(h + l, null);
    }


    //29/10 18:11:47 - Y - 29/10 17:18:33
    public static String[] getStatusLogData(String masterSystem) throws ClassNotFoundException, SQLException {
        StringBuilder result = new StringBuilder();
        Class.forName(className);
        Connection connection = DriverManager.getConnection(urlDb);
        Statement statement = connection.createStatement();
        String sql = "SELECT start, end, status FROM status_log WHERE master_system = '" + masterSystem + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String[] startDateTime = resultSet.getString("start").split(" ");
            String[] startDate = startDateTime[0].split("-");
            String startDateFormat = startDate[2] + "/" + startDate[1];
            String endTime = resultSet.getString("end").split(" ")[1];
            String startDateTimeFormat = startDateFormat + " " + startDateTime[1];
            String endDateTimeFormat = startDateFormat + " " + endTime;
            String status = "Y";
            if(resultSet.getString("status").equals("Disconnect")){
                status = "Z";
            }
            if(resultSet.getString("status").equals("Error")){
                status = "N";
            }
            result.append(endDateTimeFormat).append(" - ").append(status).append(" - ").append(startDateTimeFormat).append(" - null").append("!");
        }
        connection.close();
        statement.close();
        resultSet.close();
        //System.out.println(result);
        return result.toString().split("!");

    }

}
