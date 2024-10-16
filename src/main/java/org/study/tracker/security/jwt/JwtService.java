package org.study.tracker.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static io.jsonwebtoken.Jwts.parser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.study.tracker.model.User;


@Service
public class JwtService {
  //private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  @Value("${security.jwt.secret}")
  private String jwtSigningKey;

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    if (userDetails instanceof User customUserDetails) {
      claims.put("id", customUserDetails.getId());
      claims.put("role", customUserDetails.getRole());
    }
    return generateToken(claims, userDetails);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    int jwtExpirationMs = 3000000 + 60 * 24;
    return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return parser()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

//  public boolean validateJwtToken(String authToken) {
//    try {
//      parser().setSigningKey(getSigningKey()).build().parse(authToken);
//      return true;
//    } catch (MalformedJwtException e) {
//      logger.info("Invalid JWT token: {}", e.getMessage());
//    } catch (ExpiredJwtException e) {
//      logger.info("JWT token is expired: {}", e.getMessage());
//    } catch (UnsupportedJwtException e) {
//      logger.info("JWT token is unsupported: {}", e.getMessage());
//    } catch (IllegalArgumentException e) {
//      logger.info("JWT claims string is empty: {}", e.getMessage());
//    }
//    return false;
//  }

}
