package com.eascapeco.cinemapr.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MenuDto {
    private Long mnuNo;
    // @Min(0)
    private Long preMnuNo;
    // @NotBlank(message = "메뉴명은 필수 입니다.")
    private String mnuName;
    // @Min(0) @Max(3)
    private int mnuLv;
    // @NotBlank(message = "URL은 필수 입니다.")
    private String urlAdr;
    private boolean useYn;
    private boolean dpYn;
    private int dpSequence;
    private LocalDateTime regDate;
    private Long regNo;
    private LocalDateTime modDate;
    private Long modNo;
    private List<MenuDto> subMenus;
    @JsonIgnore
    private Integer admNo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String createType;
}
