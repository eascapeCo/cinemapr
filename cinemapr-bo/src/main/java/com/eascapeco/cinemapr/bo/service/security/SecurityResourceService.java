package com.eascapeco.cinemapr.bo.service.security;

import com.eascapeco.cinemapr.api.model.entity.MenuRole;
import com.eascapeco.cinemapr.api.repository.MenuRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class SecurityResourceService {

    private final MenuRoleRepository menuRoleRepository;

    public SecurityResourceService(final MenuRoleRepository menuRoleRepository) {
        this.menuRoleRepository = menuRoleRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<ConfigAttribute> attributes = null;

        List<MenuRole> resourcesList = menuRoleRepository.findAllMenuRole();
        AntPathRequestMatcher presentResource;
        for (MenuRole resource : resourcesList) {
            if (resource.isMnuCreate()) {
                presentResource = new AntPathRequestMatcher(resource.getMenu().getUrlAdr(), HttpMethod.POST.name());
                attributes = result.getOrDefault(presentResource, new ArrayList<>());
                attributes.add(new SecurityConfig("ROLE_" + resource.getRole().getRolNm()));
                result.put(presentResource, attributes);
            }

            if (resource.isMnuRead()) {
                presentResource = new AntPathRequestMatcher(resource.getMenu().getUrlAdr(), HttpMethod.GET.name());
                attributes = result.getOrDefault(presentResource, new ArrayList<>());
                attributes.add(new SecurityConfig("ROLE_" + resource.getRole().getRolNm()));
                result.put(presentResource, attributes);
            }

            if (resource.isMnuUpdate()) {
                presentResource = new AntPathRequestMatcher(resource.getMenu().getUrlAdr(), HttpMethod.PUT.name());
                attributes = result.getOrDefault(presentResource, new ArrayList<>());
                attributes.add(new SecurityConfig("ROLE_" + resource.getRole().getRolNm()));
                result.put(presentResource, attributes);
            }

            if (resource.isMnuDelete()) {
                presentResource = new AntPathRequestMatcher(resource.getMenu().getUrlAdr(), HttpMethod.DELETE.name());
                attributes = result.getOrDefault(presentResource, new ArrayList<>());
                attributes.add(new SecurityConfig("ROLE_" + resource.getRole().getRolNm()));
                result.put(presentResource, attributes);
            }
        }

        log.info("resourceMap.toString() {}", result.toString());
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
