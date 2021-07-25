package com.eascapeco.cinemapr.bo.controller.auth;

import com.eascapeco.cinemapr.api.exception.UserLoginException;
import com.eascapeco.cinemapr.api.model.entity.RefreshToken;
import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.api.model.payload.LoginRequest;
import com.eascapeco.cinemapr.api.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * Bo 관리자 인증 Controller
 *
 * @Date 2019. 11. 01
 * @author jaehankim
 *
 */

@RestController
@RequestMapping("/api")
public class BoAuthController {

    private static final Logger logger = LoggerFactory.getLogger(BoAuthController.class);

    private AuthService authService;

    public BoAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        logger.info("Admin info : {}", loginRequest.toString());

        Authentication authentication = authService.authenticateUser(loginRequest)
            .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createTokenByLogin(loginRequest)
            .map(RefreshToken::getToken)
            .map(refreshToken -> {
                String accessToken = authService.generateAccessToken(loginRequest);
                Date expiryDuration = authService.getExpiryDuration(accessToken);
                return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken, expiryDuration));
            }).orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }

}
