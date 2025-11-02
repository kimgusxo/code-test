package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
/*
1. 문제: 응답DTO는 불변성을 가져야 합니다 하지만 지금은 DTO 필드에 @Setter가 존재해 컨트롤러 이후 계층에서 컨트롤러 이후 계층에서 값이 바뀔 수 있습니다
2. 원인: @Setter가 클래스 단위로 적용
3. 개선안: @Setter를 제거하고 final 키워드를 붙이거나 record로 만들어 불변성을 유지합니다.
*/
@Setter
public class ProductListResponse {
    /*
    1. 문제: 응답에 엔티티가 노출되어 있으므로 직렬화 시 N+1 문제가 발생할 수 있습니다.
    2. 원인: List<Product> products;
    3. 개선안: List<ProductResponse> items 같이 엔티티를 DTO로 만들어서 사용합니다.
    */
    private List<Product> products;
    private int totalPages;
    private long totalElements;
    private int page;

    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
