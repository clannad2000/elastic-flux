package com.nurkiewicz.elasticflux;


import com.nurkiewicz.elasticflux.model.dto.GarageInfoDTO;
import com.nurkiewicz.elasticflux.model.vo.GarageQueryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @Author 黄念
 * @Date 2020/12/7
 * @Version1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GarageESMapper {

    private final static String garageIndex = "tyredb-s_garage";

    private final RestHighLevelClient restHighLevelClient;

    /**
     * 查询修理厂相关信息
     *
     * @param garageQueryVo
     * @return
     * @throws IOException
     */

//    public PageInfo<GarageInfoDTO> searchGarage(GarageQueryVo garageQueryVo) throws IOException {
//        SearchRequest searchRequest = getSearchRequest(garageQueryVo);
//
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//
//        List<GarageInfoDTO> garageInfoDTOList = getGarageInfoDTOS(response, garageQueryVo.getLat() != null && garageQueryVo.getLng() != null ? SortEnum.DISTANCE : SortEnum.AVGLEVEL);
//
//        Page<GarageInfoDTO> page = new Page<>(garageQueryVo.getPageNum(), garageQueryVo.getPageSize());
//        page.setTotal(response.getHits().getTotalHits().value);
//        PageInfo<GarageInfoDTO> pageInfo = page.toPageInfo();
//        pageInfo.setList(garageInfoDTOList);
//        return pageInfo;
//    }

    public Mono<List<GarageInfoDTO>> test(GarageQueryVo garageQueryVo) {
        return Mono
                .<SearchResponse>create(sink -> restHighLevelClient.searchAsync(getSearchRequest(garageQueryVo), RequestOptions.DEFAULT, ElasticAdapter.listenerToSink(sink)))
                .map(this::getGarageInfoDTOS);
    }


    private SearchRequest getSearchRequest(GarageQueryVo garageQueryVo) {
        SearchRequest searchRequest = new SearchRequest(garageIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        boolQueryBuilder.filter(QueryBuilders.termQuery("garageStatus", 1));//0下架 1上架
//        boolQueryBuilder.filter(QueryBuilders.existsQuery("serviceTel"));
//        boolQueryBuilder.filter(QueryBuilders.existsQuery("businessStartTime"));

        if (garageQueryVo.getGarageNo() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("garageId", garageQueryVo.getGarageNo()));
        }

        if (StringUtils.isNotBlank(garageQueryVo.getServiceTel())) {
            String tel = disposeTel(garageQueryVo.getServiceTel());
            if (tel.length() == 4) {
                boolQueryBuilder.should(QueryBuilders.termQuery("serviceTel.edge", tel));
            } else if (tel.length() >= 6) {
                boolQueryBuilder.should(QueryBuilders.prefixQuery("serviceTel.prefix", tel));
            }
        }

        if (StringUtils.isNotBlank(garageQueryVo.getCityNumber())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("cityNumber", garageQueryVo.getCityNumber()));
        }

        if (StringUtils.isNotBlank(garageQueryVo.getAreaNumber())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("areaNumber", garageQueryVo.getAreaNumber()));
        }

        if (StringUtils.isNotBlank(garageQueryVo.getBrand())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", garageQueryVo.getBrand()));
        }

        if (StringUtils.isNotBlank(garageQueryVo.getServiceNo())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("supportService", garageQueryVo.getServiceNo()));
        }

        if (garageQueryVo.getReserveStartTime() != null && garageQueryVo.getReserveEndTime() != null) {
            long zeroTime = TimeUtils.getZeroTime(garageQueryVo.getReserveStartTime()).toInstant().toEpochMilli();

            BoolQueryBuilder businessTimeBoolQueryBuilder1 = QueryBuilders.boolQuery();
            businessTimeBoolQueryBuilder1.must(QueryBuilders.rangeQuery("businessStartTimeQ1").lte(garageQueryVo.getReserveStartTime() - zeroTime));
            businessTimeBoolQueryBuilder1.must(QueryBuilders.rangeQuery("businessEndTimeQ1").gte(garageQueryVo.getReserveEndTime() - zeroTime));
            boolQueryBuilder.should(businessTimeBoolQueryBuilder1);

            BoolQueryBuilder businessTimeBoolQueryBuilder2 = QueryBuilders.boolQuery();
            businessTimeBoolQueryBuilder2.must(QueryBuilders.rangeQuery("businessStartTimeQ2").lte(garageQueryVo.getReserveStartTime() - zeroTime));
            businessTimeBoolQueryBuilder2.must(QueryBuilders.rangeQuery("businessEndTimeQ2").gte(garageQueryVo.getReserveEndTime() - zeroTime));
            boolQueryBuilder.should(businessTimeBoolQueryBuilder2);

            boolQueryBuilder.minimumShouldMatch(Integer.parseInt(Optional.ofNullable(boolQueryBuilder.minimumShouldMatch()).orElse("0")) + 1);
        }

        if (StringUtils.isNotBlank(garageQueryVo.getCompany())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("company", garageQueryVo.getCompany()));

            BoolQueryBuilder rescoreBoolQueryBuilder = QueryBuilders.boolQuery();
            MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("company.prefix", garageQueryVo.getCompany());
            matchPhraseQuery.slop(20);
            rescoreBoolQueryBuilder.should(matchPhraseQuery);

            PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("company", garageQueryVo.getCompany());
            prefixQuery.boost(100);
            rescoreBoolQueryBuilder.should(prefixQuery);

            QueryRescorerBuilder queryRescorerBuilder = new QueryRescorerBuilder(rescoreBoolQueryBuilder);
            searchSourceBuilder.addRescorer(queryRescorerBuilder);

            boolQueryBuilder.minimumShouldMatch(Integer.parseInt(Optional.ofNullable(boolQueryBuilder.minimumShouldMatch()).orElse("0")) + 1);
        }

        if (garageQueryVo.getLat() != null && garageQueryVo.getLng() != null) {
            GeoDistanceSortBuilder geoDistanceSortBuilder = SortBuilders
                    .geoDistanceSort("location", garageQueryVo.getLat(), garageQueryVo.getLng())
                    .geoDistance(GeoDistance.ARC)
                    .unit(DistanceUnit.KILOMETERS)
                    .order(SortOrder.ASC);
            searchSourceBuilder.sort(geoDistanceSortBuilder);
        } else {
            searchSourceBuilder.sort(SortBuilders.fieldSort("avgLevel").order(SortOrder.DESC));
        }

        if (garageQueryVo.getFilterBuildersSupplier() != null) {
            boolQueryBuilder.filter(garageQueryVo.getFilterBuildersSupplier().get());
        }

//        boolQueryBuilder.mustNot(QueryBuilders.termQuery("serviceTel", "null"));
//        boolQueryBuilder.mustNot(QueryBuilders.termQuery("businessStartTime", "null"));

        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        log.info("es request: {}", searchSourceBuilder.toString());
        return searchRequest;
    }


    private final List<GarageInfoDTO> getGarageInfoDTOS(SearchResponse response) {
        log.info("本次搜索耗时: {},结果集大小: {}", response.getTook(), response.getHits().getHits().length);
        return Stream.of(response.getHits().getHits())
                .map(searchHit -> JsonUtil.stringToObj(searchHit.getSourceAsString(), GarageInfoDTO.class))
                .collect(Collectors.toList());
    }

    private final static String disposeTel(String tel) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tel) && tel.contains("-")) {
            String s = tel.substring(tel.indexOf("-") + 1);
            if (StringUtils.isNotBlank(s)) {
                tel = s;
            } else {
                tel = "";
            }
        }
        return tel;
    }

}
