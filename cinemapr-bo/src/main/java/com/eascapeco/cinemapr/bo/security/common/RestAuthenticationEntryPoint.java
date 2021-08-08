package com.eascapeco.cinemapr.bo.security.common;

import com.eascapeco.cinemapr.api.model.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Result result = new Result();
        result.setCode(401);
        result.setMessage("UNAUTHORIZED");

        ObjectMapper mapper = new ObjectMapper();

        String test = mapper.writeValueAsString(result);

        response.getWriter().write(test);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
