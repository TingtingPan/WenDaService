package com.example.wenDaService.controller;

import com.example.wenDaService.model.HostHolder;
import com.example.wenDaService.model.Question;
import com.example.wenDaService.model.ViewObject;
import com.example.wenDaService.service.QuestionService;
import com.example.wenDaService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pantingting on 2017/8/13.
 */

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder    hostHolder;


    @RequestMapping(path = {"/hello"}, method = RequestMethod.GET)
    @ResponseBody
    public String sayHello() {
        return "hello";
    }


    @RequestMapping(path = {"/", "/index"})
    public String index(Model model,
                        HttpServletResponse response) {
        model.addAttribute("vos", getQuestions(0, 0, 10));

        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
