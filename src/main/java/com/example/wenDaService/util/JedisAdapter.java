package com.example.wenDaService.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool pool;
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d %s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushDB();

        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newHello");
        print(2, jedis.get("newHello"));
        //验证码是带有过期时间的很好的例子。可以将验证码设置一段超时时间。
        //缓存也是redis很好的应用。将对象序列化-》redis-》读取-》反序列化-》obj
        //数值型：例如浏览数。通常都是放在内存中的、如果去锁数据库记录、性能很差
        jedis.setex("hello2", 15, "world");
        //数值型举例：
        jedis.set("pv", "10");
        jedis.incr("pv");
        print(3, jedis.get("pv"));
        print(4, jedis.keys("*"));
        String listname = "list";
        jedis.del(listname);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listname, "a" + String.valueOf(i));
        }
        print(5, jedis.lrange(listname, 0, 12));
        print(6, jedis.llen(listname));
        print(7, jedis.lpop(listname));
        print(8, jedis.llen(listname));
        print(9, jedis.lrange(listname, 2, 6));
        print(10, jedis.linsert(listname, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(11, jedis.lrange(listname, 0, 12));
        //hash
        String userKey = "userxxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "13082853139");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hkeys(userKey));

        //set
        //使用举例：共同好友、共同关注
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));
        print(23, jedis.sdiff(likeKey1, likeKey2));
        print(24, jedis.sinter(likeKey1, likeKey2));
        print(25, jedis.sismember(likeKey1, "12"));

        //排行榜、优先队列。
        String ranKey = "ranKey";
        jedis.zadd(ranKey, 15, "jim");
        jedis.zadd(ranKey, 70, "kin");
        jedis.zadd(ranKey, 88, "jjj");
        print(30, jedis.zcard(ranKey));
        print(31, jedis.zcount(ranKey, 61, 100));
        print(32, jedis.zscore(ranKey, "kin"));
        jedis.zincrby(ranKey, 2, "kin");
        print(33, jedis.zscore(ranKey, "kin"));
        print(34, jedis.zrange(ranKey, 1, 3));
        for (Tuple tuple :
                jedis.zrangeByScoreWithScores(ranKey, 60, 100)) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(38, jedis.zrank(ranKey, "jim"));

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        print(40, jedis.zlexcount(setKey, "-", "+"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());

        } finally {
            if (jedis != null) {
                jedis.close();
                ;
            }
        }
        return 0;
    }
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());

        } finally {
            if (jedis != null) {
                jedis.close();
                ;
            }
        }
        return 0;
    }
    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());

        } finally {
            if (jedis != null) {
                jedis.close();
                ;
            }
        }
        return 0;
    }
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());

        } finally {
            if (jedis != null) {
                jedis.close();
                ;
            }
        }
        return false;
    }


    //Redis应用
    /***
     * 1、PV
     * 2、关注
     * 3、点赞
     * 4、排行榜
     * 5、验证码
     * 6、缓存
     * 7、异步队列
     * 8、判别队列
     */
}
