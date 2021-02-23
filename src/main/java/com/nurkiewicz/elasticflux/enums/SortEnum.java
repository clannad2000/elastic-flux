package com.nurkiewicz.elasticflux.enums;

/**
 * @Description
 * @Author 黄念
 * @Date 2021/2/1
 * @Version1.0
 */
public enum SortEnum {
    DISTANCE(1), AVGLEVEL(2);

    public int value;

    SortEnum(int value) {
        this.value = value;
    }
}
