package com.nurkiewicz.elasticflux.enums;

/**
 * @author 李海
 * @Description 发号枚举
 * @Company 北京三真优购科技有限公司
 * @date 2017年7月31日
 */
public enum SendNoTypeEnum {
	GUEST("guest", "CKH"), // 客户档案
	ORDER("order", "TC"), // 订单
	SERVICE("serviceOrder", "XF"),
	BACK_ORDER("backOrder", "TK")
	;



	public String value;
	public String name;

	SendNoTypeEnum(String name, String value) {
		this.value = value;
		this.name = name;
	}
}
