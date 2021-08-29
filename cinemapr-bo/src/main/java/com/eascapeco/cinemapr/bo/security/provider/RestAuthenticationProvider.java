package com.eascapeco.cinemapr.bo.security.provider;

import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.bo.security.token.AjaxAuthenticationToken;
import com.eascapeco.cinemapr.bo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication == null) {
            throw new IllegalArgumentException("authentication issue error");
        }

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("authentication id : {}, pwd : {}", username, password);

        Admin admin = authService.findByAdmId(username, password);

        AdminDto account = new AdminDto();
        account.setAdmId(admin.getAdmId());
//        account.setPwd(admin.getPwd());
        account.setAdmNo(admin.getAdmNo());
        account.setAdminRole(admin.getAdminRole());

        return new AjaxAuthenticationToken(account, account.getPassword(), account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AjaxAuthenticationToken.class);
    }

}
