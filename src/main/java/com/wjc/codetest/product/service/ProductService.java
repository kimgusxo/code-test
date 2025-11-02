package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: @Transactional이 없어 영속성 컨텍스트의 장점을 사용하지 못합니다.
2. 원인: 읽기/쓰기 메소드에 @Transactional이 존재하지 않음
3. 개선안: 조회 메소드에는 @Transactional(readOnly = true), 나머지 생성/수정/삭제 메소드에 대해선 @Transactional을 붙입니다.
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: 대부분의 메소드가 엔티티를 반환타입으로 사용하여 반환 시 클라이언트에게 불필요한 데이터를 노출할 수 있고
        계층 간에 의존성이 강해지고 결합도가 높아져 엔티티 수정 시 문제가 생깁니다.
2. 원인: 반환 타입을 엔티티로 사용
3. 개선안: DTO를 만들어서 사용하거나 원래 작성된 DTO를 사용해야합니다.
*/
/* -------------------------클래스 전체적인 문제-------------------------
1. 문제: getProductById()에만 예외 처리가 되어있고 나머지 메소드는 예외처리가 되어있지 않습니다.
        또한 RuntimeException은 말하고자 하는바가 너무 모호합니다.
2. 원인: 예외처리 미흡 및 RuntimeException
3. 개선안: 여러 예외 클래스를 만들어 메소드 별로 맞는 예외처리 로직을 작성합니다.
*/

/*
1. 문제: 로그를 사용한 흔적이 없는데 @Slf4j가 선언되어 있습니다.
2. 원인: @Slf4j
3. 개선안: 필요한 로그를 작성하거나 필요없다면 삭제해야 합니다.
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    /*
    1. 문제: 엔티티를 setter로 직접 변경하여 무결성에 위배됩니다.
    2. 원인: product.setCategory(dto.getCategory()), product.setName(dto.getName());
    3. 개선안: 업데이트에 필요한 변수만 수정할 수 있는 별도의 메소드를 만들어 사용합니다.
    */
    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    /*
    1. 문제: 카테고리 데이터가 많을 시 리스트에 전부 로딩하여 비용 소모가 큼
    2. 원인: List<String> getUniqueCategories()
    3. 개선안: Repository를 Page<String>으로 변경하고 Pageable을 파라미터로 받아서 호출합니다.
    */
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}