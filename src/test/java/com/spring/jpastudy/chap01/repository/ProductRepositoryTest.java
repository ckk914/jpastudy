package com.spring.jpastudy.chap01.repository;

import com.spring.jpastudy.chap01.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback               //실제 db에 넣지 않기 위함
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품을 db에 저장한다")
    void saveTest() {
        //given
        Product product = Product.builder()
                .name("신발")
                .price(140000)
                .category(Product.Category.FASHION)
                .build();
        //when
        //insert 후 저장된 데이터의 객체를 반환
        Product saved = productRepository.save(product);


        //then
        assertNotNull(saved);
    }
}