package com.eascapeco.cinemapr.bo.security.filter;

import com.eascapeco.cinemapr.api.model.payload.JwtAuthenticationResponse;
import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.security.token.AjaxAuthenticationToken;
import com.eascapeco.cinemapr.bo.security.token.JwtTokenProvider;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
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
        try {
            String jwtToken = getJwtFromRequest(request);
            log.info("jwtToken: {}", jwtToken);

            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                Long adminNo = jwtTokenProvider.getAdminNoFromToken(jwtToken);
                log.info("adminNo: {}", adminNo);
                jwtTokenProvider.getAuthorityListFromToken(jwtToken);
                List<GrantedAuthority> authorityList = jwtTokenProvider.getAuthorityListFromToken(jwtToken);
                AdminDto adminDto = new AdminDto();
                adminDto.setAdmNo(adminNo);
                AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(adminDto, null, authorityList);
                ajaxAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(ajaxAuthenticationToken);
            }
        } catch (ExpiredJwtException ex) {
//            check refreshToken
            RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(request);
            log.info("token {} : ", refreshToken.getRefreshToken());
            if (!jwtTokenProvider.isTokenExpired(refreshToken.getRefreshToken())) {
                AdminDto adminDto = boAdminService.findById(refreshToken.getAdmNo());
                JwtAuthenticationResponse jwtAuthenticationResponse = jwtTokenProvider.getJwtAuthenticationResponse(adminDto);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(jwtAuthenticationResponse));
            }
        } catch (Exception e) {
            log.error("Failed to set user authentication in security context: ", e);
            throw e;
        }

        filterChain.doFilter(request, response);

    }


    /**
     * Extract the token from the Authorization request header
     */
    private String getJwtFromRequest(HttpServletRequest request) {

        String accessToken = request.getHeader(tokenRequestHeader);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            log.info("Extracted Token: " + accessToken);
            return accessToken.replace("Bearer ", "");
        }
        return null;
    }

}
