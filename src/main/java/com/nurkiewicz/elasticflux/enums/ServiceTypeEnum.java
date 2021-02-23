package com.nurkiewicz.elasticflux.enums;

/**
 * @Description
 * @Author 黄念
 * @Date 2021/1/28
 * @Version1.0
 */
public enum ServiceTypeEnum {
    ONSITE(1),
    SHOP(2),
    STORE(3);
    public Integer value;

    ServiceTypeEnum(int value) {
        this.value = value;
    }
}
