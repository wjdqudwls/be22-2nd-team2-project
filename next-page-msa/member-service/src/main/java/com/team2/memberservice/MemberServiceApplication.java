package com.team2.memberservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Member Service Application
 * 회원 관리 및 인증을 담당하는 마이크로서비스
 *
 * @author Next-Page Team (김태형)
 */
@SpringBootApplication(scanBasePackages = {
    "com.team2.memberservice",
    "com.team2.commonmodule"
})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
