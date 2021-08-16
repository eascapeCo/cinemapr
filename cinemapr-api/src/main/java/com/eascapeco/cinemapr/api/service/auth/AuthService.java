package com.eascapeco.cinemapr.api.service.auth;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.entity.RefreshToken;
import com.eascapeco.cinemapr.api.model.payload.LoginRequest;
import com.eascapeco.cinemapr.api.security.JwtTokenProvider;
import com.eascapeco.cinemapr.api.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * 관리자 service
 *
 * @author jaehankim
 * @Date 2019. 10. 10
 */
@Service @RequiredArgsConstructor
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    private AuthenticationManager authenticationManager;

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getAdmId(), loginRequest.getPwd())));
    }

    public Optional<RefreshToken> createTokenByLogin(LoginRequest loginRequest) {
        Admin currentAdmin = adminService.findByUsername(loginRequest.getAdmId());

//        Refresh Token 확인하는 로직 추가해야됨

        RefreshToken refreshToken = jwtTokenProvider.genRefreshToken(currentAdmin);

//        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    public String generateAccessToken(LoginRequest loginRequest) {
        Admin currentAdmin = adminService.findByUsername(loginRequest.getAdmId());

        return jwtTokenProvider.generateToken(currentAdmin);
    }

    public Date getExpiryDuration(String accessToken) {
        return jwtTokenProvider.getExpirationDateFromToken(accessToken);
    }
}