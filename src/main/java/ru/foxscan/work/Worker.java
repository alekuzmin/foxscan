package ru.foxscan.work;

import ru.foxscan.base.DBUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Worker extends DBUtils{

    public static String getTimeTable(String masterSystem) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return getTimeTable(masterSystem, sdf.format(date));
    }

    public static String getTimeTable(String masterSystem, String date) throws ParseException, SQLException {
        StringBuilder result = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = sdf.parse(date);
        long millis = current_date.getTime();
        result.append("<tr>");
        openConnection();
        for(int i = 0; i < 1440; i = i+5) {
            Date from = new Date(millis + (long) i * 1000 * 60);
            Date to = new Date(millis + (long) (i + 5) * 1000 * 60);
            String sql = "SELECT status_code FROM mon WHERE master_system = '" + masterSystem + "' AND moment BETWEEN strftime('%Y-%m-%d %H:%M:%S','" + sdfFull.format(from) + "') AND strftime('%Y-%m-%d %H:%M:%S','" + sdfFull.format(to) + "') LIMIT 1;";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                result.append("<td></td>");
            } else {
                int statusCode = resultSet.getInt("status_code");
                if (statusCode == 200) {
                    result.append("<td bgcolor=\"green\"></td>");
                } else {
                    result.append("<td bgcolor=\"red\"></td>");
                }
            }
        }
        result.append("</tr>");
        return result.toString();
    }
}
