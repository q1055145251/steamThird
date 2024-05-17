package com.example.steamThird.controller;


import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@ApiSupport(order = 1)
@Tag(name = "测试")
@RestController
public class TestController {

    @Operation(summary = "测试")
    @Parameter(name = "aa",description = "啊啊啊",required = true)
    @GetMapping("test")
    public String test(String aa) {
        return aa;
    }

}
