package com.eascapeco.cinemapr.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class MenuDto {
    private Long mnuNo;
    @Min(0)
    private Long preMnuNo;
    @NotBlank(message = "메뉴명은 필수 입니다.")
    private String mnuName;
    @Min(0) @Max(3)
    private int mnuLv;
    @NotBlank(message = "URL은 필수 입니다.")
    private String urlAdr;
    private boolean useYn;
    private boolean dpYn;
    private int dpNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    private Long regNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modDate;
    private Long modNo;
    private List<MenuDto> subMenus;
    @JsonIgnore
    private Integer admNo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String createType;

    public MenuDto(Long mnuNo, boolean useYn, boolean dpYn, int dpNo, LocalDateTime modDate, Long modNo, LocalDateTime regDate, Long regNo) {
        this.mnuNo = mnuNo;
        this.useYn = useYn;
        this.dpYn = dpYn;
        this.dpNo = dpNo;
        this.modDate = modDate;
        this.modNo = modNo;
        this.regDate = regDate;
        this.regNo = regNo;
    }
}
