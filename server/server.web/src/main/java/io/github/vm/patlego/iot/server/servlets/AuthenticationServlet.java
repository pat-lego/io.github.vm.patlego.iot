package io.github.vm.patlego.iot.server.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.apache.cxf.helpers.IOUtils;
import org.osgi.framework.BundleContext;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;
import io.github.vm.patlego.iot.server.users.SimpleUserManager;
import io.github.vm.patlego.iot.server.users.User;
import io.github.vm.patlego.iot.server.utils.WebAppHelper;

public class AuthenticationServlet extends HttpServlet {

    private static final String ISS = "patlego-vm IoT module";
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User requestUser = gson.fromJson(IOUtils.toString(req.getInputStream(), "UTF-8"), User.class);

        try(OutputStream out = resp.getOutputStream()) {
            if (null == requestUser.getUsername() || null == requestUser.getPassword()) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                Security security = WebAppHelper.getService((BundleContext) req.getServletContext().getAttribute("osgi-bundlecontext"), Security.class);
                SimpleUserManager userManager = new SimpleUserManager();
                if (userManager.userExists(requestUser.getUsername())) {
                    User user = userManager.getUser(requestUser.getUsername());
                    if (requestUser.getPassword().equals(security.decrypt(user.getPassword()))) {
                        Authentication<Jwt> jwtAuthentication = WebAppHelper.getService((BundleContext) req.getServletContext().getAttribute("osgi-bundlecontext"), Authentication.class);
                        
                        Jwt jwt = new Jwt();
                        jwt.setExp(Date.from(LocalDateTime.now().plusHours(1L).atZone(ZoneId.of("America/New_York")).toInstant()));
                        jwt.setIat(Date.from(LocalDateTime.now().atZone(ZoneId.of("America/New_York")).toInstant()));
                        jwt.setIss(ISS);
                        jwt.setSub("IoT System Console");
                        jwt.setAud("IoT Auth Token");

                        String jwtToken = jwtAuthentication.createToken(jwt);

                        AuthResponse authResponse = new AuthResponse();
                        authResponse.setToken(jwtToken);

                        out.write(gson.toJson(authResponse).getBytes());
                    } else {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }        
    }

}
