package com.eascapeco.cinemapr.bo.service.auth;

import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.security.token.JwtTokenProvider;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 관리자 service
 *
 * @author jaehankim
 * @Date 2019. 10. 10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final BoAdminService boAdminService;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationResponse createAccessTokenByLogin(AdminDto adminDto) {
        JwtAuthenticationResponse jwtAuthenticationResponse = jwtTokenProvider.getJwtAuthenticationResponse(adminDto);
        return jwtAuthenticationResponse;
    }

    public RefreshToken createRefreshTokenByLogin(AdminDto adminDto) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), adminDto.getAdmNo(), jwtTokenProvider.generateRefreshToken(adminDto));

        return refreshToken;
    }

    public AdminDto findByAdmId(String username, String password) {
        AdminDto adminDto = boAdminService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, adminDto.getPassword())) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
        return adminDto;
    }

}