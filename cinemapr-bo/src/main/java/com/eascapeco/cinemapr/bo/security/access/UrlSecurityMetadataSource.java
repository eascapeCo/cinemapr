package com.eascapeco.cinemapr.bo.security.access;

import com.eascapeco.cinemapr.bo.service.security.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    // 그룹권한 리소스 정보
    private final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;
    // private LinkedHashMap<AntPathRequestData, List<ConfigAttribute>> requestMap;

    private final SecurityResourceService securityResourceService;

    public UrlSecurityMetadataSource(final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap, final SecurityResourceService securityResourceService) {
        this.requestMap = resourcesMap;
        this.securityResourceService = securityResourceService;
        // requestMap.put(new AntPathRequestMatcher("/api/menus/"), List.of(new SecurityConfig("ROLE_MANAGER")));
        // requestMap.put(new AntPathRequestMatcher("/sample/sample-editor"), List.of(new SecurityConfig("ROLE_MANAGER")));
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> result = null;
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest httpServletRequest = fi.getHttpRequest();

        log.info("url : {} =================================== " + "method : {}", fi.getRequest().getRequestURI(),
            fi.getRequest().getMethod());

        if (requestMap != null) {
            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                if (matcher.matches(httpServletRequest)) {
                    result = entry.getValue();
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
