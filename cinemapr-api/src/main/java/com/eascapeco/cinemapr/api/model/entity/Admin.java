package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@RequiredArgsConstructor
public class Admin extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "admin_no_seq", allocationSize = 1)
    private Long admNo;

    @NaturalId
    @Column(name = "admin_id", unique = true)
    @NotBlank(message = "Admin Id cannot be null")
    private String admId;

    @NotNull(message = "Password cannot be null")
    private String pwd;

    @ColumnDefault("true")
    private Boolean useYn;

    @ColumnDefault("true")
    private Boolean pwdExpd;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AdminRole> adminRoles = new ArrayList<>();

    public Admin(Admin admin) {
        admNo = admin.getAdmNo();
        admId = admin.getAdmId();
        pwd = admin.getPwd();
        useYn = admin.getUseYn();
        pwdExpd = admin.getPwdExpd();
        adminRoles = admin.getAdminRoles();
    }

}
