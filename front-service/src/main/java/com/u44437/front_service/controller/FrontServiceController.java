package com.u44437.front_service.controller;

import com.u44437.front_service.client.BackServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class FrontServiceController {
  private final BackServiceClient backServiceClient;

  public FrontServiceController(BackServiceClient backServiceClient) {
    this.backServiceClient = backServiceClient;
  }

  @GetMapping("/")
  public ResponseEntity getSomething(){
    ResponseEntity responseEntity = backServiceClient.getSomething();

    return responseEntity;
  }
}
