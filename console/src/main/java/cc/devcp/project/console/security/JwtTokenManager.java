package cc.devcp.project.console.security;

import cc.devcp.project.core.auth.AuthConfigs;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * JWT token manager
 *
 * @author wfnuser
 * @author nkorange
 */
@Component
public class JwtTokenManager {

    private static final String AUTHORITIES_KEY = "auth";

    @Autowired
    private AuthConfigs authConfigs;

    /**
     * Create token
     *
     * @param authentication auth info
     * @return token
     */
    public String createToken(Authentication authentication) {
        return createToken(authentication.getName());
    }

    public String createToken(String userName) {

        long now = (new Date()).getTime();

        Date validity;
        validity = new Date(now + authConfigs.getTokenValidityInSeconds() * 1000L);

        Claims claims = Jwts.claims().setSubject(userName);

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, authConfigs.getSecretKey())
            .compact();
    }

    /**
     * Get auth Info
     *
     * @param token token
     * @return auth info
     */
    public Authentication getAuthentication(String token) {
        /**
         *  parse the payload of token
         */
        Claims claims = Jwts.parser()
            .setSigningKey(authConfigs.getSecretKey())
            .parseClaimsJws(token)
            .getBody();

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * validate token
     *
     * @param token token
     * @return whether valid
     */
    public void validateToken(String token) {
        Jwts.parser().setSigningKey(authConfigs.getSecretKey()).parseClaimsJws(token);
    }
}
