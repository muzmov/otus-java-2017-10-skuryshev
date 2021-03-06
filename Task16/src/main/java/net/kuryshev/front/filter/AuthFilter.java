package net.kuryshev.front.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        System.out.println("AuthFilter path:" + path);
        if (isAccessiblePath(httpRequest, path)) chain.doFilter(request, response);
        else {
            if (!isAuthorized(httpRequest)) httpResponse.sendRedirect(httpRequest.getContextPath() + "/signin.html");
            else chain.doFilter(request, response);
        }
    }

    private boolean isAccessiblePath(HttpServletRequest httpRequest, String path) {
        return path.equalsIgnoreCase(httpRequest.getContextPath() + "/login")
                || path.equalsIgnoreCase(httpRequest.getContextPath() + "/signin.html");
    }

    private boolean isAuthorized(HttpServletRequest httpRequest) {
        boolean authorized = Objects.equals(httpRequest.getSession().getAttribute("authorized"), true);
        System.out.println("Authorized " + authorized);
        return authorized;
    }

    @Override
    public void destroy() {
    }
}
