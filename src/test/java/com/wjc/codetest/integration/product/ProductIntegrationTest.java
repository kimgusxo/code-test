package com.wjc.codetest.integration.product;

import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductIntegrationTest {

    @LocalServerPort
    int port;

    WebTestClient client;

    @Autowired
    ProductRepository repo;

    Long savedId;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        repo.deleteAll();

        savedId = repo.save(new Product("category0", "name0")).getId();

        IntStream.range(1, 100)
                .forEach(i -> repo.save(new Product("category"+i, "name"+i)));
    }

    @Test
    @DisplayName("엔티티가 그대로 직렬화되어 내려오는 문제")
    void test_getProductById_1() {
        client.get().uri("/get/product/by/{productId}", savedId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.category").isEqualTo("category0")
                .jsonPath("$.name").isEqualTo("name0");
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 RuntimeException 발생")
    void test_getProductById_2() {
        client.get().uri("/get/product/by/{productId}", 999999L)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("빈 객체를 요청으로 보내도 성공 응답이 반환될것이지만 다중 생성자로 인한 역직렬화 실패로 에러 발생")
    void test_createProduct_1() {
        client.post().uri("/create/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("삭제가 POST로 구현되어 있으며 true 문자열을 반환함")
    void test_deleteProduct_1() {
        client.post().uri("/delete/product/{productId}", savedId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("true");
    }

    @Test
    @DisplayName("업데이트가 POST로 구현됬으며 엔티티 그대로 응답으로 반환됨 또한 다중 생성자로 인한 역직렬화 실패로 에러 발생")
    void test_updateProduct_1() {
        String reqJson = """
            {"id": %d, "category": "category0", "name": "name123"}
            """.formatted(savedId);

        client.post().uri("/update/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reqJson)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("목록 조회가 POST로 구현됬으며 Page 엔티티가 그대로 직렬화됨")
    void test_getProductListByCategory_1() {
        String body = """
            {"category":"category0", "page":0, "size":10}
        """;

        client.post().uri("/product/list")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalElements").isNumber()
                .jsonPath("$.page").isEqualTo(0);
    }

    @Test
    @DisplayName("전체 카테고리 조회 시 페이징 없이 모두 반환됨")
    void test_getProductListByCategory_2() {
        client.get().uri("/product/category/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").value(v -> assertThat((Integer) v).isGreaterThanOrEqualTo(100));
    }
}
