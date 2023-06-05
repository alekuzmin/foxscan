package ru.foxscan.servlet;

import ru.foxscan.work.StandStatusUtils;

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


        String masterSystem = "" + request.getParameter("master");
        if (masterSystem.equals("null")||masterSystem.equals("")){
            masterSystem = "EPK";
        }

        String masterValue = "ЕПК";
        if(masterSystem.equals("EPC")){masterValue = "ППРБ.ЕПС";}
        if(masterSystem.equals("auth")){masterValue = "AUTH";}
        if(masterSystem.equals("PPRB.CS")){masterValue = "ППРБ.КС";}

        String statDataAll = "[]";
        try {
            statDataAll = StandStatusUtils.getStatDataAll(masterSystem);
        } catch (ParseException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        request.setAttribute("master", masterValue);
        request.setAttribute("statDataAll", statDataAll);
        request
                .getRequestDispatcher("/WEB-INF/views/mon.jsp")
                .forward(request, response);
    }


}
