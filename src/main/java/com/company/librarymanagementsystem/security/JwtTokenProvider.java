package com.company.librarymanagementsystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${jwtSecret}")
  private String jwtSecret;

  @Value("${jwtExpirationInMs}")
  private int jwtExpirationInMs;

  public String generateToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
            .setSubject(userPrincipal.getUsername()) // sub olaraq ID yox, username qoyuruq
            .claim("id", userPrincipal.getId()) // ID ayrıca əlavə edilir
            .claim("roles", userPrincipal.getAuthorities()) // Rolları da əlavə edirik
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
  }


  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }

}
