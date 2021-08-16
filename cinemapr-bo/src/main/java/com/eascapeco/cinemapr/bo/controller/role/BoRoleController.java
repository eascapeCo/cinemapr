package com.eascapeco.cinemapr.bo.controller.role;

import com.eascapeco.cinemapr.api.model.dto.RoleDto;
import com.eascapeco.cinemapr.api.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoRoleController {
    private final RoleService roleService;

    /*
    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody final Menu request) {
        final Menu response = menuService.create(request);
        return ResponseEntity.created(URI.create("/api/menus/" + response.getId()))
            .body(response);
    }

    @PutMapping("/{menuId}/price")
    public ResponseEntity<Menu> changePrice(@PathVariable final UUID menuId, @RequestBody final Menu request) {
        return ResponseEntity.ok(menuService.changePrice(menuId, request));
    }
     */
    @GetMapping("/roles")
    public ResponseEntity<Page<RoleDto>> getRoles(RoleDto roles) {
        log.info("get roles");
        return ResponseEntity.ok(this.roleService.getRoles(roles));
    }

    @GetMapping("/roles/{id]")
    public RoleDto getOneRoles(@PathVariable Integer id) {
        throw new RuntimeException();
        // return this.rolesService.getOneRoles(id);
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDto> saveRoles(/*@AuthenticationPrincipal AdminDto loginUser, */@RequestBody RoleDto roleDto) {
        // log.info("{}", loginUser);


        RoleDto result = this.roleService.createRole(roleDto, 1L);

        URI localtion = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{rolNo}")
            .buildAndExpand(result.getModNo())
            .toUri();



        return ResponseEntity.created(localtion).build();
    }
    @GetMapping("/roleList")
    public List<RoleDto> getRoleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}