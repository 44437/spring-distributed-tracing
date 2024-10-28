package com.u44437.back_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BackServiceController {
  @GetMapping("/")
  public ResponseEntity getSomething(){
    System.out.println("works");
    return ResponseEntity.ok().build();
  }
}
