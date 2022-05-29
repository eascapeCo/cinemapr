package com.eascapeco.cinemapr.bo.security.handler;

import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.service.auth.AuthService;
import com.eascapeco.cinemapr.bo.service.redis.RefreshTokenRedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AdminDto adminDto = (AdminDto) authentication.getPrincipal();
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.createAccessTokenByLogin(adminDto);

        RefreshToken refreshToken = authService.createRefreshTokenByLogin(adminDto);

        redisRepository.save(refreshToken);

        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(jwtAuthenticationResponse));

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}