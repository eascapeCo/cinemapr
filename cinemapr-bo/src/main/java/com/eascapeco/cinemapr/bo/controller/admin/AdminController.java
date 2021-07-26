package com.eascapeco.cinemapr.bo.controller.admin;

import com.eascapeco.cinemapr.api.exception.UserRegistrationException;
import com.eascapeco.cinemapr.api.model.payload.ApiResponse;
import com.eascapeco.cinemapr.api.model.payload.RegistrationRequest;
import com.eascapeco.cinemapr.api.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    @PostMapping("/admin")
    public ResponseEntity insertAdmin(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("userId : {}, Roles : {}, RequestHeader : {}", registrationRequest.getAdmId());

        return adminService.registerAdmin(registrationRequest)
            .map(admin -> {
                logger.info("Registered User returned [API[: " + admin);
                return ResponseEntity.ok(new ApiResponse(true, "User registered successfully."));
            })
            .orElseThrow(() -> new UserRegistrationException(registrationRequest.getAdmId(), "Missing admin object in database"));
    }

}
