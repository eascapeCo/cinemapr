package com.eascapeco.cinemapr.bo.model.dto;

import com.eascapeco.cinemapr.api.model.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AdminDto extends Admin implements UserDetails {

    public AdminDto(final Admin admin) {
        super(admin);
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
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
