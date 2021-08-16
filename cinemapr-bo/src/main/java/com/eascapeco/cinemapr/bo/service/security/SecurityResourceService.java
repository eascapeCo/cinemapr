package com.eascapeco.cinemapr.bo.service.security;

import com.eascapeco.cinemapr.api.model.entity.MenuRole;
import com.eascapeco.cinemapr.api.repository.MenuRoleRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SecurityResourceService {

    private final MenuRoleRepository menuRoleRepository;

    public SecurityResourceService(final MenuRoleRepository menuRoleRepository) {
        this.menuRoleRepository = menuRoleRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        List<MenuRole> resourcesList = menuRoleRepository.findAllMenuRole();
        /*
        resourcesList.forEach(re -> {
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            re.getRoleSet().forEach(role -> {
                configAttributeList.add(new SecurityConfig(role.getRoleName()));
                result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributeList);
            });
        });

         */

        return result;
    }
}
