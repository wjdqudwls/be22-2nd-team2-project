package com.team2.nextpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Next Page 애플리케이션 메인 클래스
 * 릴레이 소설 창작 플랫폼 서버 애플리케이션의 진입점입니다.
 *
 * @author 정진호
 */
@EnableJpaAuditing
@SpringBootApplication
public class NextPageApplication {

  public static void main(String[] args) {
    SpringApplication.run(NextPageApplication.class, args);
  }

}
