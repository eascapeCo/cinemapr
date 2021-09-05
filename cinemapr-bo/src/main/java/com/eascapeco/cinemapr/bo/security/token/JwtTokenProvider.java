package com.eascapeco.cinemapr.bo.security.token;

import com.eascapeco.cinemapr.api.exception.InvalidTokenRequestException;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.entity.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider implements Serializable {

    static final long JWT_TOKEN_EXP = (60 * 1 * 1000); // 30 mins
    static final long JWT_REFRESH_TOKEN_EXP = 30 * (60 * 60 * 24 * 1000); // 30 days

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
//    public String createJwtToken(List<String> li, Admin admin) {
    public String createJwtToken(Map<String, Object> map, Admin admin) {
        return Jwts.builder().setClaims(map)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXP))
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
    public String refreshJwtToken(Admin chkAdm) {
        Map<String, Object> map = new HashMap<>();
        return Jwts.builder().setClaims((Claims) new HashMap<>().put("admNo", chkAdm.getAdmNo()))
                             .setIssuedAt(new Date(System.currentTimeMillis()))
                             .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXP))
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
     * @return
     */
    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(null, "", null);
    }

    /**
     * JWT 토큰 생성 시작
     *
     * @param chkAdm
     * @return
     */
    public String generateToken(Admin chkAdm) {
        Map<String, Object> map = new HashMap<>();

        chkAdm.getRoleList().forEach(s -> {
            log.info("Role chk : {}", s.getRole());
        });

        map.put("admId", chkAdm.getAdmId());
        map.put("admNo", chkAdm.getAdmNo());
        map.put("admRole", chkAdm.getRoleList());

        List<String> rolesList = new ArrayList<>();

//        for (GrantedAuthority a: chkAdm.getAuthorities()) {
//            rolesList.add(a.getAuthority());
//        }

//        map.put("roles", rolesList);
        return createJwtToken(map, chkAdm);
    }

    /**
     * JWT 토큰 생성 시작
     *
     * @param chkAdm
     * @return
     */
    public RefreshToken genRefreshToken(Admin chkAdm) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshJwtToken(chkAdm));
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXP));

        return refreshToken;
    }

    /**
     * check if the token has expired
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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
        return (List<GrantedAuthority>) getClaimFromToken(token).get("admRole");

    }

    /**
     * retrieve admin number from jwt token
     *
     * @param token
     * @return
     */
    public Long getAdminNoFromToken(String token) {
        return (Long) getClaimFromToken(token).get("admNo");
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
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
            throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
        return true;
    }

    public String getExpiresIn(String token) {
        return Long.toString(Math.abs(getClaimFromToken(token).getExpiration().getTime() - new Date(System.currentTimeMillis()).getTime() - 1000));
    }

}