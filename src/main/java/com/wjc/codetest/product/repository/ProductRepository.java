package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    1. 문제: 카테고리명으로 상품을 찾는데 매개변수는 String name을 사용하여 가독성이 떻어집니다.
    2. 원인: String name
    3. 개선안: 파라미터를 String category로 변경합니다.
    */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    /*
    1. 문제: 카테고리 데이터가 많다면 List로 전부 가져오는것은 비용이 너무 많이 듭니다.
    2. 원인: List<String> findDistinctCategories()
    3. 개선안: 페이지네이션을 적용하여 조회합니다.
    */
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
