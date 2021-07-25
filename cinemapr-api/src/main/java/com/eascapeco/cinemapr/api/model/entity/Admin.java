package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Admin extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admNo;
    private String admId;
    private String pwd;
    private Boolean useYn;
    private Boolean pwdExpd;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AdminRole> adminRoles = new ArrayList<>();
}
