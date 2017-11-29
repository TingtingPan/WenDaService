package com.example.wenDaService.async;

import com.alibaba.fastjson.JSONObject;
import com.example.wenDaService.util.JedisAdapter;
import com.example.wenDaService.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * 事件的入口
 * 利用redis的阻塞队列。
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            //为啥这里是lpush？？怎么确定brpop和lpush 操作的是同一个队列、为什么push不需要阻塞、是一个无界队列吗？
            //eventProducer将事件放到事件队列里面。生产者的工作就结束了
            jedisAdapter.lpush(key,json);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    //各种异步框架：例如XXQUEUE就是这种思想、发布/订阅.生产者/消费者
}
