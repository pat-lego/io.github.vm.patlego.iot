package io.github.vm.patlego.iot.server.commands.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestPropertyType {

    @Test
    public void testSimplePropertyString() throws PropertyException {
        Property prop = new SimpleProperty("Pat was here!!!");
        assertEquals(PropertyType.STRING, prop.getType());

        assertEquals("Pat was here!!!", prop.getAsString());
    }

    @Test
    public void testSimplePropertyInt() throws PropertyException {
        Property prop = new SimpleProperty("Int:1");
        assertEquals(PropertyType.INTEGER, prop.getType());

        assertEquals(1, prop.getAsInteger());
    }

    @Test
    public void testSimplePropertyFloat() throws PropertyException {
        Property prop = new SimpleProperty("Float:1");
        assertEquals(PropertyType.FLOAT, prop.getType());

        assertEquals(1L, prop.getAsFloat());
    }

    @Test
    public void testSimplePropertyBool() throws PropertyException {
        Property prop = new SimpleProperty("Bool:TruE");
        assertEquals(PropertyType.BOOLEAN, prop.getType());

        assertEquals(true, prop.getAsBoolean());
    }
    
}
