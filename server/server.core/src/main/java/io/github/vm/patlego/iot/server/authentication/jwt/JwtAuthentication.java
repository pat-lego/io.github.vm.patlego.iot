package io.github.vm.patlego.iot.server.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.authentication.Authentication;

@Component(service = Authentication.class, immediate = true)
public class JwtAuthentication implements Authentication<Jwt> {

    @Reference
    private Security security;

    private Algorithm algo;

    @Override
    public Jwt validate(String jwtToken) {
        JWTVerifier verifier = JWT.require(this.algo).build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
    
        Jwt jwt = new Jwt();

        jwt.setAud(String.join(",", decodedJWT.getAudience()));
        jwt.setExp(decodedJWT.getExpiresAt());
        jwt.setIat(decodedJWT.getIssuedAt());
        jwt.setIss(decodedJWT.getIssuer());
        jwt.setSub(decodedJWT.getSubject());

        return jwt;
    }

    @Override
    public String createToken(Jwt token) {

        return JWT.create()
            .withIssuer(token.getIss())
            .withIssuedAt(token.getIat())
            .withSubject(token.getSub())
            .withAudience(token.getAud())
            .withExpiresAt(token.getExp())
            .sign(this.algo);
    }

    @Activate
    protected void activate() {
        String base64 = this.security.decrypt("HdPuBv8LQFJ2tO6apIM54ZE7beYmvHp5/N7F20bwRJT0a3KGOUfsm7o4CmhXbFznL7cEWJGk+HbbJ70s7lta4UNupQ1E8CNVavCuJ4jOEoE=");
        this.algo = Algorithm.HMAC256(base64);
    }
    
}
