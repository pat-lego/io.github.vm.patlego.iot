package io.github.vm.patlego.iot.server.sensor.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Conduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.authentication.Authentication;

public class AuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Authentication auth;

    public AuthenticationInterceptor(Authentication auth) {
        super(Phase.INVOKE);

        this.auth = auth;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
        // Check to see if the header is well set if not return and send a nice piss off
        // message
        if (policy == null) {
            sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }

    }

    private void sendErrorResponse(Message message, int responseCode) {
        Message outMessage = getOutMessage(message);
        outMessage.put(Message.RESPONSE_CODE, responseCode);

        try(OutputStream os = outMessage.getContent(OutputStream.class)) {
            message.getInterceptorChain().abort();
            getConduit(message).prepare(outMessage);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Message getOutMessage(Message inMessage) {
        Exchange exchange = inMessage.getExchange();
        Message outMessage = exchange.getOutMessage();
        if (outMessage == null) {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMessage = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMessage);
        }
        outMessage.putAll(inMessage);
        return outMessage;
    }

    private Conduit getConduit(Message inMessage) throws IOException {
        Exchange exchange = inMessage.getExchange();
        Conduit conduit = exchange.getDestination().getBackChannel(inMessage);
        exchange.setConduit(conduit);
        return conduit;
    }

}
