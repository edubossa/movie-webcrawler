package br.com.webcrawler.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class ApplicationCache {

    @Value("${cache.name}")
    private String cacheName;

    @Value("${cache.timeout}")
    private Long timeout;

    @Bean
    public CacheManager cacheManager() {
        log.info("[[MovieWebcrawler] - cacheManager - inicializado");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder().initialCapacity(5000).maximumSize(5000)
                .expireAfterAccess(timeout, TimeUnit.MINUTES).recordStats();
    }

}
