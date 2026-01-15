package com.team2.commonmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Common Module Application
 *
 * <p>
 * MSA 프로젝트 전역에서 사용되는 공통 모듈입니다.
 * 모든 서비스에서 공유하는 DTO, 유틸리티, 예외 처리 등을 제공합니다.
 * </p>
 *
 * @author Next-Page Team
 */
@SpringBootApplication
public class CommonModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonModuleApplication.class, args);
	}

}
