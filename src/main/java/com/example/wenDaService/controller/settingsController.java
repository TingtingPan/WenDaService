package com.example.wenDaService.controller;

import com.example.wenDaService.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by pantingting on 2017/8/12.
 */

@Controller
public class settingsController {
    //享元模式，一个创建好的实例可以给各个地方用。
    @Autowired
    WendaService wendaService;

    @RequestMapping(path = {"/setting"},method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession){
        return "Setting OK"+wendaService.getMessage(1);
    }

}
