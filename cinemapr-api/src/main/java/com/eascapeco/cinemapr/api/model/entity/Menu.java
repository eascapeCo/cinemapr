package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Menu extends BaseEntity {
    private static final List<String> menuTypes = List.of("siblingLevel", "subLevel");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mnuNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preMnuNo")
    private Menu parentMenu;
    private String mnuName;
    private int mnuLv;
    private String urlAdr;
    private boolean useYn;
    private boolean dpYn;
    private int dpNo;
    @OneToMany(mappedBy = "parentMenu")
    private List<Menu> subMenus;

    public Menu(String mnuName, Menu parentMenu, int mnuLv, String urlAdr, boolean useYn, boolean dpYn, int dpNo, Long userNo) {
        this.mnuName = mnuName;
        this.parentMenu = parentMenu;
        this.mnuLv = mnuLv;
        this.urlAdr = urlAdr;
        this.useYn = useYn;
        this.dpYn = dpYn;
        this.dpNo = dpNo;

        super.createData(userNo);
    }
    public void changeData(String mnuName, Menu parentMenu, int mnuLv, String urlAdr, boolean useYn, boolean dpYn, int dpNo) {
        this.mnuName = mnuName;
        this.parentMenu = parentMenu;
        this.mnuLv = mnuLv;
        this.urlAdr = urlAdr;
        this.useYn = useYn;
        this.dpYn = dpYn;
        this.dpNo = dpNo;
    }
}
