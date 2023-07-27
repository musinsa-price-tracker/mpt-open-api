package com.mpt.openapi.controller;

import com.mpt.openapi.config.UrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@Slf4j
@RestController
public class GoodsController {

    private final RestTemplate restTemplate;

    @Value("${my.url.goods}")
    private String GOODS_URL;

    @Value("${my.url.goodsList}")
    private String GOODS_LIST_URL;

    public GoodsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/goods/{goods_id}")
    @CrossOrigin("*")
    public ResponseEntity<String> getGoodsById(@PathVariable("goods_id") int data) {

        log.info("before call url");
        String url = UrlConfig.GOODS_SERVER.getUrl() + GOODS_URL;
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, data);
        log.info("result = {}", result.getBody());
        return ResponseEntity.ok(result.getBody());
    }

    @GetMapping("/goods")
    @CrossOrigin("*")
    public ResponseEntity<String> getGoods(
            @RequestParam(required = false) HashMap<String, String> param
    ) {
        /**
         * saleMode= yesterday, priceComparison
         * page = 1,2,3...
         * ex) GOODS_SERVER/goods?saleType=yesterday&page=1&search=하얀색 상의
         */
        if (!param.containsKey("saleType") && !param.containsKey("page")) {
            param.put("saleType", "yesterday");
            param.put("page", "1");
        }
        else if (!param.containsKey("saleType")){
            param.put("saleType", "yesterday");
        }
        else if (!param.containsKey("page")) {
            param.put("page", "1");
        }
        // UriComponentsBuilder -> Uri 편하게 만들기 위하여 사용. <- 안쓰면 직접 path?key=value 다 설정해야 함.
        URI uri = UriComponentsBuilder
                .fromUriString(UrlConfig.GOODS_SERVER.getUrl())
                .path("/api/goods")
                .queryParam("saleMode", param.get("salemode"))
                .queryParam("page", param.get("page"))
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        return ResponseEntity.ok(result.getBody());
    }

    @GetMapping("/goods/chart/{goods_id}")
    @CrossOrigin("*")
    public ResponseEntity<String> getGoodsListChart(@PathVariable("goods_id") int data, @RequestHeader HttpHeaders httpHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",httpHeaders.getFirst("Authorization"));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        String url = UrlConfig.GOODS_SERVER.getUrl() + GOODS_LIST_URL;

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,request,String.class,data);

        return ResponseEntity.ok(result.getBody());
    }
}
