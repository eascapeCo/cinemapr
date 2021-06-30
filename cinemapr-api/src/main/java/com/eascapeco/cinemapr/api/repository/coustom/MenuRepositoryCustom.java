package com.eascapeco.cinemapr.api.repository.coustom;

import com.eascapeco.cinemapr.api.model.entity.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findByMenuList();
}
