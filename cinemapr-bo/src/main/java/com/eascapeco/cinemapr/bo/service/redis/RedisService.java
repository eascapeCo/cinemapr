/*
package com.eascapeco.cinemapr.bo.service.redis;

import com.eascapeco.cinemapr.bo.model.RefreshToken;
import com.eascapeco.cinemapr.bo.model.dto.AdminDto;
import com.eascapeco.cinemapr.bo.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void pushByRefreshToken(AdminDto adminDto, RefreshToken refreshToken, HttpServletResponse response) {
        UUID uuid = UUID.randomUUID();
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(uuid.toString(), refreshToken);
        CookieUtils.setCookie("uid", uuid.toString(), 7 * 24 * 60 * 60, response);
    }

    public Object getValue(String key) {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        return vop.get(key);
    }
}
*/
