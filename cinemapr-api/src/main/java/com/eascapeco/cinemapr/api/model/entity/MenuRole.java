package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.id.MenuRoleId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(MenuRoleId.class)
@NoArgsConstructor
@Getter @Setter
public class MenuRole implements Serializable {

    @Id
    private Long mnuNo;

    @Id
    private Long rolNo;

    private boolean mnuCreate;

    private boolean mnuRead;

    private boolean mnuUpdate;

    private boolean mnuDelete;

    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "mnuNo", insertable = false, updatable = false)
    private Menu menu;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "rolNo", insertable = false, updatable = false)
    private Role role;
}
