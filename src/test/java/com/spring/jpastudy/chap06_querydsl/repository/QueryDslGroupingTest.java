package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.dto.GroupAverageAgeDto;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslGroupingTest {

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
        Group bts = new Group("방탄소년단");
        Group newjeans = new Group("뉴진스");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        groupRepository.save(bts);
        groupRepository.save(newjeans);

        Idol idol1 = new Idol("김채원", 24, "여", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "여", leSserafim);
        Idol idol3 = new Idol("가을", 22, "여", ive);
        Idol idol4 = new Idol("리즈", 20, "여", ive);
        Idol idol5 = new Idol("장원영", 20, "여", ive);
        Idol idol6 = new Idol("안유진", 21, "여", ive);
        Idol idol7 = new Idol("카즈하", 21, "여", leSserafim);
        Idol idol8 = new Idol("RM", 29, "남", bts);
        Idol idol9 = new Idol("정국", 26, "남", bts);
        Idol idol10 = new Idol("해린", 18, "여", newjeans);
        Idol idol11 = new Idol("혜인", 16, "여", newjeans);

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
        idolRepository.save(idol6);
        idolRepository.save(idol7);
        idolRepository.save(idol8);
        idolRepository.save(idol9);
        idolRepository.save(idol10);
        idolRepository.save(idol11);
    } // set up end
    @Test
    @DisplayName("성별별, 그룹별로   그룹화 하고, 아이돌의 숫자가 3명 이하인 그룹만 조회")
    void groupByGenderTest () {
        //given
        /*
        * SELECT G.*, I.GENDER, COUNT(I.idol_id)
        * FROM tbl_idol I
        * JOIN tbl_group g
        * ON I.group_id  = G.group_id
        * Group by G.group_id, I.gender
        * */
        //        ll
        //when
        List<Tuple> idolList = factory
                .select(idol.group,idol.gender,idol.count())
                .from(idol)
                .groupBy(idol.gender, idol.group)
                .having(idol.count().loe(3))
                .fetch();
/*
* ⌛ ︎having 추가 전
idolList = [[Group(groupName=방탄소년단,
* idols=[Idol(id=8, idolName=RM, age=29, gender=남), Idol(id=9, idolName=정국, age=26,
* gender=남)]), 남, 2],
*
* [Group(groupName=르세라핌, idols=[Idol(id=1, idolName=김채원, age=24, gender=여),
* Idol(id=2, idolName=사쿠라, age=26, gender=여), Idol(id=7, idolName=카즈하, age=21, gender=여)]), 여, 3],
*
* [Group(groupName=아이브, idols=[Idol(id=3, idolName=가을, age=22, gender=여),
* Idol(id=4, idolName=리즈, age=20, gender=여),
* Idol(id=5, idolName=장원영, age=20, gender=여),
*  Idol(id=6, idolName=안유진, age=21, gender=여)]), 여, 4],
*
* [Group(groupName=뉴진스, idols=[Idol(id=10, idolName=해린, age=18, gender=여),
* Idol(id=11, idolName=혜인, age=16, gender=여)]), 여, 2]]
* */
        //having 추가하면 4명 그룹 사라짐 ⭐️
        /*
        * ⌛ ︎
그룹명 : 방탄소년단, 성별: 남, 인원수: 2

그룹명 : 르세라핌, 성별: 여, 인원수: 3

그룹명 : 뉴진스, 성별: 여, 인원수: 2
        * */
        //then
        System.out.println("\n\n\n⌛ ︎");
//        System.out.println("idolList = " + idolList);
        for( Tuple tuple: idolList){
            Group group = tuple.get(idol.group);
            String gender = tuple.get(idol.gender);
            Long count = tuple.get(idol.count());
            System.out.println(
                    String.format("\n그룹명 : %s, 성별: %s, 인원수: %d\n"
                            , group.getGroupName(), gender, count)
            );

        }
        /*
        *  ⌛ ︎
            idolList = [[남, 2], [여, 9]]
        * */
    }

    @Test
    @DisplayName("연령대별로 그룹화하여 아이돌 수를 조회")
    void ageGroupTest() {

        /*
            SELECT
                CASE age WHEN BETWEEN 10 AND 19 THEN 10
                CASE age WHEN BETWEEN 20 AND 29 THEN 20
                CASE age WHEN BETWEEN 30 AND 39 THEN 30
                END,
                COUNT(idol_id)
            FROM tbl_idol
            GROUP BY
                CASE age WHEN BETWEEN 10 AND 19 THEN 10
                CASE age WHEN BETWEEN 20 AND 29 THEN 20
                CASE age WHEN BETWEEN 30 AND 39 THEN 30
                END

         */

        //given

        // QueryDSL로 CASE WHEN THEN 표현식 만들기
        NumberExpression<Integer> ageGroupExpression = new CaseBuilder()
                .when(idol.age.between(10, 19)).then(10)
                .when(idol.age.between(20, 29)).then(20)
                .when(idol.age.between(30, 39)).then(30)
                .otherwise(0);

        //when
        List<Tuple> result = factory
                .select(ageGroupExpression, idol.count())
                .from(idol)
                .groupBy(ageGroupExpression)
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            int ageGroupValue = tuple.get(ageGroupExpression);
            long count = tuple.get(idol.count());

            System.out.println("\n\nAge Group: " + ageGroupValue + "대, Count: " + count);
            
            /*
            * Age Group: 10대, Count: 2


                Age Group: 20대, Count: 9
            * */
        }
    }
    
    @Test
    @DisplayName("아이돌 그룹명, 평균나이 조회. 평균 나이 20~25세 사이인그룹만 조회하기~!")
    void groupAvgAgeTest() {
        //given
        /*
        *  select g.group_name, avg(i.age)
        * from tbl_idol i
        * join tbl_group g
        * on i.group_id = g. group_id
        * group by g.group_id
        * having avg(i.age) between 20 and 25
        * */
        List<Tuple> result = factory
                .select(idol.group.groupName, idol.age.avg())   //그룹 네임 , 나이 평균~!
                .from(idol)
                .groupBy(idol.group)        //여기서 조인 나간다₩!
                .having(idol.age.avg().between(20,25))  //20~25세 아니면 짜름
                .fetch();
        //when
        //해빙 전
//        Group: 르세라핌, Average Age: 23.6667
//        Group: 아이브, Average Age: 20.75
//        Group: 방탄소년단, Average Age: 27.5
//        Group: 뉴진스, Average Age: 17.0
        //해빙 후
//        Group: 르세라핌, Average Age: 23.6667
//        Group: 아이브, Average Age: 20.75

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            String groupName = tuple.get(idol.group.groupName);
            double averageAge = tuple.get(idol.age.avg());

            System.out.println("\n\nGroup: " + groupName + ", Average Age: " + averageAge);
        }
    }

    //
    @DisplayName("그룹별 평균 나이 조회 (결과 DTO 처리)")
    @Test
    void groupAverageAgeDtoTest() {

        /*
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */

        // Projections : 커스텀 DTO를 포장해주는 객체
        List<GroupAverageAgeDto> result = factory
                .select(
                        Projections.constructor(
                                GroupAverageAgeDto.class,
                                idol.group.groupName,
                                idol.age.avg()
                        )
                )
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (GroupAverageAgeDto dto : result) {
            String groupName = dto.getGroupName();
            double averageAge = dto.getAverageAge();

            System.out.println("\n\nGroup: " + groupName
                    + ", Average Age: " + averageAge);
            //    Group: 르세라핌, Average Age: 23.6667
        }
    }




}       //end QueryDslSortTest