package com.nurkiewicz.elasticflux.enums;

/**
 * @author 张奇
 * @desc
 * @time 2020/12/12 下午2:42
 */
public enum OrderStatusEnum {

    WAIT_SURE(1,"待接单"),
    ALREADY_APPOINT(2,"已预约"),
    WAIT_RECEIVE(5,"待揽件"),
    WAIT_PAY(10,"待支付"),
    WAIT_SETTLE(15,"修理厂待揽货结算"),
    COMPLETE(100,"已完成"),
    CANCEL(200, "已取消");

    private Integer status;

    private String value;

    OrderStatusEnum(Integer status, String value) {
        this.status = status;
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
