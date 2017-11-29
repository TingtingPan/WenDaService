package com.example.wenDaService.async;

import com.alibaba.fastjson.JSON;
import com.example.wenDaService.util.JedisAdapter;
import com.example.wenDaService.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * event和handler之间的关系是通过consumer来维护的。
 * consumer将event分发给对应的handler
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType,List<EventHandler>>config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        //找到所有eventHandler的实现类
        //自动化注册、方便、可扩展
        Map<String,EventHandler>beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans!=null){
            //遍历所有声明的handlerBean。
            for (Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType>eventTypes= entry.getValue().getSupportEventType();//为啥不直接往map里面放呢？因为一个handler会支持多个event所以要全部取出来再遍历
                for (EventType type:eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0,key);
                    //取到event之后、分发给对应的handler
                    for (String message :events){
                        if (message.equals(key)){//???为啥会pop出来key
                            continue;//???
                        }
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别");
                            continue;
                        }
                        //用对应的eventHandler把event给处理掉。
                        for (EventHandler eventHandler:config.get(eventModel.getEventType())){
                            System.out.println("找到对应的handler，分发给handler处理");
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
