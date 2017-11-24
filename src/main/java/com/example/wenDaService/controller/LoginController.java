package com.example.wenDaService.controller;

import com.example.wenDaService.dao.LoginTicketDAO;
import com.example.wenDaService.service.UserService;
import com.sun.deploy.net.HttpResponse;
import freemarker.template.utility.StringUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
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

    @RequestMapping(path = {"/reg/"}, method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next", required = false) String next,
                      HttpServletResponse response) {
        try {
            Map<String, String> map = userService.register(username, password);
            //下面是注册成功直接变成登陆状态进入主页
            if (map.containsKey("ticket")) {
                //登录成功、下发token。
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                //把cookie放到response中
                response.addCookie(cookie);
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                //登录成功后跳转到主页
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";//说明登录失败
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    //参数中的response是为了下发cookie给浏览器。
    @RequestMapping(path = {"/login/"}, method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                //说明登录成功
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                //把cookie放到response中
                response.addCookie(cookie);
                //判断一下是否带有next的值
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                //登录成功后跳转到主页
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return "login";
        }
    }

    //登录注册页
    @RequestMapping(path = {"/reglogin"}, method = RequestMethod.GET)
    public String reg(Model model,
                      @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/logout"})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        //退出后返回首页
        return "redirect:/";
    }
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

/***用户数据安全性
 * 1、HTTPS注册页
 * 2、公钥加密、私钥解密、e.g.支付宝h5页面支付密码加密
 * 3、用户密码salt加密防破解
 * 4、token有效期，防止token泄露就永久泄露了。
 * 5、单一平台的单点登录
 * 6、用户状态的权限判断
 * 7、添加验证码机制、防止爆破和批量注册。
 */

/***登录注册总结：
 * 1、密码加salt 后 md5加密
 * 2、拦截器处理用户cookie，token、判断用户是否登录、
 * 3、hostHolder保存此次页面访问的用户信息、用ThreadLocal
 * 4、redirect和forward之间的区别、forward不会在浏览器产生新的request、forward之后会共享前面的request。redirect是浏览器重新发了个请求
 * 5、只要自己网页上的脚本不会触发对外的请求，就不会造成cookie泄露
 * 6、未登录跳转。用户在没有登录的状态下，访问某些页面，会跳转到登陆页，登录完成后自动跳回刚才访问的页面
 */

/**
 * 注意，next的值需要进一步判断、例如假如next为www.baidu.com就会跳转到别人的网站上面去了。
 */
