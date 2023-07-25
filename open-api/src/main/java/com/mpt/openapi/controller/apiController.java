package com.mpt.openapi.controller;

import com.mpt.openapi.common.MptServerInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
public class apiController {
	@RestController
	public class ApiController {
		@Autowired
		private RestTemplate restTemplate;

		@GetMapping("/goods/{goods_id}")
		public ResponseEntity<String> getMethodWithID(@PathVariable("goods_id") int data) {

			String url = MptServerInfo.GOODS_SERVER.getUrl() + "/api/goods/{goods_id}";

			ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, data);

			return ResponseEntity.ok(result.getBody());
		}

		@GetMapping("/goods")
		public ResponseEntity<String> getgoodsListMethod() {

			String url = MptServerInfo.GOODS_SERVER.getUrl() +"/api/goods";
			ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

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

			ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, SocialLoginPath,code,state);

			return ResponseEntity.ok(result.getBody());
		}

		@GetMapping("/token")
		public ResponseEntity<String> validCheck(@RequestParam(name = "ACCESS_TOKEN", required = false) String token) throws IOException {
			String url = MptServerInfo.AUTH_SERVER.getUrl() + "/api/token?ACCESS_TOKEN={ACCESS_TOKEN}";

			ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, token);

			return ResponseEntity.ok(result.getBody());
		}
	}
}