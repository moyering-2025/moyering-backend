package com.dev.moyering.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @GetMapping("/sido")
    public ResponseEntity<String> getSido(@RequestParam("lat") double lat,@RequestParam("lng") double lng) {
        String url = String.format(
                "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%f&y=%f", lng, lat
            );

         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

         HttpEntity<Void> entity = new HttpEntity<>(null, headers);


         ResponseEntity<String> response = restTemplate.exchange(
    		 url,
             HttpMethod.GET,
             entity,
             String.class
         );
         System.out.println(response.getBody());

         try {
             ObjectMapper mapper = new ObjectMapper();
             JsonNode root = mapper.readTree(response.getBody());
             String sido = root.get("documents").get(0).get("region_1depth_name").asText();
             return ResponseEntity.ok(sido); // ⚠️ JSON 말고 문자열만 리턴!
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주소 분석 실패");
         }

     }
}
