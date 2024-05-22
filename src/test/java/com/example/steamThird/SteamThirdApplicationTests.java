package com.example.steamThird;

import com.example.steamThird.utils.SteamUtils;
import in.dragonbra.javasteam.steam.steamclient.SteamClient;
import in.dragonbra.javasteam.steam.steamclient.callbackmgr.CallbackManager;
import in.dragonbra.javasteam.util.log.DefaultLogListener;
import in.dragonbra.javasteam.util.log.LogManager;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SteamThirdApplicationTests {

    @Resource
    private SteamUtils steamUtils;

    @Test
    void contextLoads() throws IOException {

//        steamUtils.getAppInfo(578080);
    }

}
