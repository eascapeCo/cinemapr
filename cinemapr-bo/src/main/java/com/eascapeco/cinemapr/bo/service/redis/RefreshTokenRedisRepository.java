package com.eascapeco.cinemapr.bo.service.redis;

import com.eascapeco.cinemapr.bo.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
