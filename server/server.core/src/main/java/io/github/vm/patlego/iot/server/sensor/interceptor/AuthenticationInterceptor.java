package io.github.vm.patlego.iot.server.sensor.interceptor;


import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class AuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    public AuthenticationInterceptor() {
        super(Phase.INVOKE);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
        // Check to see if the header is well set if not return and send a nice piss off message 
        if (policy == null) {
            return;
        }
        
        
    }
    
}
