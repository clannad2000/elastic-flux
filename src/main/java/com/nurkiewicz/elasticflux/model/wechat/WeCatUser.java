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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeCatUser {
	private String openid;//	用户的唯一标识

	private String nickname;	//用户昵称

	private String sex;	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

	private String language;	//语言

	private String province;	//用户个人资料填写的省份

	private String city;	//普通用户个人资料填写的城市

	private String country;	//国家，如中国为CN

	private String headimgurl;	//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。

	private String[] privilege;	//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）

	private String unionid;	//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。

	@Override
	public String toString() {
		return "WeCatUser [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", province=" + province
				+ ", city=" + city + ", country=" + country + ", headimgurl=" + headimgurl + ", privilege=" + privilege
				+ ", unionid=" + unionid + "]";
	}

}
