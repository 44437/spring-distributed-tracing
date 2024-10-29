package com.u44437.front_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "backServiceClient", url = "http://localhost:8081")
public interface BackServiceClient {
  @GetMapping("/")
  void getSomething(@RequestHeader Map<String, String> headers);
}
