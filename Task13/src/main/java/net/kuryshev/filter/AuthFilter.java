package net.kuryshev.filter;

import javax.servlet.*;
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
        if (path.startsWith("/login") || path.startsWith("/signin.html")) chain.doFilter(request, response);
        else {
            if (!isAuthorized(httpRequest)) httpResponse.sendRedirect("/signin.html");
            else chain.doFilter(request, response);
        }
    }

    private boolean isAuthorized(HttpServletRequest httpRequest) {
        return Objects.equals(httpRequest.getSession().getAttribute("authorized"), true);
    }

    @Override
    public void destroy() {
    }
}
