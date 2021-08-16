package com.eascapeco.cinemapr.bo.service.admin;

import com.eascapeco.cinemapr.api.exception.ResourceAlreadyInUseException;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BoAdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public BoAdminService(AdminRepository adminRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a admin in the database by username
     * @return
     */
    public Admin findByAdmId(String admId) {
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
            log.error("Admin Id already exists: " + newRegistrationRequestAdminId);
            throw new ResourceAlreadyInUseException("Admin Id", "Admin", newRegistrationRequestAdminId);
        }
        log.info("Trying to register new user [" + newRegistrationRequestAdminId + "]");
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
