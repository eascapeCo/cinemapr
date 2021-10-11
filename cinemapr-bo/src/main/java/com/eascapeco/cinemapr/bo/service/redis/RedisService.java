package com.eascapeco.cinemapr.bo.service.redis;

import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String pushByRefreshToken(AdminDto adminDto, RefreshToken refreshToken) {
        UUID uuid = UUID.randomUUID();

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(uuid.toString(), refreshToken);

        return uuid.toString();
    }

}
