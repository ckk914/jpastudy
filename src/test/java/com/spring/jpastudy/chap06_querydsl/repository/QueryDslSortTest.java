package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslSortTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;


    @Autowired
    JPAQueryFactory factory;

    @BeforeEach
    void setUp() {

        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);

        Idol idol1 = new Idol("김채원", 24, leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, leSserafim);
        Idol idol3 = new Idol("가을", 22, ive);
        Idol idol4 = new Idol("리즈", 20, ive);
        Idol idol5 = new Idol("장원영", 20, ive);

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
    }

    @Test
    @DisplayName("queryDsl로 기본 정렬하기")
    void sortingTest() {
        //given

        //when
        //나이 많은 순 정렬
        List<Idol> sortedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .fetch();
        //then
        assertFalse(sortedIdols.isEmpty());

        System.out.println("\n\n\n");
        sortedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 아이돌이 나이가 가장 많고 이름이 올바르게 정렬되었는지 확인
        assertEquals("사쿠라", sortedIdols.get(0).getIdolName());
        assertEquals(26, sortedIdols.get(0).getAge());
    }

    @Test
    @DisplayName("페이징 처리하기")
    void pagingTest() {
        //given
        //(0) 1페이지~2페이지
//        List<Idol> pageIdols = factory
//                .selectFrom(idol)
//                .orderBy(idol.age.desc())
//                .offset(0)
//                .limit(2)
//                .fetch();
        //when

//        System.out.println("\n\n\n🪿");
//        pageIdols.forEach(System.out::println);

        int pageNo = 1;
        int amount = 2;

        List<Idol> pageIdols2 = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .offset((pageNo-1)*amount)
                .limit(amount)
                .fetch();
        
        //총데이터 수
      
        //when

        System.out.println("\n\n\n🪿2");
        pageIdols2.forEach(System.out::println);

        //널이면 0 을 내보내겠다
        Long totalCount = Optional.ofNullable(factory.select(idol.count())
                .from(idol)
                .fetchOne()).orElse(0L);

        System.out.println("\n\n\n🪿3 = 총합");
        System.out.println("totalCount = " + totalCount);
        assertTrue(totalCount == 5);
        //then
    }


}       //end QueryDslSortTest