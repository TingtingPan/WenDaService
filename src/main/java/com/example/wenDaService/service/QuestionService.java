package com.example.wenDaService.service;

import com.example.wenDaService.dao.QuestionDAO;
import com.example.wenDaService.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pantingting on 2017/8/13.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
