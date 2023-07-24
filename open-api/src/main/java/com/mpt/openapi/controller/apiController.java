package com.mpt.openapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class apiController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/goods/{goods_id}")
	public ResponseEntity<String> getMethodWithID(@PathVariable("goods_id") int data) {
		System.out.println("param = " + data);

		String url;

		ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, data);

		return ResponseEntity.ok(result.getBody());
	}

	@GetMapping("/users/{user_id}")
	public ResponseEntity<String> getMethodWithID(@PathVariable("user_id") String id) {
		System.out.println("param = " + id);

		String url;

		ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, id);

		return ResponseEntity.ok(result.getBody());
	}

	@GetMapping("/users")
	public ResponseEntity<String> getListMethod() {
		
		String url;
		ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
		//List result = restTemplate.getForObject(url, List.class);

		return ResponseEntity.ok(result.getBody());
	}
}
