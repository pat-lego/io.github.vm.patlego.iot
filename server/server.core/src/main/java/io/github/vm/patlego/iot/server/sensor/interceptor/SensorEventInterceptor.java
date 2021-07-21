package io.github.vm.patlego.iot.server.sensor.interceptor;

import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.dao.exceptions.InvalidSensorEventException;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;

public class SensorEventInterceptor extends AbstractPhaseInterceptor<Message> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SensorEventInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleFault(Message message) {
        String url = (String) message.get(Message.REQUEST_URL);
        if (url.contains("/cxf/sensors/events")) {
            Exception ex = message.getContent(Exception.class);
            if (ex.getCause() instanceof InvalidSensorEventException) {
                HttpServletResponse resp = (HttpServletResponse) message.getExchange().getInMessage()
                        .get(AbstractHTTPDestination.HTTP_RESPONSE);
                try (OutputStream out = resp.getOutputStream()) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

    @Override
    public void handleMessage(Message message) throws Fault {
        // Nothing to do
    }

}
