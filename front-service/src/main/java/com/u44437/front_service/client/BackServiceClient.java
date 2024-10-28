package com.u44437.front_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backServiceClient", url = "http://localhost:8081")
public interface BackServiceClient {
  @GetMapping("/")
  ResponseEntity getSomething();
}
