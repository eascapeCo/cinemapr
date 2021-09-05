package com.eascapeco.cinemapr.api.model.entity.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Column(updatable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime regDate;
    @Column(updatable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private Long regNo;

    private LocalDateTime modDate;

    private Long modNo;

    public void createData(Long userNo) {
        LocalDateTime now = LocalDateTime.now();
        this.regDate = now;
        this.modDate = now;
        this.regNo = userNo;
        this.modNo = userNo;
    }

    public void updateData(Long userNo) {
        this.modDate = LocalDateTime.now();
        this.modNo = userNo;
    }
}
