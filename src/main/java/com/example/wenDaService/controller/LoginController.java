package com.example.wenDaService.controller;

import com.example.wenDaService.dao.LoginTicketDAO;
import com.example.wenDaService.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"},method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        try {
            Map<String, String> map = userService.register(username, password);
            //下面是注册成功直接变成登陆状态进入主页
            if (map.containsKey("ticket")) {
                //说明登录成功
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setMaxAge(3600);
                //把cookie放到response中
                response.addCookie(cookie);
                //登录成功后跳转到主页
                return "redirect:/";
            } else {
                model.addAttribute("msg",map.get("msg"));
                return "login";//说明登录失败
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    //参数中的response是为了下发cookie给浏览器。
    @RequestMapping(path = {"/login/"},method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                //说明登录成功
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setMaxAge(3600);
                //把cookie放到response中
                response.addCookie(cookie);
                //登录成功后跳转到主页
                return "redirect:/";
            } else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"},method = RequestMethod.GET)
    public String reg(Model model) {
        return "login";
    }

    /***
     * 页面访问：
     * 客户端：带token的HTTP请求
     * 服务端：
     * 1、根据token获取用户id
     * 2、根据用户id去获取用户相关信息
     * 3、用户和页面的权限处理
     * 4、渲染页面或者跳转页面。
     */
}
