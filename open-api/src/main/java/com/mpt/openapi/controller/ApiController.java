package com.mpt.openapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@RestController
public class ApiController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/goods/{goods_id}")
	public ResponseEntity<String> getGoodsById(@PathVariable("goods_id") int data) {

		String url = "http://localhost:8080/api/goods/" + data;

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
				.fromUriString("http://localhost:8080")
				.path("/api/goods")
				.queryParam("salemode", param.get("salemode"))
				.queryParam("page", param.get("page"))
				.encode()
				.build()
				.toUri();

		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		return ResponseEntity.ok(result.getBody());
	}
}