package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter

/*
1. 문제: 엔티티 전체 필드에 @Setter가 적용되어 있어 외부에서 임의로 id 또는 데이터를 변경할 수 있습니다.
2. 원인: @Setter가 클래스 단위로 적용
3. 개선안: 엔티티는 생성자를 통해 일관된 상태로만 생성되어야 하므로 @Setter 제거 후 변경이 필요한 필드는 별도의 메소드를 통해 조작합니다.
4. 검증:
*/
@Setter
public class Product {

    @Id
    @Column(name = "product_id")
    /*
    1. 문제: @GeneratedValue(strategy = GenerationType.AUTO)는 데이터베이스에 따라 전략을 자동으로 설정합니다. 또한
            H2 데이터베이스는 인메모리 데이터베이스이므로 임시로 사용하는 데이터베이스입니다. 따라서 데이터베이스 변경 시 전략 문제가 발생할 수 있습니다.
    2. 원인: @GeneratedValue(strategy = GenerationType.AUTO)
    3. 개선안: H2 데이터베이스는 시퀀스 기반이므로 @GeneratedValue(strategy = GenerationType.SEQUENCE)로 수정합니다.
    4. 검증:
    */
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
    1. 문제: column명만 정의되어 있고 다른 옵션이 선언되지 않았습니다.
    2. 원인: @Column(name = "category"), @Column(name = "name")
    3. 개선안: @Column의 nullable 옵션을 사용하여 null 값 여부를 확인합니다.
    4. 검증:
    */
    @Column(name = "category")
    private String category;
    @Column(name = "name")
    private String name;

    /*
    1. 문제: protected 기본 생성자를 수동으로 작성했는데 Lombok 기능을 통해 수동으로 작성하지 않아도 됩니다.
    2. 원인: protected Product()
    3. 개선안: @NoArgsConstructor(access = AccessLevel.PROTECTED)를 사용하여 가독성을 높입니다.
    4. 검증:
    */
    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    /*
    1. 문제: @Getter가 이미 적용되어 있는데도 getter 메소드를 작성하였습니다.
    2. 원인: public String getCategory(), public String getName()
    3. 개선안: 불필요하게 작성된 getter 메소드를 제거하여 코드 수를 줄입니다.
    4. 검증:
    */
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
}
