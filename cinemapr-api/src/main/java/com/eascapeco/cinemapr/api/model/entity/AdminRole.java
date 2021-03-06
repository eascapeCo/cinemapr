package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;
import com.eascapeco.cinemapr.api.model.entity.id.AdminRoleId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(AdminRoleId.class)
@Getter
@Setter
public class AdminRole extends BaseEntity implements Serializable {

    @Id
    private Long admNo;
    @Id
    private Long rolNo;

    @ManyToOne(targetEntity = Admin.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "admNo", insertable = false, updatable = false)
    private Admin admin;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "rolNo", insertable = false, updatable = false)
    private Role role;

}
