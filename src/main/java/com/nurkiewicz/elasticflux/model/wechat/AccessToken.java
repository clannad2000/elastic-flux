/**
 *
 */
package com.nurkiewicz.elasticflux.model.wechat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 刘曙尘
 * @Description
 * @Company 北京三真优购科技有限公司
 * @date 2017年8月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessToken {
	private String access_token;	//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同

	private String expires_in;	//access_token接口调用凭证超时时间，单位（秒）

	private String refresh_token;	//用户刷新access_token

	private String openid;	//用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID

	private String scope;	//用户授权的作用域，使用逗号（,）分隔

	private String errcode;  //返回异常参数

	private String errmsg; //异常说明



	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", expires_in=" + expires_in + ", refresh_token="
				+ refresh_token + ", openid=" + openid + ", scope=" + scope + ", errcode=" + errcode + ", errmsg="
				+ errmsg + "]";
	}

}
