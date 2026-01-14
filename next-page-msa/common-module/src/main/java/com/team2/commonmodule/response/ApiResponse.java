package com.team2.commonmodule.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team2.commonmodule.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 전역 API 표준 응답 포맷 (Jend-send like).
 * 모든 REST API 응답은 이 클래스로 래핑되어 클라이언트에 전달됩니다.
 *
 * 형태:
 * {
 *   "success": true,
 *   "code": "SUCCESS",
 *   "message": "요청이 성공했습니다.",
 *   "data": { ... }
 * }
 *
 * MSA 환경: 모든 서비스에서 공통으로 사용합니다.
 *
 * @author 정진호
 * @param <T> 응답 데이터의 타입
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Null인 필드는 응답에서 제외
public class ApiResponse<T> {

    /** 요청 성공 여부 */
    private boolean success;

    /** 응답 코드 (성공 시 "SUCCESS" 또는 200, 실패 시 ErrorCode의 code) */
    private String code;

    /** 응답 메시지 (클라이언트 노출용) */
    private String message;

    /** 실제 응답 데이터 (payload) */
    private T data;

    /**
     * 성공 응답을 생성합니다 (Data 포함).
     *
     * @param data 클라이언트에 전달할 데이터 객체
     * @param <T>  데이터 타입
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.code = "SUCCESS";
        response.message = "요청이 성공적으로 처리되었습니다.";
        response.data = data;
        return response;
    }

    /**
     * 성공 응답을 생성합니다 (Data 없음).
     *
     * @return ApiResponse 객체
     */
    public static ApiResponse<Void> success() {
        ApiResponse<Void> response = new ApiResponse<>();
        response.success = true;
        response.code = "SUCCESS";
        response.message = "요청이 성공적으로 처리되었습니다.";
        return response;
    }

    /**
     * 에러 응답을 생성합니다 (ErrorCode 활용).
     * GlobalExceptionHandler에서 사용됩니다.
     *
     * @param errorCode 발생한 에러 코드 정보
     * @return ApiResponse 객체
     */
    public static ApiResponse<Void> error(ErrorCode errorCode) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.success = false;
        response.code = errorCode.getCode();
        response.message = errorCode.getMessage();
        return response;
    }

    /**
     * 에러 응답을 생성합니다 (직접 메시지 지정).
     * 주로 Validation 에러 메시지를 전달할 때 사용됩니다.
     *
     * @param code    커스텀 에러 코드
     * @param message 커스텀 에러 메시지
     * @return ApiResponse 객체
     */
    public static ApiResponse<Void> error(String code, String message) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.success = false;
        response.code = code;
        response.message = message;
        return response;
    }
}
