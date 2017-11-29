package com.example.wenDaService.async.handler;

import com.example.wenDaService.async.EventHandler;
import com.example.wenDaService.async.EventModel;
import com.example.wenDaService.async.EventType;
import com.example.wenDaService.model.Message;
import com.example.wenDaService.model.User;
import com.example.wenDaService.service.MessageService;
import com.example.wenDaService.service.UserService;
import com.example.wenDaService.util.WendaUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class likeHandler implements EventHandler,InitializingBean {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        System.out.println("进入了handler的处理程序了");
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户："+user.getName()+"赞了你的评论:http://127.0.0.1:8080/question/"+eventModel.getExt("questionId"));
        messageService.addMessage(message);
        //接下来要做的就是在事件的入口把事件发出去
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("LikeHandler初始化");
    }
}
