package com.eascapeco.cinemapr.bo.model.dto;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import com.eascapeco.cinemapr.api.model.entity.AdminRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    private Collection<GrantedAuthority> authorities;

    public AdminDto(Admin admin) {
        admNo = admin.getAdmNo();
        admId = admin.getAdmId();
        pwd = admin.getPwd();
        adminRoles = admin.getAdminRoles();
        useYn = admin.getUseYn();
        pwdExpd = admin.getPwdExpd();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAdminRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRolNm()))
                        .collect(Collectors.toList());
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
