package com.example.wenDaService.service;

import com.example.wenDaService.dao.CommentDAO;
import com.example.wenDaService.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment)>0?comment.getId():0;
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }
    //service作为中间层、对上是面向业务的、对下面向数据库
    public boolean deleteComment(int commentId){
        try {
            commentDAO.updateStatus(commentId, 1);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
