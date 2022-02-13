package com.eascapeco.cinemapr.bo.security.filter;

import com.eascapeco.cinemapr.api.exception.InvalidTokenRequestException;
import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.security.token.JwtTokenProvider;
import com.eascapeco.cinemapr.bo.security.token.TokenStatus;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
import com.eascapeco.cinemapr.bo.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String tokenRequestHeader = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    private final BoAdminService boAdminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getAccessTokenFromRequest(request);
        AdminDto adminDto = null;

        TokenStatus tokenStatus = StringUtils.hasText(accessToken) ? jwtTokenProvider.validateToken(accessToken) : TokenStatus.INVALID;
        if (tokenStatus == TokenStatus.ACTIVE) {
            adminDto = new AdminDto();
            adminDto.setAdmNo(jwtTokenProvider.getAdminNoFromToken(accessToken));
            this.setAuthentication(accessToken, adminDto);

        } else if (tokenStatus == TokenStatus.EXPIRED) {
            RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(CookieUtils.getCookie("uid", request));

            if (jwtTokenProvider.validateToken(refreshToken.getRefreshToken()) == TokenStatus.ACTIVE) {
                adminDto = boAdminService.findById(refreshToken.getAdmNo());
                JwtAuthenticationResponse newAuthenticationResponse = jwtTokenProvider.getJwtAuthenticationResponse(adminDto);
                jwtTokenProvider.setHeaderAccessToken(response, newAuthenticationResponse);

                setAuthentication(newAuthenticationResponse.getAccessToken(), adminDto);

            } else {
                SecurityContextHolder.clearContext();
                throw new InvalidTokenRequestException("RefreshToken", refreshToken.getRefreshToken(), "Expired Token");
            }

        } else {
            SecurityContextHolder.clearContext();
            throw new InvalidTokenRequestException("AccessToken", accessToken, "Expired Token");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract the token from the Authorization request header
     */
    private String getAccessTokenFromRequest(HttpServletRequest request) {

        String accessToken = request.getHeader(tokenRequestHeader);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            log.info("Extracted Token: " + accessToken);
            return accessToken.replace("Bearer ", "");
        }
        return null;
    }

    private void setAuthentication(String token, AdminDto adminDto) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token, adminDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
