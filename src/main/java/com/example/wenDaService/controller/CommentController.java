package com.example.wenDaService.controller;

import com.example.wenDaService.model.Comment;
import com.example.wenDaService.model.EntityType;
import com.example.wenDaService.model.HostHolder;
import com.example.wenDaService.service.CommentService;
import com.example.wenDaService.service.QuestionService;
import com.example.wenDaService.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId")int questionId,
                             @RequestParam("content")String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);

            if (hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(questionId,EntityType.ENTITY_QUESTION);
            questionService.updateCommentCount(questionId,count);
        } catch (Exception e) {
            logger.error("增加评论失败"+e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/question/"+questionId;
    }
}
