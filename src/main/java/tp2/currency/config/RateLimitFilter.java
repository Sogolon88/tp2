package tp2.currency.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int LIMITE = 20;            // requêtes
    private static final long FENETRE_MS = 60_000;  // par minute

    private static class Fenetre {
        long debut;
        int compteur;
    }

    private final Map<String, Fenetre> parIp = new ConcurrentHashMap<>();

    // Le filtre ne s'applique qu'à GET /api/currency/convert
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return !("GET".equals(req.getMethod()) && "/api/currency/convert".equals(req.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        long maintenant = System.currentTimeMillis();

        Fenetre f = parIp.compute(req.getRemoteAddr(), (ip, ancienne) -> {
            if (ancienne == null || maintenant - ancienne.debut >= FENETRE_MS) {
                Fenetre nouvelle = new Fenetre();   // nouvelle minute → compteur remis à zéro
                nouvelle.debut = maintenant;
                nouvelle.compteur = 1;
                return nouvelle;
            }
            ancienne.compteur++;
            return ancienne;
        });

        if (f.compteur > LIMITE) {
            res.setStatus(429);
            res.setHeader("Retry-After", "60");
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"status\":429,\"error\":\"Too Many Requests\","
                    + "\"message\":\"Limite de 20 requêtes par minute dépassée\","
                    + "\"path\":\"/api/currency/convert\"}");
            return;
        }
        chain.doFilter(req, res);
    }
}