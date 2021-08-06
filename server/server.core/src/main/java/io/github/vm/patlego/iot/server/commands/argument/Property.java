package io.github.vm.patlego.iot.server.commands.argument;

public interface Property {
    
    public PropertyType getType();

    public String getAsString() throws PropertyException;

    public Integer getAsInteger() throws PropertyException;

    public Boolean getAsBoolean() throws PropertyException;

    public Float getAsFloat() throws PropertyException;

    public Double getAsDouble() throws PropertyException;

}
