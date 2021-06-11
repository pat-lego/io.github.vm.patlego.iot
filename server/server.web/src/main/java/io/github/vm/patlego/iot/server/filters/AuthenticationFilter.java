package io.github.vm.patlego.iot.server.filters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

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

        if (this.shouldFilter(new URL(httpRequest.getRequestURL().toString()))) {
            String authHeader = getQueryToken(httpRequest.getQueryString());
            if (null != authHeader) {
                Authentication<Jwt> authentication = WebAppHelper.getService(
                        (BundleContext) request.getServletContext().getAttribute("osgi-bundlecontext"),
                        Authentication.class);
                try {
                    authentication.validate(authHeader);
                    chain.doFilter(request, response);
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

    private String getQueryToken(String queryParams) {
        if (null == queryParams) {
            return null;
        }

        Optional<String> token = Arrays.stream(queryParams.split("&"))
            .filter(entry -> entry.contains("token"))
            .findFirst();
        if (token.isPresent()) {
            return token.get().split("=")[1];
        } else {
            return null;
        }
        
    }

    private boolean shouldFilter(URL url) {
        String path = url.getPath();
        if (path.endsWith("events.html")) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
