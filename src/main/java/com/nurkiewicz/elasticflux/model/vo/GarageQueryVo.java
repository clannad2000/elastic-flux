package com.nurkiewicz.elasticflux.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.function.Supplier;

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
public class GarageQueryVo {

    /**
     *
     */
    private Long garageNo;

    /**
     * 服务电话
     */
    private String serviceTel;

    /**
     * 城市编号
     */
    private String cityNumber;

    /**
     * 区域编号
     */
    private String areaNumber;

    /**
     * 汽车品牌名称
     */
    private String brand;

    /**
     * 服务类型 1.存胎 2.换胎 3洗车 4其他
     */
    private String serviceNo;
    /**
     * 预约开始时间
     */
    private Long reserveStartTime;
    /**
     * 预约结束时间
     */
    private Long reserveEndTime;
    /**
     * 经度
     */
    private Float lng;
    /**
     * 纬度
     */
    private Float lat;

    /**
     * 查询关键字:公司名
     */
    private String company;


    private Supplier<QueryBuilder> filterBuildersSupplier;

}
