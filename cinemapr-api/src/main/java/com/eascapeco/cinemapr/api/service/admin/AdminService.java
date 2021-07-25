package com.eascapeco.cinemapr.api.service.admin;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 관리자 service
 *
 * @author jaehankim
 * @Date 2019. 10. 10
 */
@Service @RequiredArgsConstructor
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    /**
     * Finds a admin in the database by username
     */
    public Admin findByUsername(String admId) {
        return adminRepository.findByAdmId(admId);
    }

}