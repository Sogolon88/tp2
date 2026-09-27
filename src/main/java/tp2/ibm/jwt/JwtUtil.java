package tp2.ibm.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;


public class JwtUtil {
    private final SecretKey cle = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        public String genererToken(String username) {
        return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 3600_000)) // 1h
        .signWith(cle)
        .compact();
    }
    public String extraireUsername(String token) {
        return Jwts.parser().verifyWith(cle).build()
        .parseSignedClaims(token).getPayload().getSubject();
    }
}