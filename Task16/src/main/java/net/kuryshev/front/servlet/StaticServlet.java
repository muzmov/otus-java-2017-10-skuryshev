package net.kuryshev.front.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticServlet extends HttpServlet {
    private static final String RESOURCE_BASE = "/WEB-INF/public_html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String address = request.getRequestURI().replaceAll(request.getContextPath(), "");
        InputStream is = getClass().getResourceAsStream(RESOURCE_BASE + address);
        OutputStream os = response.getOutputStream();
        if (is != null) {
            int count;
            byte[] b = new byte[1024];
            while ((count = is.read(b)) > 0) {
                os.write(b, 0, count);
            }
            is.close();
        }
        os.flush();
    }
}