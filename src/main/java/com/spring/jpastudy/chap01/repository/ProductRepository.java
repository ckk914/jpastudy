package com.spring.jpastudy.chap01.repository;

import com.spring.jpastudy.chap01.entity.Product;
import com.spring.jpastudy.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
//extends JpaRepository 만 붙여주면 됨~!⭐️ (crud 완료)
// jpaRepository를 상속한후
//첫번쨰 제너릭엔 엔터티 클래스 타입,
// 두번째 제널익엔 pk 타입 넣기 !
public interface ProductRepository extends JpaRepository<Product, Long> {


}
