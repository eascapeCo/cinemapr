package com.eascapeco.cinemapr.bo.security.filter;

import com.eascapeco.cinemapr.api.model.dto.AdminDto;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import com.eascapeco.cinemapr.bo.security.token.AjaxAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    public RestLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login", HttpMethod.POST.name()));
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        /*
        if (!isAjax(request)) {
            throw new IllegalStateException("Authentitacion is not supported");
        }*/

        AdminDto accountDto = objectMapper.readValue(request.getReader(), AdminDto.class);
        System.out.println("admId " + accountDto.getUsername());
        System.out.println("pw " + accountDto.getPassword());

        if (StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    private boolean isAjax(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-with"))) {
            return  true;
        }
        return false;
    }
}
