package com.eascapeco.cinemapr.bo.security.filter;

import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.security.token.AjaxAuthenticationToken;
import com.eascapeco.cinemapr.bo.security.token.JwtTokenProvider;
import com.eascapeco.cinemapr.bo.security.token.TokenStatus;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
import com.eascapeco.cinemapr.bo.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private String tokenRequestHeader = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    private final  ObjectMapper objectMapper;

    private final BoAdminService boAdminService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getAccessTokenFromRequest(request);
        TokenStatus tokenStatus = StringUtils.hasText(jwtToken) ? jwtTokenProvider.validateToken(jwtToken) : TokenStatus.INVALID;

        if (tokenStatus == TokenStatus.ACTIVE) {
            AdminDto adminDto = new AdminDto();
            adminDto.setAdmNo(jwtTokenProvider.getAdminNoFromToken(jwtToken));
            List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorityListFromToken(jwtToken);

            setAuthentication(request, adminDto, authorities);

        } else if (tokenStatus == TokenStatus.EXPIRED) {
            RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(CookieUtils.getCookie("uid", request));

            TokenStatus refreshTokenStatus = jwtTokenProvider.validateToken(refreshToken.getRefreshToken());

            if (refreshTokenStatus == TokenStatus.ACTIVE) {
                AdminDto adminDto = boAdminService.findById(refreshToken.getAdmNo());
                JwtAuthenticationResponse jwtAuthenticationResponse = jwtTokenProvider.getJwtAuthenticationResponse(adminDto);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorityListFromToken(jwtAuthenticationResponse.getAccessToken());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(jwtAuthenticationResponse));

                setAuthentication(request, adminDto, authorities);
            }

        } else {

        }

        filterChain.doFilter(request, response);

    }

    private void setAuthentication(HttpServletRequest request, AdminDto adminDto, List<GrantedAuthority> authorities) {
        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(adminDto, null, authorities);
        ajaxAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(ajaxAuthenticationToken);
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

}
