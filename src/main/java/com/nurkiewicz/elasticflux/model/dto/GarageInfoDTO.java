package com.nurkiewicz.elasticflux.model.dto;

import com.nurkiewicz.elasticflux.ParamsUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 黄念
 * @Date 2020/12/4
 * @Version1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarageInfoDTO {

    /**
     * 修理厂id
     */
    private  Long garageId;

    /**
     * 修理厂店铺图片
     */
    private  String topImage;

    /**
     * 修理厂名称
     */
    private  String company;

    /**
     * 修理厂帐号
     */
    private  String userName;

    /**
     * 主营
     */
    private  List<String> brand;

    /**
     * 详细地址
     */
    private  String address;

    /**
     * 店铺星级
     */
    private  Double avgLevel;

    /**
     * 总评论数
     */
    private  Integer appraiseNum;

    /**
     * 距离
     */
    private  Double distance;

    /**
     * 已预约数量
     */
    private  Integer reserveNum;

    /**
     * 剩余预约数量
     */
    private  Integer remainReserveNum;

    /**
     * 每小时换胎数量
     */
    private final Integer tireNum = 0;


    /**
     * 经度
     */
    private  Double lng;

    /**
     * 纬度
     */
    private  Double lat;

    /**
     * 营业开始时间
     */
    private  String businessStartTime;

    /**
     * 营业结束时间
     */
    private  String businessEndTime;


    public void setRemainReserveNum(Integer remainReserveNum) {
        this.remainReserveNum = remainReserveNum != null && remainReserveNum > 0 ? remainReserveNum : 0;
    }

    public void setDistance(Double distance) {
        if (distance == null) return;
        this.distance = Double.parseDouble(String.format("%.2f", distance));
    }


}
