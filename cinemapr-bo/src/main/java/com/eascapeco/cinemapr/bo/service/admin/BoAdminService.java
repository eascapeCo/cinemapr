package com.eascapeco.cinemapr.bo.service.admin;

import com.eascapeco.cinemapr.api.exception.ResourceAlreadyInUseException;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoAdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /**
     * Finds a admin in the database by username
     * @param admId
     * @return
     */
    public Admin findByAdmId(String admId) {
        return adminRepository.findByAdmId(admId);
    }

    /**
     * Finds a admin in the database by admin no
     * @param adminNo
     * @return
     */
    public Admin findByAdmNo(Long adminNo) {
        return adminRepository.findByAdmNo(adminNo);
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<Admin> registerAdmin(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestAdminId = newRegistrationRequest.getAdmId();
        if (adminRepository.existsByAdmId(newRegistrationRequestAdminId)) {
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
