package com.mpt.openapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpt.openapi.config.UrlConfig;
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
    private String LOGIN_REDIRECT;

    @Value("${mpt.server.suffix.valid}")
    private String VALID;

    @Value("${mpt.server.suffix.access}")
    private String ACCESS;

    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        String url = UrlConfig.AUTH_SERVER.getUrl() + LOGIN_REDIRECT;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath, code, state);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/loginProcess.html");
        mav.addObject("userResponse", result.getBody());
        return mav;
    }

    @GetMapping("/token")
    @CrossOrigin("*")
    public ResponseEntity<String> validCheck(@RequestParam(name = "ACCESS_TOKEN", required = false) String token) throws IOException {
        String url = UrlConfig.AUTH_SERVER.getUrl() + VALID;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, token);

        return ResponseEntity.ok(result.getBody());
    }

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
}