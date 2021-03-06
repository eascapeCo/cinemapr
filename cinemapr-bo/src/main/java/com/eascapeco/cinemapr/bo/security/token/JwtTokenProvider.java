package com.eascapeco.cinemapr.bo.security.token;

import com.eascapeco.cinemapr.api.exception.RefreshTokenNotFoundException;
import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.service.redis.RefreshTokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class JwtTokenProvider implements Serializable {

    static final long JWT_TOKEN_EXP = (60 * 30); // 30 mins

    static final long JWT_REFRESH_TOKEN_EXP = (60 * 60 * 24); // 1 days

    private final RefreshTokenRedisRepository redisRepository;

    byte[] byteKeys;

    {
        try {
            byteKeys = "eascapecoscinemapr2020".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    SecretKey key = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(byteKeys).getBytes());

    public JwtTokenProvider(RefreshTokenRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

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
    public String generateRefreshToken(AdminDto chkAdm) {
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
     * JWT 토큰으로 인증 정보를 조회
     *
     * @param token
     * @param adminDto
     * @return
     */
    public Authentication getAuthentication(String token, AdminDto adminDto) {
        return new UsernamePasswordAuthenticationToken(adminDto, "", getAuthorityListFromToken(token));
    }

    /**
     * JWT 토큰 생성 시작
     *
     * @param chkAdm
     * @return
     */
    @Transactional
    public String generateAccessToken(AdminDto chkAdm) {
        List authorityList = chkAdm.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        authorityList.add("ROLE_MANAGER");

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
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
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
    }

    /**
     * @param token
     * @return
     */
    public List<GrantedAuthority> getAuthorityListFromToken(String token) {
        List<String> list = getClaimFromToken(token).get("authorities", List.class);
        return list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    }

    /**
     * retrieve admin number from jwt token
     *
     * @param token
     * @return
     */
    public Long getAdminNoFromToken(String token) {
        Long a = Long.parseLong(getClaimFromToken(token).get("sub").toString());

        return a;
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token
     * @return
     */
    public LocalDateTime getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
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
    public TokenStatus validateToken(String authToken) {
        TokenStatus tokenStatus;

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            tokenStatus = TokenStatus.ACTIVE;

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            tokenStatus = TokenStatus.INVALID;
//            throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            tokenStatus = TokenStatus.INVALID;
//            throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");

        } catch (ExpiredJwtException ex) {
            tokenStatus = TokenStatus.EXPIRED;
            log.error("Expired JWT token");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            tokenStatus = TokenStatus.INVALID;
//            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            tokenStatus = TokenStatus.INVALID;
//            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
        return tokenStatus;
    }

    public RefreshToken getRefreshToken(String key) throws RefreshTokenNotFoundException {
        return redisRepository.findById(key)
            .orElseThrow(() -> new RefreshTokenNotFoundException(key));
    }

    public JwtAuthenticationResponse getJwtAuthenticationResponse(AdminDto adminDto) {
        String accessToken = generateAccessToken(adminDto);
        LocalDateTime expiryDuration = getExpirationDateFromToken(accessToken);

        return new JwtAuthenticationResponse(accessToken, expiryDuration);
    }

    public void setHeaderAccessToken(ServletResponse response, JwtAuthenticationResponse jwtAuthenticationResponse) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("access_token", jwtAuthenticationResponse.getAccessToken());
        httpServletResponse.setHeader("expires_in", String.valueOf(jwtAuthenticationResponse.getExpiryDuration()));

    }
}