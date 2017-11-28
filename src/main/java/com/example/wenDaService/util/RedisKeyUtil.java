package com.example.wenDaService.util;

public class RedisKeyUtil {
    //前缀都是业务。后面用entityId+entityType组成
    private static  String SPLIT = ":";
    private static  String BIZ_LIKE = "LIKE";
    private static  String BIZ_DISLIKE = "DISLIKE";

    public static String getLikeKey(int entityType,int entityID){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+String.valueOf(entityID);
    }
    public static String getDisLikeKey(int entityType,int entityID){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+String.valueOf(entityID);
    }


}
