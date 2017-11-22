package com.example.wenDaService.service;

import com.example.wenDaService.dao.UserDAO;
import com.example.wenDaService.model.User;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pantingting on 2017/8/13.
 */
@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectByid(id);
    }

    public Map<String,String> register(String userName,String password){
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isEmpty(userName)){
            map.put("msg","用户名不能为空");
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
                    
        }
        /// TODO: 2017/11/22 0022  register and login 
        return map;
    }
}

