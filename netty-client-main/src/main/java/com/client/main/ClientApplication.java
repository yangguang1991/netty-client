package com.client.main;

import com.client.server.CpmpNettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: kevin yang
 * @Description:
 * @Date: create in 2021/4/13 14:57
 */
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableScheduling
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        //System.setProperty("spring.cloud.consul.host","192.168.110.66");
        System.setProperty("spring.profiles.active","dev");
        SpringApplication.run(ClientApplication.class, args);
    }

    @Value("${cpmp.server.port}")
    private int port;
    @Value("${cpmp.server.ip}")
    private String ip;

    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CpmpNettyServer cpmpNettyServer = new CpmpNettyServer(port, ip);
                cpmpNettyServer.start();
            }
        }).start();
    }


}
