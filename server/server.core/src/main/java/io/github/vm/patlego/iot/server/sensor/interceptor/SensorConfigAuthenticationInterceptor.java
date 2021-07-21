package io.github.vm.patlego.iot.server.sensor.interceptor;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;

public class SensorConfigAuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    private SensorConfigDS sensor;
    private Security security;

    private static final String AUTH_HEADER = "Authorization";

    public SensorConfigAuthenticationInterceptor(SensorConfigDS sensor, Security security) {
        super(Phase.INVOKE);
        this.sensor = sensor;
        this.security = security;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        String url = (String) message.get(Message.REQUEST_URL);
        String method = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (url.contains("/cxf/sensors/configs") && method.equalsIgnoreCase("GET")) {
            long configId = getId(url);
            String key = this.sensor.getConfig(configId).getKey();
            Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));

            String authHeader = headers.get(AUTH_HEADER).get(0);
            String keyHeader = this.security.decrypt(authHeader);

            if (!key.equals(keyHeader)) {
                throw new Fault(new AuthenticationException(
                        "Failed to validate Auth headers for authentication generating fault"));
            }
        }

    }

    public long getId(String url) {
        String reversed = new StringBuilder(url).reverse().toString();
        StringBuilder id = new StringBuilder();
        for (char entry : reversed.toCharArray()) {
            if ('/' == entry) {
                break;
            }
            id.append(entry);
        }
        return Long.parseLong(id.toString());

    }

    @Override
    public void handleFault(Message message) {
        // DO NOTHING
    }

}
