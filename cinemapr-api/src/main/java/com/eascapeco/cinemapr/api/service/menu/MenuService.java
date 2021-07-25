package com.eascapeco.cinemapr.api.service.menu;

import com.eascapeco.cinemapr.api.exception.BadRequestException;
import com.eascapeco.cinemapr.api.model.dto.MenuDto;
import com.eascapeco.cinemapr.api.model.entity.Menu;
import com.eascapeco.cinemapr.api.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * 메뉴 생성
     * @param menuDto
     * @return
     */
    public MenuDto createMenu(MenuDto menuDto) {
        List<String> createTypes = List.of("siblingLevel", "subLevel");
        if (!createTypes.contains(menuDto.getCreateType())) {
            throw new BadRequestException("유효하지 않는 생성 타입");
        }

        Menu currentMenu = this.menuRepository.findById(menuDto.getMnuNo()).orElseThrow(BadRequestException::new);

        int currentLevel = currentMenu.getMnuLv();
        Menu preMenu = currentMenu.getParentMenu();

        // 등록하려하는 메뉴가 동일 레벨인지 하위 레벨인지를 판단
        if(StringUtils.equals(menuDto.getCreateType(), "subLevel")) {
            preMenu = currentMenu;
            currentLevel++;
        }

        Menu savedMenu = new Menu(menuDto.getMnuName(), preMenu, currentLevel, menuDto.getUrlAdr(), menuDto.isUseYn(), menuDto.isDpYn(), menuDto.getDpNo(), 1L);

        this.menuRepository.save(savedMenu);

        return new MenuDto(savedMenu.getMnuNo(), savedMenu.isUseYn(), savedMenu.isDpYn(), savedMenu.getDpNo(), savedMenu.getModDate(), savedMenu.getModNo(), savedMenu.getRegDate(), savedMenu.getRegNo());
    }

    public MenuDto getMenu(Long mnuNo) {

        Menu findMenu = menuRepository.findById(mnuNo).orElseThrow(BadRequestException::new);
        MenuDto menuDto = new MenuDto(findMenu.getMnuNo(), findMenu.isUseYn(), findMenu.isDpYn(), findMenu.getDpNo(), findMenu.getModDate(), findMenu.getModNo(), findMenu.getRegDate(), findMenu.getRegNo());

        return menuDto;
    }
}
