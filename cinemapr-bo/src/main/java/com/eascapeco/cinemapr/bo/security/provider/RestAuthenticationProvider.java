package com.eascapeco.cinemapr.bo.security.provider;

import com.eascapeco.cinemapr.api.model.dto.AdminDto;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import com.eascapeco.cinemapr.bo.security.token.AjaxAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

@Slf4j
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    public RestAuthenticationProvider(final AdminRepository adminRepository, final PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.info("username {}", username);
        log.info("password {}", password);

        final Admin admin = adminRepository.findByAdmId(username);
        log.info("login info");
        log.info("admin id {}", admin.getAdmId());
        log.info("admin pw {}", admin.getPwd());

        if (!passwordEncoder.matches(password, admin.getPwd())) {
            throw new BadCredentialsException("Invalid Username or Password");
        }

        log.info("pw 일치 ");
        AdminDto adminDto = new AdminDto();
        adminDto.setAdmId(admin.getAdmId());
        adminDto.setAdmNo(admin.getAdmNo());

        return new AjaxAuthenticationToken(adminDto, null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AjaxAuthenticationToken.class);
    }
}
