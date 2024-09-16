package com.example.gamehubbackend.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    /**
     * Configure the CacheManager bean.
     *
     * @return a CacheManager instance configured with "games" and "gameDetail" caches
     */
    @Bean
    public CacheManager cacheManager() {
        // ConcurrentMapCacheManager provides a simple in-memory cache implementation.
        // This setup will create and manage caches named "games" and "gameDetail".
        return new ConcurrentMapCacheManager("games", "gameDetail");
    }
}