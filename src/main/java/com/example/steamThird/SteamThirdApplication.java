package com.example.steamThird;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Properties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
public class SteamThirdApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamThirdApplication.class, args);
    }

}
