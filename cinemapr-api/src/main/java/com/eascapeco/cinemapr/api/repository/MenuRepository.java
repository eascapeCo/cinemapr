package com.eascapeco.cinemapr.api.repository;

import com.eascapeco.cinemapr.api.model.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m from Menu m where m.useYn = true " +
        "and m.dpYn = true " +
        "and exists (select mr from MenuRole mr where mr.rolNo in (select ar.rolNo from adm_role ar where ar.rolNo = :admNo)" +
        "and mr.mnuNo = m.mnuNo)" +
        "order by m.dpNo, m.parentMenu.mnuNo")
    List<Menu> findAllMenus(@Param("admNo") Long id);
}
