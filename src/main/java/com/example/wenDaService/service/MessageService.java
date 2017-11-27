package com.example.wenDaService.service;

import com.example.wenDaService.dao.MessageDAO;
import com.example.wenDaService.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message)>0?message.getId():0;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getConversationUnreadCount( String conversationId, int userId){
        return messageDAO.getConversationUnreadCount(conversationId,userId);
    }
    public int getConversationTotalCount( String conversationId, int userId){
        return messageDAO.getConversationTotalCount(conversationId,userId);
    }
    public void readMessage(int id,int hasRead){
        messageDAO.updateStatus(id,1);
    }
}
