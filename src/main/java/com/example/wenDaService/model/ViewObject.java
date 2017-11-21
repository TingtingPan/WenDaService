package com.example.wenDaService.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pantingting on 2017/8/13.
 */
public class ViewObject {
    private Map<String,Object> objs = new HashMap<String,Object>();

    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }

    @Override
    public String toString() {
        return "ViewObject{" +
                "objs=" + objs +
                '}';
    }
}
