package com.eascapeco.cinemapr.bo.model.dto;

import com.eascapeco.cinemapr.api.model.entity.AdminRole;
import lombok.*;
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
@ToString
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
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRolNm()))
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
