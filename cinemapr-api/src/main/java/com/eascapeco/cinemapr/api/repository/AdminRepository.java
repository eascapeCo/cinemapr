package com.eascapeco.cinemapr.api.repository;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdmId(String admId);

    Boolean existsByAdmId(String adminId);

}
