package io.github.vm.patlego.iot.server.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;

@Service
@Command(scope = "iot", name = "validate", description = "Generate a token for the IoT endpoint")
public class JwtDecompiler implements Action {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private String pattern = "MM/dd/yyyy HH:mm:ss";
    private DateFormat df = new SimpleDateFormat(pattern);

    @Argument(index = 0, name = "token", description = "The JWT token to decompile", required = true, multiValued = false)
    public String token = StringUtils.EMPTY;

    @Reference
    private Authentication<Jwt> jwtAuthentication;

    @Override
    public Object execute() throws Exception {

        Jwt jwt = jwtAuthentication.validate(token);
        return String.format(ANSI_RED + "Audience " + ANSI_RESET + "%s \n" +
                            ANSI_RED + "Issuer "  + ANSI_RESET + "%s \n" +
                            ANSI_RED + "Subject " + ANSI_RESET + "%s \n" +
                            ANSI_RED + "Issued at " + ANSI_RESET + "%s \n" +
                            ANSI_RED + "Expires at " + ANSI_RESET + "%s", jwt.getAud(), jwt.getIss(), jwt.getSub(), df.format(jwt.getIat()), df.format(jwt.getExp()));
    }
    
}
