package com.example.wenDaService.service;

import com.example.wenDaService.dao.UserDAO;
import com.example.wenDaService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

