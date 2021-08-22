package com.eascapeco.cinemapr.bo.controller.menu;

import com.eascapeco.cinemapr.api.model.dto.MenuDto;
import com.eascapeco.cinemapr.api.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoMenuController {

    private final MenuService menuService;

    @GetMapping("/menus")
    public List<MenuDto> getMenuList() {

        return menuService.getMenuList();
    }

    @GetMapping("/menus/{id}")
    public MenuDto getMenu(@PathVariable Long id) {

        return new MenuDto();
    }

    @RequestMapping(value = "/menus", method = RequestMethod.POST)
    public ResponseEntity<MenuDto> saveMenu(/*@AuthenticationPrincipal Admin loginUser, */@RequestBody MenuDto menuDto) {
        /*
        log.info("user name -> {}", loginUser.getUsername());
        log.info("user admNo -> {}", loginUser.getAdmNo());
         */
        log.info("{}", menuDto);
        log.info("!@# {}", menuDto.getCreateType());

        MenuDto savedMenu = this.menuService.createMenu(menuDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{mnuNo}")
            .buildAndExpand(savedMenu.getMnuNo())
            .toUri();
        log.info("menu {} ", menuDto.toString());

        return ResponseEntity.created(location).build();
    }

}
