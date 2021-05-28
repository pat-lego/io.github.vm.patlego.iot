package io.github.vm.patlego.iot.server.authentication.jwt;

import java.security.Key;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component(service = Authentication.class, immediate = true)
public class JwtAuthentication implements Authentication<Jwt> {

    private Key key;

    @Override
    public Jwt validate(String jwtToken) {
        Claims claim = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(jwtToken).getBody();
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
            .signWith(this.key)
            .compact();

    }

    @Activate
    protected void activate() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    
}
