package com.eascapeco.cinemapr.bo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 86400)
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = -3324566589694238922L;

    @Id
    private String uid;

    private Long admNo;

    private String refreshToken;

}
