package run.antleg.sharp.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.security.MyUserDetails;
import run.antleg.sharp.util.EncoderHelper;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {
    // TODO: 移动到配置文件中
    private static final String HS512KEY = "0nQEx2CO15Sv4+AfdkqH5nadsvty3PAil+IJ7p08h/2HXYj2pnW5j18DPSTjJqEit0zumIkF0Ll7xtVvWAweyQ==";

    private final SecretKey hs512Key;

    private final JwtParser parser;

    public JwtService() {
        this.hs512Key = Keys.hmacShaKeyFor(EncoderHelper.base64(HS512KEY));
        this.parser = Jwts.parserBuilder().setSigningKey(this.hs512Key).build();
    }

    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7;

    private static final String ISSUER = "sharp";


    public String makeJwt(MyUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUserId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .setIssuer(ISSUER)
                .setId(UUID.randomUUID().toString()) // TODO: 搞一下 jti
                .signWith(this.hs512Key)
                .compact();
    }

    public Either<JwtValidationResult, UserId> verify(String jwt) {
        try {
            var token = parser.parseClaimsJws(jwt);
            var claims = token.getBody();

            var exp = claims.getExpiration();
            if (exp == null) {
                return Either.left(JwtValidationResult.INVALID_TOKEN);
            } else if (exp.getTime() < System.currentTimeMillis()) {
                return Either.left(JwtValidationResult.EXPIRED);
            }

            var iss = claims.getIssuer();
            if (iss == null) {
                return Either.left(JwtValidationResult.INVALID_TOKEN);
            } else if (!Objects.equals(iss, ISSUER)) {
                return Either.left(JwtValidationResult.INVALID_ISSUER);
            }

            var jti = claims.getId();
            if (jti == null) {
                return Either.left(JwtValidationResult.INVALID_TOKEN);
            }

            var sub = claims.getSubject();
            if (sub == null) {
                return Either.left(JwtValidationResult.INVALID_TOKEN);
            }

            return Either.right(UserId.from(sub));
        } catch (ExpiredJwtException ex) {
            log.error("jwt is expired", ex);
            return Either.left(JwtValidationResult.EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            log.error("jwt is invalid", ex);
            return Either.left(JwtValidationResult.INVALID_TOKEN);
        }
    }

    public static void main(String[] args) {
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println(EncoderHelper.base64(key.getEncoded()));
    }
}
