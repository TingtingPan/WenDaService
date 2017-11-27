package com.example.wenDaService.controller;

import com.example.wenDaService.model.*;
import com.example.wenDaService.service.CommentService;
import com.example.wenDaService.service.QuestionService;
import com.example.wenDaService.service.UserService;
import com.example.wenDaService.util.WendaUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;


    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreateDate(new Date());
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQustion(question) > 0) {
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
//    @ResponseBody
    public String getQuestion(@PathVariable("qid") int qid, Model model) {
        Question question = questionService.selectById(qid);
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));
        //因为页面上展示的东西和后台的数据模型不太一致、页面上的评论区域、包含用户信息以及评论详情、所以我们应该要用vo
        List<ViewObject> comments = new ArrayList<ViewObject>();
        for (Comment comment :
                commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }
}
