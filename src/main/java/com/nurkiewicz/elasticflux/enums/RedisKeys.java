package com.nurkiewicz.elasticflux.enums;

/**
 * @author 张奇
 * @desc
 * @time 2020/12/9 下午5:38
 */
public enum RedisKeys {
    CACHE_TYRE("szcl::tyre::order::info::"),
    APPRAISE_COUNT("szcl::tyre::garage::appraise::count::")
    ;


    RedisKeys(String value){
        this.value = value;
    }

    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
