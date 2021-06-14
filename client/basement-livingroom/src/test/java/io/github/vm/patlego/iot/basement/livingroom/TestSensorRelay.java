package io.github.vm.patlego.iot.basement.livingroom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.vm.patlego.iot.client.config.Config;

public class TestSensorRelay {

    @Test
    public void testDecrypt() {
        Config config = Mockito.mock(Config.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(config.getSystem().hasAuth()).thenReturn(Boolean.TRUE);
        Mockito.when(config.getSystem().getAuth().isEncrypted()).thenReturn(Boolean.TRUE);
        Mockito.when(config.getSystem().getAuth().getAuthorization()).thenReturn("U+sg8auAId8q2GO26YYXKQ==");
        SensorRelay relay = new SensorRelay();
        String result = relay.getToken(config);
        assertEquals("123", result);
    }
    
}
