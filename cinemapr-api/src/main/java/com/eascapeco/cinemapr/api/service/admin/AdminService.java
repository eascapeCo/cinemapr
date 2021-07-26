package com.eascapeco.cinemapr.api.service.admin;

import com.eascapeco.cinemapr.api.exception.ResourceAlreadyInUseException;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 관리자 service
 *
 * @author jaehankim
 * @Date 2019. 10. 10
 */
@Service @RequiredArgsConstructor
public class AdminService {

    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;
//    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Finds a admin in the database by username
     */
    public Admin findByUsername(String admId) {
        return adminRepository.findByAdmId(admId);
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<Admin> registerAdmin(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestAdminId = newRegistrationRequest.getAdmId();
        if (adminRepository.existsByadmId(newRegistrationRequestAdminId)) {
            logger.error("Admin Id already exists: " + newRegistrationRequestAdminId);
            throw new ResourceAlreadyInUseException("Admin Id", "Admin", newRegistrationRequestAdminId);
        }
        logger.info("Trying to register new user [" + newRegistrationRequestAdminId + "]");
        Admin newAdmin = createAdmin(newRegistrationRequest);
        Admin registeredNewAdmin = adminRepository.save(newAdmin);
        return Optional.ofNullable(registeredNewAdmin);
    }

    /**
     * Creates a new user from the registration request
     */
    private Admin createAdmin(RegistrationRequest newRegistrationRequest) {

        Admin newAdmin = new Admin();
        newAdmin.setAdmId(newRegistrationRequest.getAdmId());
        newAdmin.setPwd(passwordEncoder.encode("1234"));
        newAdmin.createData(1L);

        return newAdmin;
    }

}