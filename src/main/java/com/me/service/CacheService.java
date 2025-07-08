package com.me.service;

import com.me.dao.ElderDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final ElderDao elderDao;
    @Cacheable(
            value = "elderCount",
            key = "#query != null ? #query : 'null'",
            unless = "#result == null"
    )
    public Long elderCount(String query) {
        return elderDao.countByCondition(query);
    }
}
