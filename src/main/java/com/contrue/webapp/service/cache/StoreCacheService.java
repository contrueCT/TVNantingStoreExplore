package com.contrue.webapp.service.cache;

import com.contrue.Framework.annotation.Component;
import com.contrue.util.JsonParser;
import com.contrue.util.RedisConnectionManager;
import com.contrue.webapp.entity.dto.PageResult;
import com.contrue.webapp.entity.po.Store;
import redis.clients.jedis.Jedis;

/**
 * @author confff
 */
@Component
public class StoreCacheService {
    private static final String STORE_KEY_PREFIX = "cache:store:";
    private static final String STORE_PAGE_KEY_PREFIX = "cache:store:page:";
    //30分钟过期
    private static final int EXPIRE_TIME_SECONDS = 60 * 30;


    /**
     * 将商铺信息存入缓存
     * @param store 商铺信息
     */
    public void cacheStore(Store store){
        try(Jedis jedis = RedisConnectionManager.getJedis()) {
            String key = STORE_KEY_PREFIX + store.getId();
            jedis.setex(key, EXPIRE_TIME_SECONDS, JsonParser.toJson(store));
        }
    }

    public Store getStoreFromCache(int storeId){
        try(Jedis jedis = RedisConnectionManager.getJedis()) {
            String key = STORE_KEY_PREFIX + storeId;
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            jedis.expire(key, EXPIRE_TIME_SECONDS);
            return JsonParser.parseJson(json, Store.class);
        }
    }

    /**
     * 将分页结果存入缓存
     * 以排序方式、当前页、页大小作为key
     * @param pageResult 分页结果类
     */
    public void cacheStorePage(PageResult<Store> pageResult){
        try(Jedis jedis = RedisConnectionManager.getJedis()) {
            String key = STORE_PAGE_KEY_PREFIX + pageResult.getSortBy() + ":" + pageResult.getCurrentPage() + ":" + pageResult.getSize();
            jedis.setex(key, EXPIRE_TIME_SECONDS, JsonParser.toJson(pageResult));

            if(pageResult.getResults() != null){
                for (Store store : pageResult.getResults()) {
                    cacheStore(store);
                }
            }
        }
    }

    public PageResult<Store> getStorePageFromCache(int page, int size, String sortBy){
        try(Jedis jedis = RedisConnectionManager.getJedis()) {
            String key = STORE_PAGE_KEY_PREFIX + ":" + sortBy + page + ":" + size;
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            jedis.expire(key, EXPIRE_TIME_SECONDS);

            return JsonParser.parseJson(json, PageResult.class);
        }
    }

    /**
     * 删除商铺缓存
     * 删除商铺缓存时，删除商铺信息和分页结果
     * @param storeId 要删除缓存的商铺id
     */
    public void deleteStoreCache(int storeId){
        try(Jedis jedis = RedisConnectionManager.getJedis()) {
            String storeKey = STORE_KEY_PREFIX + storeId;
            jedis.del(storeKey);

            String pattern = STORE_PAGE_KEY_PREFIX + "*";
            for(String key : jedis.keys(pattern)) {
                jedis.del(key);
            }
        }
    }

}
