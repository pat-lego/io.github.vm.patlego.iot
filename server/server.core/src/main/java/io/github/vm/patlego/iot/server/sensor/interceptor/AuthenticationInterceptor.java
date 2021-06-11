package io.github.vm.patlego.iot.server.sensor.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;

public class AuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Authentication<Jwt> auth;
    private static final String AUTH_HEADER = "Authorization";

    public AuthenticationInterceptor(Authentication<Jwt> auth) {
        super(Phase.INVOKE);

        this.auth = auth;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
            this.auth.validate(headers.get(AUTH_HEADER).get(0));
        } catch (Exception e) {
            logger.error("Failed to validate JWT for authentication generating fault", e);
            throw new Fault(new AuthenticationException("Failed to validate JWT for authentication generating fault"));
        }

    }

    @Override
    public void handleFault(Message message) {
        Exception ex = message.getContent(Exception.class);
        if (ex.getCause() instanceof AuthenticationException) {
            HttpServletResponse resp = (HttpServletResponse) message.getExchange().getInMessage()
                    .get(AbstractHTTPDestination.HTTP_RESPONSE);
            try (OutputStream out = resp.getOutputStream()) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("text/plain");
                out.write(ex.getMessage().getBytes());
                message.getInterceptorChain().setFaultObserver(null); // avoid return soap fault
                message.getInterceptorChain().abort();
            } catch (IOException e) {
                logger.error("Failed to write error message to the response stream", e);
            }
        }
    }
}
