package com.eascapeco.cinemapr.bo;

import com.eascapeco.cinemapr.api.model.entity.Menu;
import com.eascapeco.cinemapr.api.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements ApplicationRunner {

    private final MenuService menuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("!!!!");

        // menu 생성
        Menu rootMenu = new Menu();
        rootMenu.setMnuName("root-1");
        rootMenu.setMnuLv(1);
        rootMenu.setUseYn(true);
        rootMenu.setDpSequence(1);
        rootMenu.setRegDate(LocalDateTime.now());
        rootMenu.setRegNo(1);
        rootMenu.setModDate(LocalDateTime.now());
        rootMenu.setModNo(1);
        Menu rootM = this.menuService.createMenu(rootMenu);

        Menu subMenu = new Menu();
        subMenu.setMnuName("root-1 sub-1");
        subMenu.setMnuLv(2);
        subMenu.setParentMenu(rootM);
        subMenu.setUseYn(true);
        subMenu.setDpSequence(10);
        subMenu.setRegDate(LocalDateTime.now());
        subMenu.setRegNo(1);
        subMenu.setModDate(LocalDateTime.now());
        subMenu.setModNo(1);
        Menu subM = this.menuService.createMenu(subMenu);

        Menu subMenu2 = new Menu();
        subMenu2.setMnuName("root-1 sub-2");
        subMenu2.setMnuLv(2);
        subMenu2.setParentMenu(rootM);
        subMenu2.setUseYn(true);
        subMenu2.setDpSequence(20);
        subMenu2.setRegDate(LocalDateTime.now());
        subMenu2.setRegNo(1);
        subMenu2.setModDate(LocalDateTime.now());
        subMenu2.setModNo(1);
        this.menuService.createMenu(subMenu2);
/*
        Menu subMenubySub = new Menu();
        subMenubySub.setMnuName("root-1 sub-1 by sub-1");
        subMenubySub.setMnuLv(3);
        subMenubySub.setDpSequence(subM.getMnuNo());
        subMenubySub.setUseYn(true);
        subMenubySub.setDispNo(10);
        subMenubySub.setRegDate(LocalDateTime.now());
        subMenubySub.setRegNo(1);
        subMenubySub.setModDate(LocalDateTime.now());
        subMenubySub.setModNo(1);

        this.menuService.createMenu(subMenubySub);

        Menu subMenubySub2 = new Menu();
        subMenubySub2.setMnuName("root-1 sub-1 by sub-1");
        subMenubySub2.setMnuLv(3);
        subMenubySub2.setPreMnuNo(subM.getMnuNo());
        subMenubySub2.setUseYn(true);
        subMenubySub2.setDispNo(10);
        subMenubySub2.setRegDate(LocalDateTime.now());
        subMenubySub2.setRegNo(1);
        subMenubySub2.setModDate(LocalDateTime.now());
        subMenubySub2.setModNo(1);

        this.menuService.createMenu(subMenubySub2);

 */
    }
}
