package net.kuryshev.front.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sergey.Kuryshev on 09.02.2018
 */
public class LoginServlet extends HttpServlet {

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (isCorrectCredentials(request)) {
            request.getSession().setAttribute("authorized", true);
            response.sendRedirect(request.getContextPath() + "/cache.html");
        }
        else
            response.sendRedirect(request.getContextPath() + "/signin.html");

    }

    private boolean isCorrectCredentials(HttpServletRequest request) {
        return LOGIN.equalsIgnoreCase(request.getParameter("login")) && PASSWORD.equalsIgnoreCase(request.getParameter("password"));
    }
}
