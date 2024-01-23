package pierpaolo.colasante.u5w2d5project.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.UnauthorizedException;

import java.util.Date;
@Component
public class JWTtools {
    @Value("${spring.jwt.secret}")
    private String secret;
    public String createToken(User user){
        return Jwts.builder().subject(String.valueOf(user.getId())) // Subject <-- A chi appartiene il token (id dell'utente)
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione (IAT - Issued At)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza (Expiration Date)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token
                .compact();
    }
    public void verifyToken(String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi col token! Per favore effettua di nuovo il login!");
        }
    }
    public String extractIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}
