package com.eascapeco.cinemapr.bo.service.auth;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.security.token.JwtTokenProvider;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 관리자 service
 *
 * @author jaehankim
 * @Date 2019. 10. 10
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final BoAdminService boAdminService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationResponse createTokenByLogin(AdminDto adminDto) {
//        Refresh Token 확인하는 로직 추가해야됨

        String refreshToken = jwtTokenProvider.refreshJwtToken(adminDto);
        String accessToken = jwtTokenProvider.generateToken(adminDto);
        Date expiryDuration = jwtTokenProvider.getExpirationDateFromToken(accessToken);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(accessToken, refreshToken, expiryDuration);

//        refreshToken = refreshTokenService.save(refreshToken);
        return jwtAuthenticationResponse;
    }

    public Admin findByAdmId(String username, String password) {
        Admin findAdmin = boAdminService.findByAdmId(username);
        if (!passwordEncoder.matches(password, findAdmin.getPwd())) {
            throw new BadCredentialsException("Invalid Username or Password");
        }

        return findAdmin;
    }

    /**
     * Finds a admin in the database by admin no
     * @param adminNo
     * @return
     */
    public Admin findByAdmNo(Long adminNo) {
        return boAdminService.findByAdmNo(adminNo);
    }
}