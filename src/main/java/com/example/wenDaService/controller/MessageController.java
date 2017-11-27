package com.example.wenDaService.controller;

import com.example.wenDaService.model.HostHolder;
import com.example.wenDaService.model.Message;
import com.example.wenDaService.model.User;
import com.example.wenDaService.model.ViewObject;
import com.example.wenDaService.service.MessageService;
import com.example.wenDaService.service.UserService;
import com.example.wenDaService.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String getConversationList(Model model){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message>conversationList = messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message :
                conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("message",message);
            int targetId = message.getFromId() == localUserId?message.getToId():message.getFromId();
            vo.set("user",userService.getUser(targetId));
            vo.set("unread",messageService.getConversationUnreadCount(message.getConversationId(),localUserId));
            vo.set("total",messageService.getConversationTotalCount(message.getConversationId(),localUserId));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }
    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                      @RequestParam("conversationId")String converstionId){
        try{
            List<Message> messageList = messageService.getConversationDetail(converstionId,0,10);
            List<ViewObject>messages = new ArrayList<ViewObject>();
            for (Message message :
                    messageList) {
                //改变消息状态
                messageService.readMessage(message.getId(),1);
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                //取出消息关联的用户、谁发的
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情失败:"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){
        try {
            if (hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if (user==null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setToId(user.getId());
            message.setFromId(hostHolder.getUser().getId());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {

            logger.error("发送消息失败"+e.getMessage());
            return WendaUtil.getJSONString(1,"发信失败");
        }
    }
}
