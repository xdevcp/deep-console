package cc.devcp.project.gateway.utils;

import cc.devcp.project.common.model.dto.AuthInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;


/**
 * Jwt token tool
 *
 * @author wfnuser
 */
@Component
public class JwtHelper {

    private final Logger log = LoggerFactory.getLogger(JwtHelper.class);

    private static final String AUTHORITIES_KEY = "auth";

    /**
     * minimum SHA_256 secretKey string length
     */
    private static final int SHA_256_SECRET_CHAR_SIZE = 256 / 8;

    /**
     * default SHA_256 secretKey flag
     */
    private static final String DEFAULT_SECRET_FLAG = "default";

    /**
     * custom SHA_256 secretKey from config property
     */
    @Value("${app.security.token.secret-key:default}")
    private String customSecretKeyStr;

    /**
     * secret key
     */
    private SecretKey secretKey;

    /**
     * Token validity time(ms)
     */
    private long tokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        //use default secretKey for SHA-256
        if (customSecretKeyStr == null || DEFAULT_SECRET_FLAG.equals(customSecretKeyStr)) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            //use custom secretKey
            int size = customSecretKeyStr.length();
            int left = SHA_256_SECRET_CHAR_SIZE - size;
            if (left > 0) {
                //character for padding
                StringBuilder stringBuilder = new StringBuilder(customSecretKeyStr);
                for (int i = 0; i < left; i++) {
                    stringBuilder.append(i % 10);
                }
                this.secretKey = Keys.hmacShaKeyFor(stringBuilder.toString().getBytes());
            } else {
                this.secretKey = Keys.hmacShaKeyFor(customSecretKeyStr.getBytes());
            }
        }
        this.tokenValidityInMilliseconds = 1000 * 60 * 30L;
    }

    /**
     * Create token
     *
     * @param authentication auth info
     * @return token
     */
    public String createToken(String username) {
        /**
         * Current time
         */
        long now = (new Date()).getTime();
        /**
         * Validity date
         */
        Date validity;
        validity = new Date(now + this.tokenValidityInMilliseconds);

        /**
         * create token
         */
        return Jwts.builder()
            .setSubject(username)
            .claim(AUTHORITIES_KEY, "")
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Get auth Info
     *
     * @param token token
     * @return auth info
     */
    public AuthInfo getAuthentication(String token) {
        /**
         *  parse the payload of token
         */
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        AuthInfo authInfo = getUserInfo(claims);
        return authInfo;
    }

    public static AuthInfo getUserInfo(Claims claims) {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setAccount(claims.get("account", String.class));
        authInfo.setAccountName(claims.get("accountName", String.class));
        return authInfo;
    }

    /**
     * validate token
     *
     * @param token token
     * @return whether valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
