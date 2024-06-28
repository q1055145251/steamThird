package com.example.steamThird.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("test")
    public String test(String aa) {

        return aa;
    }

}
