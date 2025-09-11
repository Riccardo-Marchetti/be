package piastrellista.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import piastrellista.entities.User;
import piastrellista.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    // GENERATE THE TOKEN
    public String createToken (User user){
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 *7 ))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // CHECK THE TOKEN
    public void verifyToken (String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parse(token);
        }catch (Exception ex){
            throw new UnauthorizedException("Problems with the token please log in again");
        }
    }

    // EXTRACT ID FROM TOKEN
    public String extractIdFromToken (String token){
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // verifica il token con quel segreto
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
