package com.eascapeco.cinemapr.api.model.entity;

import com.eascapeco.cinemapr.api.model.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolNo;

    private String rolNm;


}
