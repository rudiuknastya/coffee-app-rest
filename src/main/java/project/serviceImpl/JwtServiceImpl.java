package project.serviceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private static final String SECRET_KEY = "73834a6390ff5bd94c555fd5a0bee07fd0d83918d82fe6cdd99ecad67d68e0cf";
    private long accessExpiration = 86400000;//120000;
    private long refreshExpiration = 604800000;
    @Override
    public String extractUserEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,accessExpiration);
    }
    @Override
    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,refreshExpiration);
    }
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails,long expiration ){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        String email = extractUserEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt){
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
