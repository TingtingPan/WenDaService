package com.example.wenDaService.controller;

import com.example.wenDaService.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by pantingting on 2017/8/12.
 * 这个controlelr主要是在学习SpringMVC
 */
//@Controller
public class indexController {

    @Autowired
    public WendaService wendaService;

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession httpSession){
        return "Hello Session"+httpSession.getAttribute("msg");
    }

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","tingting");
        List<String> colors;
        colors = Arrays.asList(new String[]{"sd","yellow","sdjs"});
//        model.addAttribute("colors", colors);
        return "hello";
    }

    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("CookieValue:"+sessionId+"<br>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+ request.getHeader(name)+"<br>");
        }
        //cookie
        if (request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()){
                sb.append("Cookie:"+cookie.getName()+"<br>"+"value:"+cookie.getValue()+"<br>");
            }
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURL()+"<br>");
        response.addHeader("nowCoderID","sdfkjd");
        response.addHeader("responseId","tingting");
        response.addCookie(new Cookie("userName","tingting"));
        return sb.toString();
    }

    //笔记：code为路径变量。e.g.127.0.0.1:8080/profile/1   那么1就是code
    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.POST,RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession)  {

        RedirectView rv = new RedirectView("/",true);
        if (code == 301){
            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//301跳转
            httpSession.setAttribute("msg:","301 jumping");
        }
        return rv;//302跳转
    }

    //笔记：requestParam是请求参数。e.g. 127.0.0.1:8080/profile/user/123?type=2&key=z;那么这里面的type=2就是请求参数。
    @RequestMapping(path={"/admin"})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if ("admin".equals(key)){
            return "hello admin";
        } else {
            throw  new IllegalArgumentException("参数不对");
        }
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error"+e.getMessage();
    }

}
