package com.eascapeco.cinemapr.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long rolNo;
    private String rolNm;
    private String rolDesc;
    private String regDate;
    private Long regNo;
    private String modDate;
    private Long modNo;
}
