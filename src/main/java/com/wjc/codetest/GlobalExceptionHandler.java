package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: RuntimeException 하나만 처리 중이라 케이스 별 다른 상태코드를 사용자에게 반환할 수 없습니다
2. 원인: RuntimeException 1개만 존재
3. 개선안: 다양한 상태코드에 맞는 핸들러 메소드를 작성합니다.
4. 검증:
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: @ControllerAdvice를 붙이면 메소드 마다 @ResponseBody를 붙여야되서 코드 양이 증가합니다.
2. 원인: @ControllerAdvice
3. 개선안: @RestControllerAdvice를 사용하고 메소드 단에 붙은 @ResponseBody를 제거합니다.
4. 검증:
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 500번으로만 반환하고 있는데 400, 404, 502 등 여러 상황에 맞는 상태코드를 반환해야합니다.
2. 원인: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
3. 개선안: ResponseEntity의 다양한 메소드를 상황에 맞게 사용하거나 에러 공통응답을 만들어서 사용합니다.
          또한 무슨 에러를 상세하게 나타내기 위해 본문을 추가해도 됩니다.
4. 검증:
*/

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    /* -------------------------클래스 전체적인 문제-------------------------
    1. 문제: 핸들러는 RuntimeException을 잡는데 파라미터는 Exception을 받아 다른 예외가 들어왔을때 가로채질 가능성이 있습니다.
    2. 원인: @ExceptionHandler(RuntimeException.class), runTimeException(Exception e)
    3. 개선안: 파라미터를 RuntimeException.class로 변경합니다.
    4. 검증:
    */
    /* -------------------------클래스 전체적인 문제-------------------------
    1. 문제: 상태를 결정하는 방식이 두개가 같이 있어 예사ㅡㅇ치 못한 동작이나 충돌이 발생할 수 있습니다.
    2. 원인: @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    3. 개선안: @ResponseStatus는 없애고 ResponseEntity로만 상태코드를 반환합니다.
    4. 검증:
    */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
