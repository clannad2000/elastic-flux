package com.nurkiewicz.elasticflux;

import com.google.common.collect.ImmutableMap;
import com.nurkiewicz.elasticflux.model.dto.GarageInfoDTO;
import com.nurkiewicz.elasticflux.model.vo.GarageQueryVo;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
class PersonController {

    private final static Mono<ResponseEntity<String>> NOT_FOUND = Mono.just(ResponseEntity.notFound().build());

    private final ElasticAdapter elasticAdapter;

    private final GarageESMapper garageESMapper;

    @PostMapping("/test")
    Mono<ResponseEntity<List<GarageInfoDTO>>> search(@RequestBody GarageQueryVo garageQueryVo) {
        return garageESMapper
                .test(garageQueryVo)
                .map(ResponseEntity::ok);
    }

    @PutMapping
    Mono<ResponseEntity<Map<String, Object>>> put(@Valid @RequestBody Person person) {
        return elasticAdapter
                .index(person)
                .map(this::toMap)
                .map(m -> ResponseEntity.status(HttpStatus.CREATED).body(m));
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<String>> get(@PathVariable("id") String id) {
        return elasticAdapter
                .findById(id)
                .map(ResponseEntity::ok)
                //.switchIfEmpty(Mono.create(sink->new ResponseEntity<String>(HttpStatus.OK)));
                //.switchIfEmpty(NOT_FOUND);
                .switchIfEmpty(Mono.just(ResponseEntity.ok("null")));
    }

    private final ImmutableMap<String, Object> toMap(IndexResponse response) {
        return ImmutableMap
                .<String, Object>builder()
                .put("id", response.getId())
                .put("index", response.getIndex())
                //.put("type", response.getType())
                .put("version", response.getVersion())
                .put("result", response.getResult().getLowercase())
                .put("seqNo", response.getSeqNo())
                .put("primaryTerm", response.getPrimaryTerm())
                .build();
    }

}

