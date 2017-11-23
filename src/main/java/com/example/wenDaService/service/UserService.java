package com.example.wenDaService.service;

import com.example.wenDaService.dao.LoginTicketDAO;
import com.example.wenDaService.dao.UserDAO;
import com.example.wenDaService.model.LoginTicket;
import com.example.wenDaService.model.User;
import com.example.wenDaService.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by pantingting on 2017/8/13.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private  LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /**
     * 注册要注意的点：
     * 1、长度、敏感词、重复、特殊字符
     * 2、密码长度要求
     * 3、密码salt加密要求
     * 4、密码强度检测（md5库）
     * 5、用户邮件、短信激活
     *
     * @param userName
     * @param password
     * @return
     */
    public Map<String, String> register(String userName, String password) {
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(userName);
        if (user != null) {
            map.put("msg", "用户已存在");
            return map;
        }
        //添加用户
        user = new User();
        user.setName(userName);
        Random random = new Random();
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        //使用随机的UUID生成随机的salt。
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        //使用密码+salt并取md5加密得到密文.
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        //注册成功、也是登录状态 ,添加ticket
        String ticket= addLoginTicket(user.getId());
        //最终是要下发给浏览器。
        map.put("ticket",ticket);
        return map;
    }

    /***
     * 登录注意点：
     * 1、服务器密码校验、三方校验回调，token登记，服务器端token关联userId。客户端存储token（app本地存储、浏览器存储cookie）
     * 2、设置token有效期。
     * tips:token可以是sessionId、或者是cookie中的一个key。
     * 登出注意点：
     * 1、服务器、客户端删除token
     * 2、session清理？
     */
    public Map<String, String> login(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(userName);
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg", "密码不正确");
            return map;
        }

        //登录成功 、下发ticket
        String ticket= addLoginTicket(user.getId());
        //最终是要下发给浏览器。
        map.put("ticket",ticket);
        return map;
    }

    //在数据库中增加一个ticket。
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime()+3600*24*100);
        loginTicket.setExpired(now);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        //设置为有效的
        loginTicket.setStatus(0);
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
}

