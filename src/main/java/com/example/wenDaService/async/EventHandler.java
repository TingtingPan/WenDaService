package com.example.wenDaService.async;

import java.util.List;

public interface EventHandler {

    //发生event的时候怎么处理。
    void doHandle(EventModel eventModel);

    //注册自己，让别人知道自己关注哪些event
    List<EventType> getSupportEventType();
}
