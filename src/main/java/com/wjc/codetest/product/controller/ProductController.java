package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 대부분의 메소드가 엔티티를 반환타입으로 사용하여 반환 시 클라이언트에게 불필요한 데이터를 노출할 수 있고
        계층 간에 의존성이 강해지고 결합도가 높아져 엔티티 수정 시 문제가 생깁니다.
2. 원인: 엔티티를 직접 직렬화함
3. 개선안: DTO를 만들어서 사용하거나 원래 작성된 DTO를 사용해야합니다.
4. 검증:
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 동사 기반 경로에 루트 도메인도 없습니다. REST API 설계가 통일되어 있지 않습니다.
2. 원인: @RequestMapping
3. 개선안: @RequestMapping("/product") 정도로 루트를 지정하고 현재는 행위가 URI에 포함되어 있는데
          행위는 메소드로 표현하므로 제거하여 사용합니다.
4. 검증:
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 200번으로만 반환하고 있는데 생성 성공 시 201번 삭제 성공시 204번을 사용해야합니다.
2. 원인: ResponseEntity.ok
3. 개선안: ResponseEntity의 다양한 메소드를 상황에 맞게 사용하거나 공통응답을 만들어서 사용합니다.
4. 검증:
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 요청에 대한 유효성 검증이 존재하지 않습니다.
2. 원인: @Valid / @Validated의 부재
3. 개선안: 요청DTO에 Bean Validation을 사용하고 컨트롤러에 @Valid를 붙여 유효성 검증을 합니다.
4. 검증:
*/

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    /*
    1. 문제: 삭제를 @PostMapping으로 진행하여 REST 원칙에 위배됩니다.
    2. 원인: @PostMapping
    3. 개선안: @DeleteMapping으로 변경합니다.
    4. 검증:
    */
    /*
    1. 문제: boolean형을 반환하고 있는데 삭제 성공/실패는 HTTP 상태코드로 처리가능합니다.
    2. 원인: ResponseEntity.ok(true);
    3. 개선안: 성공 시 204, 존재하지 않으면 404, 제약으로 인한 삭제 불가는 409로 반환합니다.
    4. 검증:
    */
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    /*
    1. 문제: 업데이트를 @PostMapping으로 진행하여 REST 원칙에 위배됩니다.
    2. 원인: @PostMapping
    3. 개선안: @PutMapping으로 변경합니다.
    4. 검증:
    */
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    /*
    1. 문제: 리스트 조회를 @PostMapping으로 처리하여 REST 원칙에 위배됩니다.
    2. 원인: @PostMapping
    3. 개선안: 요청DTO를 사용하지 않고 파라미터를 id와 Pageable로 나누고 @GetMapping으로 변경합니다.
    4. 검증:
    */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    /*
    1. 문제: 카테고리 데이터가 많을 시 리스트에 전부 로딩하여 비용 소모가 큼
    2. 원인: ResponseEntity<List<String>> getProductListByCategory()
    3. 개선안: 파라미터로 Pageable을 받아서 페이징 처리를 하여 반환합니다.
    4. 검증:
    */
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}