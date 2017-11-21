package com.example.wenDaService.controller;

import com.example.wenDaService.model.Question;
import com.example.wenDaService.model.ViewObject;
import com.example.wenDaService.service.QuestionService;
import com.example.wenDaService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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

    @RequestMapping(path = {"/hello"},method = RequestMethod.GET)
    @ResponseBody
    public String home(){
        return "hello";
    }


    @RequestMapping("/")
    public String index(Model model){
       List<Question> questions= questionService.getLatestQuestions(0,0,10);
       List<ViewObject> vos = new ArrayList<ViewObject>();
       model.addAttribute("vos",vos);
       for (Question question:questions){
           ViewObject vo = new ViewObject();
           vo.set("question",question);
           vo.set("user",userService.getUser(question.getUserId()));
           vos.add(vo);
       }
       model.addAttribute(vos);
        return "index";
    }
}
