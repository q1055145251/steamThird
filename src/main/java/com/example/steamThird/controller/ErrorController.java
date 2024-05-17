package com.example.steamThird.controller;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "错误处理")
@Hidden
@RestController
@Validated
public class ErrorController {
    /**
     * 重新抛出异常
     */
    @RequestMapping("/error/exthrow")
    public void rethrow(HttpServletRequest request) throws Exception {
        throw ((Exception) request.getAttribute("filter.error"));
    }
}
