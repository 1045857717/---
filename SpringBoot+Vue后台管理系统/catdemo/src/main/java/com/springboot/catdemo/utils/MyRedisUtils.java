package com.springboot.catdemo.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.entity.Files;
import com.springboot.catdemo.exception.ServiceException;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 * @Author: can
 * @Description:
 * @Date: Create in 22:41 2022/4/12
 */
@Component
public class MyRedisUtils {

    static RedisTemplate<String, Object> redisTemplate;
    private static StringRedisTemplate stringRedisTemplate;

    @Resource(name="myRedisTemplate")
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        MyRedisUtils.redisTemplate = redisTemplate;
    }

    /* -------------------------------------common----------------------------------------*/
    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time  时间（秒）
     * @return
     */
    public static boolean expire(String key, long time){
        try {
            if(time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表 永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true存在 false不存在
     */
    public boolean hashKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用redisTemplate删除单个缓存
     */
    public static boolean deleteKey(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用redisTemplate删除多个缓存
     */
    public static long deleteKeys(String... keys) {
        try {
            return redisTemplate.delete(Arrays.asList(keys));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* -------------------------------------string----------------------------------------*/


    /**
     * 使用StringRedisTemplate设置缓存
     */
    public static void setStringCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 使用StringRedisTemplate设置缓存，并设置时间
     * @param key  键
     * @param value  值
     * @param time  时间（秒）
     * @return
     */
    public static boolean setStringCache(String key, String value, long time) {

        try {
            redisTemplate.opsForValue().set(key, value);
            if (time > 0 ) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 使用StringRedisTemplate获取缓存
     */
    public static Object getStringCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /* -------------------------------------HashMap----------------------------------------*/

    /**
     * 获取单个hashKey单个键的值
     * @param key 键（rawKey） 不能为NULL
     * @param field 项（rawHashKey） 不能为NULL
     * @return 项对应的值(rawHashValue)
     */
    public Object getHashCache(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取单个或多个hashKey单个或多个键的值
     * @param key 键（rawKey） 不能为NULL
     * @param field 项（rawHashKey） 不能为NULL
     * @return 项对应的多个或单个 值(rawHashValue)
     */
    public static Object getHashKeyAllValueCache(String key, String... field) {
        return redisTemplate.opsForHash().multiGet(key, Arrays.stream(field).collect(Collectors.toList()));
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键（rawKey）
     * @return 对应的多个键值 （rawHashKey,rawHashValue）
     */
    public static Map<Object,Object> getHashAllKeyValueCache(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hashKey对应的field有多少个
     * @param key 键名
     * @return field个数
     */
    public static long getHashKeySizeFieldCache(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取hashKey对应的field的值的长度
     * @param key 键名
     * @return 值的长度
     */
    public static Long getHashKeyLengthOfFieldCache(String key, String field) {
        return redisTemplate.opsForHash().lengthOfValue(key, field);
    }

    /**
     * Hash 删除field
     * @param key 键名
     * @param key 要删除的字段名
     * @return 成功删除的field个数
     */
    public static Long delHashKeyField(String key, Object field) {
        return redisTemplate.opsForHash().delete(key, field);
    }


    /**
     * 单个的方式写入HashMap
     * @param key 键
     * @param field 项
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean setHashCache(String key, String field, Object value) {
        try {
            MyRedisUtils.redisTemplate.opsForHash().put(key, field, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 单个的方式写入HashMap 并设置时间
     * @param key 键
     * @param field 项
     * @param value 值
     * @param time 指定缓存失效时间(秒)
     * @return true成功 false失败
     */
    public static boolean setHashCache(String key, String field, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            if(time > 0) {
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashPutAll
     * @param key 键
     * @param map 对应多个键值
     * @return true成功 false失败
     */
    public static boolean setHashAllCache(String key, Map<String,Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashPutAll 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 指定缓存失效时间(秒)
     * @return true成功 false失败
     */
    public static boolean setHashAllCache(String key, Map<String,Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if(time > 0) {
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     *  这个方法会判断key是否有值，如果没有就直接插入，返回true；如果有值的话就不会插入，返回false。
     * @param key 键
     * @param field 项
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean setHashIfAbsentCache(String key, String field, String value) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, field, value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key   键 不能为NULL
     * @param field  项 可以是多个 不能NULL
     * @return 删除的条数
     */
    public static long delHashCache (String key, Object... field) {
        return redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key   键 不能为NULL
     * @param field  项 不能为NULL
     * @return true存在 false不存在
     */
    public static boolean hasHashKeyCache (String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }



    /* -------------------------------------Zset----------------------------------------*/


    /**
     * 将数据存入zset缓存
     * 向指定key中添加元素，按照score值由小到大进行排列
     * @param key 键
     * @param values 值
     * @param score 排序值
     * @param time 过期时间，可以为null
     * @return true成功 false失败
     */
    public static Boolean zsAdd(String key, Object values, double score, Long time) {
        try {
            boolean result = redisTemplate.opsForZSet().add(key, values,score);
            if(time!= null && time > 0) {
                expire(key,time);
            }
            return result;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据存入zset缓存
     * 通过TypedTuple方式新增数据
     * @param key 键
     * @param tuples set数组
     * {@link org.springframework.data.redis.core `ZSetOperations.TypedTuple`}
     * 例如：
    Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<String>>();
    ZSetOperations.TypedTuple<String> typedTuple1 = new DefaultTypedTuple<String>("E",6.0);
    typedTupleSet.add(typedTuple1);
     * @param time 过期时间，可以为null
     * @return 成功条数
     */
    public static long zsAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples, Long time) {
        try {
            long result = redisTemplate.opsForZSet().add(key, tuples);
            if(time!= null && time > 0) {
                expire(key,time);
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取集合的大小，地层调用的还是 zCard(K key)
     * @param key 键
     * @return 集合的大小
     */
    public static long zsSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Zset获取指定score区间里的元素个数
     * @param key 键
     * @param min 键
     * @param max 键
     * @return 指定score区间里的元素个数
     */
    public static long zsCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * Zset获取指定下标之间的值。
     * (0,-1)就是获取全部
     * @param key 键
     * @param start 开始下标
     * @param end 结束下标
     * @return 返回指定范围的值
     */
    public static Set zsRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zset获取指定score区间的值
     * @param key 键
     * @param min score开始下标
     * @param max score结束下标
     * @return 返回指定范围的值
     */
    public static Set zsRangeByScore(String key,double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zset获取指定score区间的值，然后从给定下标和给定长度获取最终值
     * @param key 键
     * @param min score开始下标
     * @param max score结束下标
     * @param offset 偏移量即从哪条数据开始排序
     * @param count 取count条满足条件的数据
     * @return 返回指定范围的值
     */
    public static Set zsRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zset逆序获取对应下标的元素
     * @param key 键
     * @param start 开始位置（元素最后的位置开始）
     * @param end 结束位置
     * @return 返回指定范围的值
     */
    public static Set zsReverseRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, start, end);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Zset获取指定元素在集合中的索引，索引从0开始
     * @param key 键
     * @param obj 指定元素
     * @return 返回指定元素的索引
     */
    public static Long zsRank(String key, Object obj) {
        try {
            return redisTemplate.opsForZSet().rank(key, obj);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zset获取倒序排列的索引值，索引从0开始
     * @param key 键
     * @param obj 指定元素
     * @return 返回指定元素的索引
     */
    public static Long zsReverseRank(String key, Object obj) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, obj);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zset 移除集合中指定的值
     * @param key 键
     * @param values 开始位置（元素最后的位置开始）
     * @return 返回删除数量
     */
    public static long zsRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Zset 移除指定下标的值
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return 返回删除数量
     */
    public static long zsRemoveRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().removeRange(key, start, end);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Zset 移除指定score区间内的值
     * @param key 键
     * @param min score最小值
     * @param max score最大值
     * @return 返回删除数量
     */
    public static long zsRemoveRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
