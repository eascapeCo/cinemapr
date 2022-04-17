package com.eascapeco.cinemapr.bo.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash(timeToLive = 7)
public class RefreshToken implements Serializable {
    private static final long serialVersionUID = -7353484588260422449L;
    private Long admNo;
    private String refreshToken;

}
