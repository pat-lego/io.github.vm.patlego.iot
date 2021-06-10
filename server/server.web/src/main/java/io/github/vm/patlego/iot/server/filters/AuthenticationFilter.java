package io.github.vm.patlego.iot.server.filters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;
import io.github.vm.patlego.iot.server.utils.WebAppHelper;

public class AuthenticationFilter implements Filter {

    private final String URL = "/iot/patlego/login.html";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authentication");

        if (this.shouldFilter(new URL(httpRequest.getRequestURL().toString()))) {
            if (null != authHeader) {
                Authentication<Jwt> authentication = WebAppHelper.getService(
                        (BundleContext) request.getServletContext().getAttribute("osgi-bundlecontext"),
                        Authentication.class);
                try {
                    authentication.validate(authHeader);
                } catch (Exception e) {
                    httpResponse.setHeader("X-Authentication-Failure-Cause", "Expired Token");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }

            } else {
                httpResponse.setHeader("X-Authentication-Failure-Cause", "No Token Provided");
                httpResponse.sendRedirect(this.URL);
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    private boolean shouldFilter(URL url) {
        String path = url.getPath();
        if (path.contains(".js") || path.contains(".css")) {
            return Boolean.FALSE;
        }

        if ("/iot/patlego/login.html".equals(path) || "/iot/patlego/servlet/authenticate.action".equals(path)) {
            return Boolean.FALSE;
        }

        if (path.contains("/iot/patlego")) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
