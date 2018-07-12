package com.bfchengnuo.ssmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 冰封承諾Andy on 2018/7/11.
 */
public class TokenCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
    // 当超过最大容量后会使用 LRU（最少使用）算法来移除
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(3000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    // 默认数据加载实现，当调用 get 取不到值时，就调用这个方法进行加载，替换为字符串 null 避免 equals 的 NPE
                    return "null";
                }
            });

    public static void setKey(String key, String val) {
        loadingCache.put(key,val);
    }

    public static String getVal(String key) {
        try {
            String val = loadingCache.get(key);
            if (!"null".equals(val)) {
                return val;
            }
        } catch (ExecutionException e) {
            LOGGER.error("localCache get error ->" + e);
        }
        return null;
    }
}
