package com.eascapeco.cinemapr.api.repository;

import com.eascapeco.cinemapr.api.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
