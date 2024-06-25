package com.spring.jpastudy.chap01.entity;

import jdk.jfr.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "nickName")   //⭐️중요~!
@EqualsAndHashCode(of="id")  // 조금더 최적화에 유리 <<필드명 적기>>  예) (of="id","name")    id와 name이 같으면 같은객체이다. 같은 등등 설정 가능
@NoArgsConstructor
@AllArgsConstructor
@Builder
// entity 를 먼저 설계하면 , hibernate가 테이블을 설계한다~!
@Entity
@Table(name="tbl_product")  //실무에서 쓰는 테이블명 사용하기
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //마리아 디비 Identity  /다른거면 Sequence / 다른 것도 있는데 필요시에 ..
    @Column(name="proud_id")
    private Long id;          //pk

    @Setter
    @Column(name="proud_nm",length=30, nullable = false)  // nullable = false : not null
    private String name; //상품명
    @Column(name="price")
    private int price;       //상품 가격

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING )  //ORDINAL : 순차적인    왠만하면 String 으로 바꿔서 써라
    private Category category; // 상품 카테고리

    public enum Category{
        FOOD, FASHION,  ELECTRONIC,
    }
    @CreationTimestamp  // insert 시 자동으로 서버시간 저장
    @Column(updatable = false)  //수정 불가
    private LocalDateTime createdAt; //상품 등록시간

    @UpdateTimestamp
    private LocalDateTime updatedAt;  //상품 수정 시간

    //데이터베이스에는 저장안하고 클래스 내부에서만 사용할 필드
    @Transient
    private String nickName;

    //컬럼 기본값 설정
    @PrePersist
    public void prePersist(){
        if(this.price ==0){
            this.price = 10000;
        }
        if(this.category == null){
            this.category = Category.FOOD;
        }
    }


}
