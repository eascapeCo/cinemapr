package com.eascapeco.cinemapr.api.service.role;

import com.eascapeco.cinemapr.api.model.dto.RoleDto;
import com.eascapeco.cinemapr.api.model.entity.Role;
import com.eascapeco.cinemapr.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    public Page<RoleDto> getRoles(RoleDto roleDto) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Role> roles = roleRepository.findAll(pageRequest);

        return roles.map(role ->
            new RoleDto(
                role.getRolNo(),
                role.getRolNm(),
                role.getRolDesc(),
                role.getRegDate(),
                role.getRegNo(),
                role.getModDate(),
                role.getModNo()));
    }

    public RoleDto getOneRoles(Long id) {
        return null;
    }

    /**
     *
     * @param request
     * @param admNo
     * @return roleDto
     */
    public RoleDto createRole(RoleDto request, Long admNo) {

        Role data = new Role();
        //data.registerRoleName(request.getRolNm(), request.getRolDesc(), admNo);

        Role result = roleRepository.save(data);
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(roleDto, result);

        return roleDto;
    }

    public List<RoleDto> getRoleList() {
        return null;
    }
}
