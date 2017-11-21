package com.example.wenDaService.service;

import org.springframework.stereotype.Service;

/**
 * Created by pantingting on 2017/8/12.
 */

@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message"+String.valueOf(userId);
    }
}
