package com.example.steamThird;

import com.example.steamThird.utils.SteamUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Properties;

@SpringBootTest
class SteamThirdApplicationTests {

    @Resource
    private SteamUtils steamUtils;

    @Test
    void contextLoads() throws IOException {

    }

}
