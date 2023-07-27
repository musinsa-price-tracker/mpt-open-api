package com.mpt.openapi.controller;

import com.mpt.openapi.config.UrlConfig;
import lombok.extern.slf4j.Slf4j;
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

    public AlarmController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/goods/{goods_id}/alarm") // /goods/605448/alarm?targetPrice=10000
    public ResponseEntity<String> createAlarm(@RequestHeader HttpHeaders httpHeaders, @PathVariable("goods_id") Long goods_id, @RequestParam(required = false) HashMap<String, String> param) {
        HttpEntity<MultiValueMap<String, String>> requestHeader = new HttpEntity(httpHeaders);
        if (!param.containsValue("alarmType")) {
            param.put("alarmType", "1");
        }
        try {
            for (String s : param.values()) {
                System.out.println("param = " + s);
            }
            URI uri = UriComponentsBuilder
                .fromUriString(UrlConfig.ALARM_SERVER.getUrl())
                    .path("/api/alarm")
                    .queryParam("goods_id", goods_id)
                    .queryParam("alarmType", param.get("alarmType"))
                    .queryParam("targetPrice", param.get("targetPrice"))
                    .encode()
                    .build()
                    .toUri();
            System.out.println(uri);
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, requestHeader, String.class);
            log.info("result : {}", result);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            System.out.println("e" + e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: create Alarm failed.");//가격 설정하라는 메시지
    }
}
