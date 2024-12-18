package com.example.steamThird.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;


@RestController
public class TestController {

    @GetMapping("test")
    public String test(String aa) {

        return aa;
    }

    @GetMapping("qencrypt")
    public String test(String text, int key) {
        byte[] s = DatatypeConverter.parseHexBinary(text.replace(" ", ""));
        return TestUtils.qencrypt(s, key);
    }

    @GetMapping("qencrypt2")
    public String qencrypt(String hex, int key) {
        return TestUtils.qencrypt(hex, key);
    }

}
