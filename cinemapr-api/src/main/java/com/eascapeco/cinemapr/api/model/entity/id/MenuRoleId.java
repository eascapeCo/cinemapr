package com.eascapeco.cinemapr.api.model.entity.id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@Getter @Setter
public class MenuRoleId implements Serializable {
    private Long mnuNo;

    private Long rolNo;
}
