package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
/*
1. 문제: 요청 DTO도 굳이 값을 변경할 필요가 없습니다. DTO 필드에 @Setter가 존재해 컨트롤러 이후 계층에서 컨트롤러 이후 계층에서 값이 바뀔 수 있습니다
2. 원인: @Setter가 클래스 단위로 적용
3. 개선안: @Setter를 제거하고 final 키워드를 붙이거나 record로 만들어 불변성을 유지합니다.
4. 검증:
*/
@Setter
public class UpdateProductRequest {
    /*
    1. 문제: id, category, name 필드에 유효성 제약이 없어 빈 문자열, null 값, 형식에 안맞는 값이 그대로 넘어 올 수 있습니다.
    2. 원인: id, category, name
    3. 개선안: Bean Validation을 사용하여 유효성 제약을 걸고 Controller에서 유효성 검증을 진행합니다.
    4. 검증:
    */
    private Long id;
    private String category;
    private String name;

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

