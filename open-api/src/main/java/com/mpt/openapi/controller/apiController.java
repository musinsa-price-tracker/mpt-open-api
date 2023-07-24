package com.mpt.openapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class apiController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/goods/{goods_id}")
	public ResponseEntity<String> getMethodWithID(@PathVariable("goods_id") int data) {

		String url;

		ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, data);

		return ResponseEntity.ok(result.getBody());
	}

	@GetMapping("/goods")
	public ResponseEntity<String> getgoodsListMethod() {
		
		String url;
		ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

		return ResponseEntity.ok(result.getBody());
	}
}