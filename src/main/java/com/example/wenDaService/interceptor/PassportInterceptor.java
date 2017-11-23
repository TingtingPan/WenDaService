package com.example.wenDaService.interceptor;

import com.example.wenDaService.dao.LoginTicketDAO;
import com.example.wenDaService.dao.UserDAO;
import com.example.wenDaService.model.HostHolder;
import com.example.wenDaService.model.LoginTicket;
import com.example.wenDaService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //请求处理开始之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies()!=null){
            for (Cookie cookie: httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
            if (ticket != null) {
                //验证ticket是否是有效的
                LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
                if (loginTicket == null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0) {
                    //ticket是无效的,返回true、那么这个请求就不会继续执行下去了。
                    //todo::
                    return true;
                } else {
                    //将 用户信息取出来并通过合适的方式保存、使得controller中的其他链路都可以访问到这个user。
                    //拦截器的这个工作主要是为了构建一个随处可用的hostHolder、
                    User user = userDAO.selectById(loginTicket.getUserId());
                    hostHolder.setUser(user);
                }
            }
        }
        //未登录状态
        return true;
    }

    //controller处理完之后,渲染之前。
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView!=null){
            //那么在模版中就可以直接访问user变量了
            if (hostHolder.getUser()!=null){
                System.out.println(hostHolder.getUser().toString());
                modelAndView.addObject("user",hostHolder.getUser());
            }
        }
    }
    //渲染完成之后
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
