package project.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserEmail(String jwt);
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
}
