package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Menu extends BaseEntity {

    @Id @GeneratedValue
    private Long mnuNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preMnuNo")
    private Menu parentMenu;
    private String mnuName;
    private int mnuLv;
    private String urlAdr;
    private boolean useYn;
    private boolean dpYn;
    private int dpSequence;
    @OneToMany(mappedBy = "parentMenu")
    private List<Menu> subMenus;
}
