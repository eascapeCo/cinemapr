package com.eascapeco.cinemapr.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AdminDto {

    private Long admNo;
    private String id;
    private String pwd;
    private LocalDateTime regDate;
    private Long regNo;
    private LocalDateTime modDate;
    private Long modNo;
    private Boolean useYn;
    private Boolean pwdExpd;

}
