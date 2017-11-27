package com.example.wenDaService.service;

import com.example.wenDaService.dao.QuestionDAO;
import com.example.wenDaService.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by pantingting on 2017/8/13.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public Question selectById(int id){
        return questionDAO.selectById(id);
    }

    public int addQustion(Question question){
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDAO.addQuestion(question);
    }


    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
