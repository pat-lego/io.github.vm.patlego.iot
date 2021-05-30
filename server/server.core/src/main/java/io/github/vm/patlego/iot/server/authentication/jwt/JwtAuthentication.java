package io.github.vm.patlego.iot.server.authentication.jwt;

import java.security.Key;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component(service = Authentication.class, immediate = true)
public class JwtAuthentication implements Authentication<Jwt> {

    @Reference
    private Security security;

    private Key originalKey;

    @Override
    public Jwt validate(String jwtToken) {
        Claims claim = Jwts.parserBuilder().setSigningKey(this.originalKey).build().parseClaimsJws(jwtToken).getBody();
        Jwt jwt = new Jwt();

        jwt.setAud(claim.getAudience());
        jwt.setExp(claim.getExpiration());
        jwt.setIat(claim.getIssuedAt());
        jwt.setIss(claim.getIssuer());
        jwt.setSub(claim.getSubject());

        return jwt;
    }

    @Override
    public String createToken(Jwt token) {
        return Jwts.builder()
            .setSubject(token.getSub())
            .setIssuedAt(token.getIat())
            .setAudience(token.getAud())
            .setIssuer(token.getIss())
            .setExpiration(token.getExp())
            .signWith(this.originalKey)
            .compact();
    }

    @Activate
    protected void activate() {
        String base64 = this.security.decrypt("HdPuBv8LQFJ2tO6apIM54ZE7beYmvHp5/N7F20bwRJT0a3KGOUfsm7o4CmhXbFznL7cEWJGk+HbbJ70s7lta4UNupQ1E8CNVavCuJ4jOEoE=");
        this.originalKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64));
    }
    
}
