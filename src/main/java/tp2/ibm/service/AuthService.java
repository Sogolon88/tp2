package tp2.ibm.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tp2.commonException.ConflitException;
import tp2.commonException.IdentifiantsInvalidesException;
import tp2.ibm.dto.*;
import tp2.ibm.entity.User;
import tp2.ibm.repository.UserRepository;
import tp2.ibm.service.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest req) throws ConflitException {
        if (userRepository.existsByUsername(req.username())) {
            throw new ConflitException("Ce nom d'utilisateur est déjà pris");
        }
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));   // hachage BCrypt
        user.setMail(req.email());
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getMail());
    }

    public AuthResponse login(LoginRequest req) throws IdentifiantsInvalidesException {
        User user = userRepository.findByUsername(req.username())
                .filter(u -> passwordEncoder.matches(req.password(), u.getPassword()))
                .orElseThrow(() -> new IdentifiantsInvalidesException("Identifiants invalides"));
        return new AuthResponse(
                jwtService.genererAccessToken(user.getUsername()),
                jwtService.genererRefreshToken(user.getUsername()));
    }

    public AuthResponse refresh(RefreshRequest req) throws IdentifiantsInvalidesException {
        String username = jwtService.extraireUsername(req.refreshToken(), "refresh");
        if (username == null || !userRepository.existsByUsername(username)) {
            throw new IdentifiantsInvalidesException("Refresh token invalide ou expiré");
        }
        // Nouvel access token, sans redemander le mot de passe
        return new AuthResponse(jwtService.genererAccessToken(username), req.refreshToken());
    }
}