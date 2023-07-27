package com.mpt.openapi.controller;

import com.mpt.openapi.config.UrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@Slf4j
@RestController
public class AlarmController {

    private final RestTemplate restTemplate;

    @Value("${my.url.alarmExistence}")
    private String ALARM_EXISTENCE;

    @Value("${my.url.alarm}")
    private String ALARM_CREATION;

    public AlarmController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/goods/{goods_id}/alarm")
    @CrossOrigin("*")
    public ResponseEntity<String> createAlarm(@RequestHeader HttpHeaders httpHeaders, @PathVariable("goods_id") int goodsId, @RequestParam(required = false) HashMap<String, String> param) {
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity(httpHeaders);
        if (!param.containsValue("alarmType")) {
            param.put("alarmType", "1");
        }
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(UrlConfig.ALARM_SERVER.getUrl())
                    .path(ALARM_CREATION)
                    .queryParam("goods_id", goodsId)
                    .queryParam("alarmType", param.get("alarmType"))
                    .queryParam("target_price", param.get("target_price"))
                    .encode().build().toUri();

            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, requestHeader, String.class);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            System.out.println("e: " + e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: create Alarm failed.");//가격 설정하라는 메시지
    }


    @GetMapping("/goods/alarm/{goods_id}")
    @CrossOrigin("*")
    public ResponseEntity<String> hasAlarm(@RequestHeader HttpHeaders httpHeaders, @PathVariable("goods_id") int goods_id) {
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity(httpHeaders);
        String url = UrlConfig.ALARM_SERVER.getUrl() + ALARM_EXISTENCE + String.valueOf(goods_id);
        try {
            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestHeader, String.class);
            return ResponseEntity.status(HttpStatus.OK).body(result.getBody());
        } catch (Exception e) {
            System.out.println("e: " + e);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This goods has no alarm.");//가격 설정하라는 메시지
    }
}
