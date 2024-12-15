package com.voyagerss.persist.component.constants;



public enum RedisCode {
    REDIS_CODE("REDISCODE"),
    REDIS_MULTI_TEXT("REDISMULTITEXT"),
    REDIS_MENU("REDISMENU"),
    REDIS_SQL("REDISSQL"),
    REDIS_SYSTEM("REDISSYSTEM"),
    REDIS_NATION("REDISNATION"),
    REDIS_GRID_VERSION("REDISGRIDVERSION"),
    REDIS_UUID(":UUID"),
    REDIS_MULTI_LOGIN(":MULTILOGIN");


    private String code;

    private RedisCode(String code){
        this.code = code;
    }

    public static String getCode(RedisCode code){
        return code.code;
    }

    public static boolean isRedisCode(String code){
        for(RedisCode redisCode : RedisCode.values()) {
            if(redisCode.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

}
