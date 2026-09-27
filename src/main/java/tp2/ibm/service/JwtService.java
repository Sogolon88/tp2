package tp2.ibm.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-expiration-ms}") long accessExpirationMs,
                      @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String genererAccessToken(String username) {
        return generer(username, "access", accessExpirationMs);
    }

    public String genererRefreshToken(String username) {
        return generer(username, "refresh", refreshExpirationMs);
    }

    private String generer(String username, String type, long dureeMs) {
        Date maintenant = new Date();
        return Jwts.builder()
                .subject(username)                                        // à qui appartient le token
                .claim("type", type)                                      // access ou refresh
                .issuedAt(maintenant)
                .expiration(new Date(maintenant.getTime() + dureeMs))
                .signWith(key)                                            // signature HMAC-SHA256
                .compact();                                               // → Header.Payload.Signature
    }

    /** Renvoie le username si le token est valide et du bon type, sinon null. */
    public String extraireUsername(String token, String typeAttendu) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)                 // vérifie la signature
                    .build()
                    .parseSignedClaims(token)        // vérifie aussi l'expiration
                    .getPayload();
            if (!typeAttendu.equals(claims.get("type", String.class))) {
                return null;                         // ex. : un refresh token utilisé comme access token
            }
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;                             // token falsifié, expiré ou mal formé
        }
    }
}