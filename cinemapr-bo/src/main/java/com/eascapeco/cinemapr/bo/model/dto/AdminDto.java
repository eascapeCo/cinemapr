package com.eascapeco.cinemapr.bo.model.dto;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.entity.AdminRole;
import com.eascapeco.cinemapr.api.model.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class AdminDto implements UserDetails {
    private Long admNo;
    private String admId;
    private String pwd;
    private List<AdminRole> adminRoles;
    private Boolean useYn;
    private Boolean pwdExpd;
    private Collection<SimpleGrantedAuthority> authorities;

    public AdminDto(Admin admin) {
        admNo = admin.getAdmNo();
        admId = admin.getAdmId();
        pwd = admin.getPwd();
        useYn = admin.getUseYn();
        pwdExpd = admin.getPwdExpd();
        authorities = admin.getAdminRoles().stream()
            .map(AdminRole::getRole)
            .map(Role::getRolNm)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    public AdminDto(Long admNo) {
        this.admNo = admNo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return getPwd();
    }

    @Override
    public String getUsername() {
        return getAdmId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getPwdExpd();
    }

    @Override
    public boolean isEnabled() {
        return getUseYn();
    }

}
