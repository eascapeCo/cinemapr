package com.eascapeco.cinemapr.api.service;

import com.eascapeco.cinemapr.api.model.entity.Menu;
import com.eascapeco.cinemapr.api.repository.coustom.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public Menu createMenu(Menu menu) {

        return this.menuRepository.save(menu);
    }

}
