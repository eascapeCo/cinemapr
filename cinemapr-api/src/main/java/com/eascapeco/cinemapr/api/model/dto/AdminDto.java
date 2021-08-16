package com.eascapeco.cinemapr.api.model.dto;

import com.eascapeco.cinemapr.api.model.entity.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto implements UserDetails {

    private Long admNo;
    private String admId;
    private String pwd;
    private List<AdminRole> adminRole;
    private Collection<GrantedAuthority> authorities;
    private LocalDateTime regDate;
    private Long regNo;
    private LocalDateTime modDate;
    private Long modNo;
    private Boolean useYn;
    private Boolean pwdExpd;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities = adminRole.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().getRolNm()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.admId;
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
