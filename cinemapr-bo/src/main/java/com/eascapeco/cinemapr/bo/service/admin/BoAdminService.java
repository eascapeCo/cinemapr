package com.eascapeco.cinemapr.bo.service.admin;

import com.eascapeco.cinemapr.api.exception.ResourceAlreadyInUseException;
import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.api.repository.AdminRepository;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * Finds a admin in the database by username
     * @param adminId
     * @return
     */
    @Override
    public AdminDto loadUserByUsername(String adminId) throws UsernameNotFoundException {
        Optional<Admin> findAdmin = adminRepository.findByAdmId(adminId);
        return findAdmin.map(AdminDto::new)
            .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching AdminId in the database for " + adminId));
    }


    /**
     * Finds a admin in the database by admin no
     * @param adminNo
     * @return
     */
    public AdminDto findByAdmNo(Long adminNo) {
        Optional<Admin> findAdmin = adminRepository.findByAdmNo(adminNo);
        return findAdmin
            .map(AdminDto::new)
            .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + adminNo));
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
