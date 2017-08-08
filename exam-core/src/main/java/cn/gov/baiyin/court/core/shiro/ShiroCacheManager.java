//package cn.gov.baiyin.court.core.shiro;
//
//import com.google.common.cache.CacheBuilder;
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.apache.shiro.cache.CacheManager;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by WK on 2017/3/25.
// */
//public class ShiroCacheManager implements CacheManager {
//
//    private static final Map<String, ShiroCache> cacheMap = new ConcurrentHashMap<>();
//
//    @Override
//    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
//
//        return cacheMap.computeIfAbsent(s, k -> new ShiroCache(k));
//    }
//
//    static class ShiroCache implements Cache {
//        String key;
//        com.google.common.cache.Cache cache;
//
//        ShiroCache(String k) {
//            key = k;
//            cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
//        }
//
//        @Override
//        public Object get(Object o) throws CacheException {
//            return cache.getIfPresent(o);
//        }
//
//        @Override
//        public Object put(Object o, Object o2) throws CacheException {
//            cache.put(o, o2);
//            return o2;
//        }
//
//        @Override
//        public Object remove(Object o) throws CacheException {
//            Object v = cache.getIfPresent(o);
//            if (v != null) {
//                cache.invalidate(o);
//            }
//            return v;
//        }
//
//        @Override
//        public void clear() throws CacheException {
//
//            cache.invalidateAll();
//        }
//
//        @Override
//        public int size() {
//            return (int) cache.size();
//        }
//
//        @Override
//        public Set keys() {
//            return cache.asMap().keySet();
//        }
//
//        @Override
//        public Collection values() {
//            return cache.asMap().values();
//        }
//    }
//}
