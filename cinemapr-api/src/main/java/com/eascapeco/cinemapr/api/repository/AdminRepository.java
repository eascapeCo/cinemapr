package com.eascapeco.cinemapr.api.repository;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByAdmId(String admId);

}
