package com.example.wenDaService.controller;

import com.example.wenDaService.model.EntityType;
import com.example.wenDaService.model.HostHolder;
import com.example.wenDaService.service.LikeService;
import com.example.wenDaService.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/like"},method = RequestMethod.POST)
    @ResponseBody
    public String likeComment(@RequestParam("commentId")int commentId){
        if (hostHolder.getUser() ==null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMNET,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = RequestMethod.POST)
    @ResponseBody
    public String dislikeComment(@RequestParam("commentId")int commentId){
        if (hostHolder.getUser() ==null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMNET,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
