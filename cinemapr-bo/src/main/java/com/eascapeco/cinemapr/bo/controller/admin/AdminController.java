package com.eascapeco.cinemapr.bo.controller.admin;

import com.eascapeco.cinemapr.api.exception.UserRegistrationException;
import com.eascapeco.cinemapr.api.model.payload.ApiResponse;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.bo.service.admin.BoAdminService;
import com.eascapeco.cinemapr.bo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final BoAdminService boAdminService;
    private final AuthService authService;

    @PostMapping("/admin")
    public ResponseEntity insertAdmin(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("userId : {}, Roles : {}, RequestHeader : {}", registrationRequest.getAdmId());

        return boAdminService.registerAdmin(registrationRequest)
            .map(admin -> {
                log.info("Registered User returned [API[: " + admin);
                return ResponseEntity.ok(new ApiResponse(true, "User registered successfully."));
            })
            .orElseThrow(() -> new UserRegistrationException(registrationRequest.getAdmId(), "Missing admin object in database"));
    }

}
