package ru.foxscan.servlet;

import ru.foxscan.work.StandStatusUtils;
import ru.foxscan.work.Worker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet("/mon")
public class BaseServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        String result;
//        try {
//            result = Worker.getTimeTable("EPK");
//        } catch (SQLException | ParseException e) {
//            throw new RuntimeException(e);
//        }


        String statDataAll = "[]";
        try {
            statDataAll = StandStatusUtils.getStatDataAll("EPK");
        } catch (ParseException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        System.out.println(statDataAll);
            request.setAttribute("statDataAll", statDataAll);


        request
                .getRequestDispatcher("/WEB-INF/views/mon.jsp")
                .forward(request, response);
    }


}
