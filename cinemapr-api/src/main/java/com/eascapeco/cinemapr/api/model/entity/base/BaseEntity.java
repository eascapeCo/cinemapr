package com.eascapeco.cinemapr.api.model.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {

    private LocalDateTime regDate;

    private Long regNo;

    private LocalDateTime modDate;

    private Long modNo;
}
