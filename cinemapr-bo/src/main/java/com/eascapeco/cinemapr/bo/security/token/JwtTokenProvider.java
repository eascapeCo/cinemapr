package com.eascapeco.cinemapr.bo.security.token;

import com.eascapeco.cinemapr.api.exception.InvalidTokenRequestException;
import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.service.redis.RedisService;
import com.eascapeco.cinemapr.bo.util.CookieUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class JwtTokenProvider implements Serializable {

    static final long JWT_TOKEN_EXP = (60 * 1); // 30 mins
    static final long JWT_REFRESH_TOKEN_EXP = 30 * (60 * 60 * 24); // 30 days

    private final RedisService redisService;

    public JwtTokenProvider(RedisService redisService) {
        this.redisService = redisService;
    }

    byte[] byteKeys;

    {
        try {
            byteKeys = "eascapecoscinemapr2020".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    SecretKey key = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(byteKeys).getBytes());

    /**
     * 토큰을 생성하는 메서드
     *
     * @param map
     * @param admin
     * @return
     */
    @Deprecated
    public String createJwtToken(Map<String, Object> map, AdminDto admin) {
        return Jwts.builder().setClaims(map)
                   .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                   .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(JWT_TOKEN_EXP).toInstant()))
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();

        /*return JWT.create().withClaim("id", admin.getUsername())
                             .withClaim("admNo", admin.getAdmNo())
                             .withClaim("roles", li)
                             .withIssuer("JaeHan")
                             .withIssuedAt(new Date(System.currentTimeMillis()))
                             .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_EXP * 1000))
                             .sign(Algorithm.HMAC256(secret));*/
    }

    /**
     * 리프레쉬 토큰 생성 메소드
     *
     * @param chkAdm
     * @return Optional
     */
    public String refreshJwtToken(AdminDto chkAdm) {
        Map<String, Object> map = new HashMap<>();
        return Jwts.builder().setClaims((Claims) new HashMap<>().put("admNo", chkAdm.getAdmNo()))
                             .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                             .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(JWT_REFRESH_TOKEN_EXP).toInstant()))
                             .signWith(key, SignatureAlgorithm.HS256)
                             .compact();

        /*return JWT.create().withClaim("id", admin.getUsername())
                             .withIssuer("JaeHan")
                             .withIssuedAt(new Date(System.currentTimeMillis()))
                             .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXP * 1000))
                             .sign(Algorithm.HMAC256(secret));*/
    }

    /**
     * JWT 토큰 생성 시작
     *
     * @param chkAdm
     * @return
     */
    @Transactional
    public String generateToken(AdminDto chkAdm) {
        String authorityList = chkAdm.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        return Jwts.builder().setSubject(chkAdm.getAdmNo().toString())
            .claim("authorities", authorityList)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(JWT_TOKEN_EXP).toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * check if the token has expired
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final LocalDate expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDate.now());
    }

    /**
     * retrieve username from jwt token
     *
     * @param token
     * @return
     */
//
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token).get("admId").toString();

//        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     *
     * @param token
     * @return
     */
    public List<GrantedAuthority> getAuthorityListFromToken(String token) {
        return Arrays.stream(getClaimFromToken(token).get("authorities").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    /**
     * retrieve admin number from jwt token
     *
     * @param token
     * @return
     */
    public Long getAdminNoFromToken(String token) {
        return Long.parseLong(getClaimFromToken(token).get("sub").toString());
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token
     * @return
     */
    public LocalDate getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    public Claims getClaimFromToken(String token) {
        return getAllClaimsFromToken(token);
    }

    /**
     * for retrieveing any information from token we will need the secret key
     *
     * @param token
     * @return
     */
    public Claims getAllClaimsFromToken(String token) {

        return Jwts.parserBuilder().setSigningKey(key)
                            .build().parseClaimsJws(token).getBody();
    }

    /**
     * Validates if a token satisfies the following properties
     * - Signature is not malformed
     * - Token hasn't expired
     * - Token is supported
     * - Token has not recently been logged out.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
        return true;
    }

    public RefreshToken getRefreshToken(HttpServletRequest request) {
        RefreshToken refreshToken = (RefreshToken) redisService.getValue(CookieUtils.getCookie("uid", request));
        return refreshToken;
    }

    public JwtAuthenticationResponse getJwtAuthenticationResponse(String refreshToken, AdminDto adminDto) {
        String accessToken = generateToken(adminDto);
        refreshToken = StringUtils.hasText(refreshToken) ? refreshToken : refreshJwtToken(adminDto);
        LocalDate expiryDuration = getExpirationDateFromToken(accessToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken, expiryDuration);
    }
}