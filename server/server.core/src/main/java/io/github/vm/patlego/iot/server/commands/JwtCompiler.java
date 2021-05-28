package io.github.vm.patlego.iot.server.commands;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;

@Service
@Command(scope = "iot", name = "token", description = "Generate a token for the IoT endpoint")
public class JwtCompiler implements Action {

    private static final String ISS = "patlego-vm IoT module";

    @Argument(index = 0, name = "aud", description = "The JWT recipient to set", required = true, multiValued = false)
    public String aud = StringUtils.EMPTY;

    @Argument(index = 1, name = "sub", description = "The JWT subject (the user)", required = true, multiValued = false)
    public String subject = StringUtils.EMPTY;

    @Reference
    private Authentication<Jwt> jwtAuthentication;

    @Override
    public Object execute() throws Exception {

        Jwt token = new Jwt();
        
        token.setAud(aud);
        token.setExp(Date.from(LocalDateTime.now().plusMonths(1L).atZone(ZoneId.of("America/New_York")).toInstant()));
        token.setIat(Date.from(LocalDateTime.now().atZone(ZoneId.of("America/New_York")).toInstant()));
        token.setIss(ISS);
        token.setSub(subject);

        return jwtAuthentication.createToken(token);
    }
    
}
