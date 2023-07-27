package com.mpt.openapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpt.openapi.config.UrlConfig;
import com.mpt.openapi.dto.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class AuthController {
    @Value("${mpt.server.suffix.login}")
    private String LOGIN;

    @Value("${mpt.server.suffix.loginRedirect}")
    private String LOGINREDIRECT;

    @Value("${mpt.server.suffix.valid}")
    private String VALID;

    @Value("${mpt.server.suffix.access}")
    private String ACCESS;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/login/{socialLoginType}")
    @CrossOrigin("*")
    public void doLogin(@PathVariable("socialLoginType") String SocialLoginPath, HttpServletResponse response) throws IOException {
        String url = UrlConfig.AUTH_SERVER.getUrl() + LOGIN;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath);

        response.sendRedirect(result.getBody());
    }

    @GetMapping("/login/{socialLoginType}/redirection")
    @CrossOrigin("*")
    public ModelAndView doLoginRedirectCodeNState(@PathVariable("socialLoginType") String SocialLoginPath, @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state) throws JsonProcessingException {
        String url = UrlConfig.AUTH_SERVER.getUrl() + LOGINREDIRECT;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath, code, state);

        UserResponse userResponse = getUserResponse(result);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/loginProcess.html");
        mav.addObject("email", userResponse.getEmail());
        mav.addObject("token", userResponse.getToken());
        mav.addObject("platform", userResponse.getPlatform());
        mav.addObject("status", userResponse.getStatus());
        return mav;
    }

    @GetMapping("/token")
    @CrossOrigin("*")
    public ResponseEntity<String> validCheck(@RequestParam(name = "ACCESS_TOKEN", required = false) String token) throws IOException {
        String url = UrlConfig.AUTH_SERVER.getUrl() + VALID;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, token);

        return ResponseEntity.ok(result.getBody());
    }

    // 헤더에 토큰 추가하여 전달 일반적으로 이렇게 사용합니다.
    @GetMapping("/access_token_info")
    @CrossOrigin("*")
    public ResponseEntity<String> getAccessTokenInfo(@RequestHeader HttpHeaders httpHeaders) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", httpHeaders.getFirst("Authorization"));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        String url = UrlConfig.AUTH_SERVER.getUrl() + ACCESS;

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return ResponseEntity.ok(result.getBody());
    }


    // 컨트롤러에 있어야하는 API는 아닙니다.
    // 동작확인 겸 돌아가는 것 보여드리려고 추가한 코드이니 확인하시고 구조 변경해 주세요.
    public UserResponse getUserResponse(ResponseEntity<String> response) throws JsonProcessingException {
        UserResponse userResponse= objectMapper.readValue(response.getBody(),UserResponse.class);
        return userResponse;
    }
}