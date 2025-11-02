package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
/*
1. 문제: 요청 DTO도 굳이 값을 변경할 필요가 없습니다. DTO 필드에 @Setter가 존재해 컨트롤러 이후 계층에서 컨트롤러 이후 계층에서 값이 바뀔 수 있습니다
2. 원인: @Setter가 클래스 단위로 적용
3. 개선안: @Setter를 제거하고 final 키워드를 붙이거나 record로 만들어 불변성을 유지합니다.
*/
@Setter
public class UpdateProductRequest {
    /*
    1. 문제: id, category, name 필드에 유효성 제약이 없어 빈 문자열, null 값, 형식에 안맞는 값이 그대로 넘어 올 수 있습니다.
    2. 원인: id, category, name
    3. 개선안: Bean Validation을 사용하여 유효성 제약을 걸고 Controller에서 유효성 검증을 진행합니다.
    */
    private Long id;
    private String category;
    private String name;


    /*
   1. 문제: 생성자가 여러 개라 역직렬화를 할 생성자를 못고르며 또한 기본 생성자가 없어서 역직렬화에 실패합니다.
   2. 원인: 여러 개의 생성자
   3. 개선안: 하나의 생성자를 사용하던지 여러 생성자를 사용할거면 @NoArgConstructor로 기본 생성자를 만듭니다.
   4. 검증: test_updateProduct_1
   */
    public UpdateProductRequest(Long id) {
        this.id = id;
    }
    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }
    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

