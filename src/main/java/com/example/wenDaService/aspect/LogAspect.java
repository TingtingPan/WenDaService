package com.example.wenDaService.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import java.util.logging.Logger;

/**
 * Created by pantingting on 2017/8/12.
 */

//@Aspect
//@Component
//public class LogAspect {
//    //用处：1、关注系统性能，记录一个方法运行的时间，每个方法被调用了多少次，每个方法调用的时间
//    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
//
//    @Before("execution(* com.example.wenDaService.controller.*.*(..))")
//    private void beforeMethod(JoinPoint joinPoint) {
//        StringBuilder sb = new StringBuilder();
//        for (Object arg:joinPoint.getArgs()){
//            sb.append("aeg:"+arg.toString()+"|");
//        }
//        logger.info("before method"+sb.toString());
//
//    }
//    @After("execution(* com.example.wenDaService.controller.*.*(..))")
//    public void afterMethod(){
//        logger.info("after method");
//    }
//
//}
