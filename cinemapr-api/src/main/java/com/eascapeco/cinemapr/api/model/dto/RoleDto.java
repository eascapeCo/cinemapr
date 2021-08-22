package com.eascapeco.cinemapr.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoleDto {
    private Long rolNo;
    private String rolNm;
    private String rolDesc;
    private LocalDateTime regDate;
    private Long regNo;
    private LocalDateTime modDate;
    private Long modNo;

    public RoleDto() {
    }

    public RoleDto(Long rolNo, String rolNm, String rolDesc, LocalDateTime regDate, Long regNo, LocalDateTime modDate, Long modNo) {
        this.rolNo = rolNo;
        this.rolNm = rolNm;
        this.rolDesc = rolDesc;
        this.regDate = regDate;
        this.regNo = regNo;
        this.modDate = modDate;
        this.modNo = modNo;
    }
}
