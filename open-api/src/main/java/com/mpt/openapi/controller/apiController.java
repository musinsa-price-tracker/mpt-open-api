package com.mpt.openapi.controller;

import com.mpt.openapi.common.MptServerInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

@RestController
public class ApiController {

    private final RestTemplate restTemplate;

    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/goods/{goods_id}")
    public ResponseEntity<String> getGoodsById(@PathVariable("goods_id") int data) {

        String url = MptServerInfo.GOODS_SERVER.getUrl() + "/api/goods/{goods_id}";

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, data);

        return ResponseEntity.ok(result.getBody());
    }


    @GetMapping("/goods")
    public ResponseEntity<String> getGoods(
            @RequestParam(required = false) HashMap<String, String> param
    ) {
        /**
         * saleMode= yesterday, priceComparison
         * page = 1,2,3...
         * ex) localhost:8080/goods?salemode=yesterday&page=1&search=하얀색 상의
         */
        if (param.get("salemode").equals("")) {
            param.put("salemode", "yesterday");
        }
        if (param.get("page").equals("")) {
            param.put("page", "1");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(MptServerInfo.GOODS_SERVER.getUrl())
                .path("/api/goods")
                .queryParam("salemode", param.get("salemode"))
                .queryParam("page", param.get("page"))
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        return ResponseEntity.ok(result.getBody());
    }

    @GetMapping("/login/{socialLoginType}")
    public void doLogin(@PathVariable("socialLoginType") String SocialLoginPath, HttpServletResponse response) throws IOException {
        String url = MptServerInfo.AUTH_SERVER.getUrl() + "/api/login/{socialLoginType}";

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath);

        response.sendRedirect(result.getBody());
    }

    @GetMapping("/login/{socialLoginType}/redirection")
    public ResponseEntity<String> doLoginRedirectCodeNState(@PathVariable("socialLoginType") String SocialLoginPath, @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state) {
        String url = MptServerInfo.AUTH_SERVER.getUrl() + "/api/login/{socialLoginType}/redirection?code={code}&state={state}";

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath, code, state);

        return ResponseEntity.ok(result.getBody());
    }

    @GetMapping("/token")
    public ResponseEntity<String> validCheck(@RequestParam(name = "ACCESS_TOKEN", required = false) String token) throws IOException {
        String url = MptServerInfo.AUTH_SERVER.getUrl() + "/api/token?ACCESS_TOKEN={ACCESS_TOKEN}";

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, token);

        return ResponseEntity.ok(result.getBody());
    }
}