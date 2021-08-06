package io.github.vm.patlego.iot.server.commands.argument;

import org.apache.commons.lang3.StringUtils;

public class SimpleProperty implements Property {

    private String property;

    private String BOOL = "Bool:";
    private String INT = "Int:";
    private String FLOAT = "Float:";
    private String DOUBLE = "Double:";

    public SimpleProperty(String property) {
        this.property = property;
    }

    @Override
    public PropertyType getType() {
        if (this.property.startsWith(BOOL)) {
            return PropertyType.BOOLEAN;
        } else if (this.property.startsWith(INT)) {
            return PropertyType.INTEGER;
        } else if (this.property.startsWith(FLOAT)) {
            return PropertyType.FLOAT;
        } else if (this.property.startsWith(DOUBLE)) {
            return PropertyType.DOUBLE;
        } else {
            return PropertyType.STRING;
        }
    }

    @Override
    public String getAsString() throws PropertyException {
        if (this.getType().equals(PropertyType.STRING)) {
            return this.property;
        }

        throw new PropertyException("The given property is not a String");
    }

    @Override
    public Integer getAsInteger() throws PropertyException {
        try {
            if (this.getType().equals(PropertyType.INTEGER)) {
                return Integer.parseInt(this.property.replace(INT, StringUtils.EMPTY));
            }
            throw new PropertyException("The given property is not a Integer");
        } catch (Exception e) {
            throw new PropertyException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean getAsBoolean() throws PropertyException {
        try {
            if (this.getType().equals(PropertyType.BOOLEAN)) {
                return Boolean.parseBoolean(this.property.replace(BOOL, StringUtils.EMPTY).toLowerCase());
            }
            throw new PropertyException("The given property is not a Boolean");
        } catch (Exception e) {
            throw new PropertyException(e.getMessage(), e);
        }
    }

    @Override
    public Float getAsFloat() throws PropertyException  {
        try {
            if (this.getType().equals(PropertyType.FLOAT)) {
                return Float.parseFloat(this.property.replace(FLOAT, StringUtils.EMPTY));
            }
            throw new PropertyException("The given property is not a Float");
        } catch (Exception e) {
            throw new PropertyException(e.getMessage(), e);
        }
    }

    @Override
    public Double getAsDouble() throws PropertyException  {
        try {
            if (this.getType().equals(PropertyType.BOOLEAN)) {
                return Double.parseDouble(this.property.replace(DOUBLE, StringUtils.EMPTY));
            }
            throw new PropertyException("The given property is not a Double");
        } catch (Exception e) {
            throw new PropertyException(e.getMessage(), e);
        }
    }
    
}
