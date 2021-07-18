package com.eascapeco.cinemapr.api.model.entity.id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter @Setter
public class AdminRoleId implements Serializable {

    private Long admNo;

    private Long rolNo;

}
