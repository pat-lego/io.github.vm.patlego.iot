package io.github.vm.patlego.iot.server.filters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;
import io.github.vm.patlego.iot.server.users.SimpleUserManager;
import io.github.vm.patlego.iot.server.users.UserManager;
import io.github.vm.patlego.iot.server.utils.WebAppHelper;

public class AuthenticationFilter implements Filter {

    private final String URL = "/iot/patlego/login.html";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        UserManager userManager = new SimpleUserManager();

        // Check to see if we should filter the request
        if (this.shouldFilter(new URL(httpRequest.getRequestURL().toString()))) {
            String authHeader = getQueryToken(httpRequest.getQueryString());

            if (null != authHeader) {
                Authentication<Jwt> authentication = WebAppHelper.getService(
                        (BundleContext) request.getServletContext().getAttribute("osgi-bundlecontext"),
                        Authentication.class);
                Jwt jwt = null;

                // Validate the authenticity of the authHeader
                try {
                    jwt = authentication.validate(authHeader);
                } catch (Exception e) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    logger.info("Current date time " + dtf.format(now));

                    logger.error(e.getMessage(), e);
                    httpResponse.setHeader("X-Authentication-Failure-Cause", "Invalid Token");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }

                // Permissions check
                if (jwt == null || !hasPermissions(userManager.getPermissions(jwt.getSub()).getPermissions(),
                        httpRequest.getServletPath())) {
                    httpResponse.setHeader("X-Authorization-Failure-Cause", "Invalid permissions");
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    chain.doFilter(request, response);
                }

            } else {
                httpResponse.setHeader("X-Authentication-Failure-Cause", "No Token Provided");
                httpResponse.sendRedirect(this.URL);
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    private boolean hasPermissions(String permissions, String url) {
        List<String> permList = Arrays.asList(permissions.split(","));
        List<String> filteredPerms = permList.stream().filter(perm -> url.matches(perm)).collect(Collectors.toList());

        if (!filteredPerms.isEmpty()) {
            return true;
        }

        return false;
    }

    private String getQueryToken(String queryParams) {
        if (null == queryParams) {
            return null;
        }

        Optional<String> token = Arrays.stream(queryParams.split("&")).filter(entry -> entry.contains("token"))
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
