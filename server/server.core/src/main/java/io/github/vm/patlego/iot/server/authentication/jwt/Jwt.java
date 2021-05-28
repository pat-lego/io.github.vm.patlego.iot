package io.github.vm.patlego.iot.server.authentication.jwt;

import java.util.Date;

public class Jwt {

    // Issuer of the JWT
    private String iss;

    // Subject of the JWT
    private String sub;

    // Recipient of the JWT
    private String aud;

    // Expiration time of the JWT
    private Date exp;

    // Issued at time
    private Date iat;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }

    

    
}
