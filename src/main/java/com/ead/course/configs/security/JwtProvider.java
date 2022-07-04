package com.ead.course.configs.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtProvider {

    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    public String getUsernameJwt(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }


    public String getClaimNameJwt(String token, String claimName){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
                .getBody().get(claimName).toString();
    }

    public boolean validatedJwt(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT {}" , e.getMessage());
        }catch (MalformedJwtException e) {
            log.error("Invalid JWT {}" , e.getMessage());
        }catch (ExpiredJwtException e) {
            log.error("expired JWT {}" , e.getMessage());
        }catch (UnsupportedJwtException e) {
            log.error("unsupported JWT {}" , e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("is empty JWT {}" , e.getMessage());
        }
        return false;
    }
}
