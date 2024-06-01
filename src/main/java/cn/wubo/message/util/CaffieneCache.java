package cn.wubo.message.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CaffieneCache {

    private static ConcurrentMap<String, Cache<Object, Object>> caches = new ConcurrentHashMap<>();
    private static final String TOKEN = "token";

    private CaffieneCache() {
    }

    /**
     * 获取指定名称的缓存对象。如果该缓存不存在，则根据提供的秒数创建一个新的缓存对象。
     *
     * @param cacheName 缓存的名称，用于标识缓存。
     * @param seconds   缓存的过期时间，单位为秒。用于新建缓存时设置其过期时间。
     * @return 返回指定名称的缓存对象，如果存在则直接返回，不存在则创建后返回。
     */
    public static Cache<Object, Object> getCache(String cacheName, Long seconds) {
        Assert.notNull(cacheName, "cacheName value should bot be null!");
        Assert.notNull(seconds, "seconds value should bot be null!");
        // @format:off
        // 尝试从缓存集合中获取指定名称的缓存，如果不存在，则构建一个新的缓存并添加到集合中
        return caches.getOrDefault(cacheName, Caffeine.newBuilder() // 使用Caffeine构建器配置新的缓存
                .expireAfterWrite(Duration.ofSeconds(seconds)) // 设置缓存的过期时间
                .build() // 构建并返回缓存对象
        );
        // @format:on
    }

    /**
     * 获取指定缓存名称的令牌。
     *
     * @param cacheName 缓存的名称，用于标识特定的缓存。
     * @return 如果缓存中存在指定名称的缓存项，则返回对应的令牌字符串；如果不存在或缓存为空，则返回null。
     */
    public static String getToken(String cacheName) {
        // 检查缓存是否包含指定的缓存名称
        if (caches.containsKey(cacheName)) {
            return null;
        } else {
            // 尝试从缓存中获取名为TOKEN的项
            return (String) caches.get(cacheName).getIfPresent(TOKEN);
        }
    }

    /**
     * 将token设置到指定的缓存中，并返回该token。
     *
     * @param cacheName  缓存的名称，不能为null。
     * @param tokenValue 要设置的token值，不能为null。
     * @return 如果缓存中之前已经存在了token，则返回之前的token值；否则返回null。
     */
    public static String setToken(String cacheName, Long seconds, Object tokenValue) {
        // 确保tokenValue不为null
        Assert.notNull(tokenValue, "token value should bot be null!");
        // 通过cacheName获取缓存对象
        Cache<Object, Object> cache = getCache(cacheName, seconds);
        // 将tokenValue设置到缓存中，键为TOKEN
        cache.put(TOKEN, tokenValue);
        // 返回缓存中当前的token值
        return (String) cache.getIfPresent(TOKEN);
    }
}
