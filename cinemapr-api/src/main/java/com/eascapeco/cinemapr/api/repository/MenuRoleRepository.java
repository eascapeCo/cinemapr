package com.eascapeco.cinemapr.api.repository;

import com.eascapeco.cinemapr.api.model.entity.MenuRole;
import com.eascapeco.cinemapr.api.model.entity.id.MenuRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRoleRepository extends JpaRepository<MenuRole, MenuRoleId> {

    @Query("select mr from MenuRole mr join fetch mr.menu join fetch mr.role where mr.menu.dpYn = true and mr.menu.useYn = true")
    List<MenuRole> findAllMenuRole();
}
