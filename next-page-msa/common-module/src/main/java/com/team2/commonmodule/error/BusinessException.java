package com.team2.commonmodule.error;

import lombok.Getter;

/**
 * 비즈니스 로직 수행 중 발생하는 예외를 처리하는 최상위 클래스입니다.
 * ErrorCode를 포함하여 구체적인 에러 상황을 전달합니다.
 *
 * MSA 환경: 모든 서비스에서 공통으로 사용합니다.
 *
 * @author 정진호
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
