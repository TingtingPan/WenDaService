package com.example.wenDaService.async;

import java.util.HashMap;
import java.util.Map;

/***
 * 表示事件发生现场
 */
public class EventModel {
    //为了快速读取、所以把这些参数独立出来。
    private EventType eventType;//点赞
    private int actorId;//谁点赞
    private int entityType;
    private int entityId;
    private int entityOwnerId;//人与人交互

    //就像viewObject、什么都可以放进去。
    private Map<String,String> exts = new HashMap<>();

    //需要在EventModel中增加构造函数、否则的话，JSON工具在反序列化的时候会报错。
    public EventModel(){

    }

    public  EventModel (EventType eventType){
        this.eventType = eventType;
    }

    //返回this、链式调用、
    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
