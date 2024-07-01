package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Album;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QAlbum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spring.jpastudy.chap06_querydsl.entity.QAlbum.album;
import static com.spring.jpastudy.chap06_querydsl.entity.QGroup.group;
import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslSubQueryTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AlbumRepository albumRepository;

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
        Idol idol12 = new Idol("김종국", 48, "남", null);
        Idol idol13 = new Idol("아이유", 31, "여", null);


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
        idolRepository.save(idol12);
        idolRepository.save(idol13);


        Album album1 = new Album("MAP OF THE SOUL 7", 2020, bts);
        Album album2 = new Album("FEARLESS", 2022, leSserafim);
        Album album3 = new Album("UNFORGIVEN", 2023, bts);
        Album album4 = new Album("ELEVEN", 2021, ive);
        Album album5 = new Album("LOVE DIVE", 2022, ive);
        Album album6 = new Album("OMG", 2023, newjeans);
        Album album7 = new Album("AFTER LIKE", 2022, ive);

        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);
        albumRepository.save(album4);
        albumRepository.save(album5);
        albumRepository.save(album6);
        albumRepository.save(album7);



    }

@Test
@DisplayName("특정 그룹의 평균 나이보다 많은 아이돌 조회")
void subQueryTest1() {
    //given
    List<Idol> result = factory
            .select(idol)
            .from(idol)
            .where(idol.age.gt(             //비교대상1
                    JPAExpressions     //"서브 쿼리의 시작"
                            .select(idol.age.avg())   //비교대상2
                            .from(idol)
                            .innerJoin(idol.group,group)                                    //그룹쓰려면 이너조인해야함 ⭐️
                            .where(group.groupName.eq("르세라핌"))) //특정 조건
                    .and(idol.group.isNotNull())
            )
            .fetch();

    //when

    //then
    assertFalse(result.isEmpty());
    for (Idol i : result) {
        System.out.println("\nIdol: " + i.getIdolName()
                + ", Group: " + i.getGroup().getGroupName()
                + ", Age: " + i.getAge());
    }
}

    @Test
    @DisplayName("그룹별 가장 최근의 발매된 앨범 정보 조회")
    void subqueryTest2() {

        /*

            SELECT G.group_name, A.album_name, A.release_year
            FROM tbl_group G
            INNER JOIN tbl_album A
            ON G.group_id = A.group_id
            WHERE A.album_id IN (
                    SELECT S.album_id
                    FROM tbl_album S
                    WHERE S.group_id = A.group_id
                        AND (
                            SELECT MAX(release_year)
                            FROM tbl_album
                            WHERE S.group_id = A.group_id
                        )
            )

         */

        //given
        QAlbum albumA = new QAlbum("albumA");
        QAlbum albumS = new QAlbum("albumS");

        //when
        List<Tuple> result = factory
                .select(group.groupName, albumA.albumName, albumA.releaseYear)
                .from(group)
                .innerJoin(group.albums, albumA)
                .where(albumA.id.in(
                        JPAExpressions
                                .select(albumS.id)
                                .from(albumS)
                                .where(albumS.group.id.eq(albumA.group.id)
                                        .and(albumS.releaseYear.eq(
                                                JPAExpressions
                                                        .select(albumS.releaseYear.max())
                                                        .from(albumS)
                                                        .where(albumS.group.id.eq(albumA.group.id))
                                        ))
                                )

                ))
                .distinct()
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            String groupName = tuple.get(group.groupName);
            String albumName = tuple.get(albumA.albumName);
            Integer releaseYear = tuple.get(albumA.releaseYear);

            System.out.println("\nGroup: " + groupName
                    + ", Album: " + albumName
                    + ", Release Year: " + releaseYear);
        }

    }


    @Test
    @DisplayName("특정 연도에 발매된 앨범 수가 2개 이상인 그룹 조회")
    void testFindGroupsWithMultipleAlbumsInYear() {
        int targetYear = 2022;

        QAlbum subAlbum = new QAlbum("subAlbum");

        // 서브쿼리: 각 그룹별로 특정 연도에 발매된 앨범 수를 계산
        // 서브쿼리를 변수로 추출함~!⭐️ : 가독성 올리는 목적
        JPQLQuery<Long> subQuery = JPAExpressions
                .select(subAlbum.group.id)
                .from(subAlbum)
                .where(subAlbum.releaseYear.eq(targetYear))
                .groupBy(subAlbum.group.id)
                .having(subAlbum.count().goe(2L));

        // 메인쿼리: 서브쿼리의 결과와 일치하는 그룹 조회
        List<Group> result = factory
                .selectFrom(group)
                .where(group.id.in(subQuery))
                .fetch();

        assertFalse(result.isEmpty());
        for (Group g : result) {
            System.out.println("\nGroup: " + g.getGroupName());
        }
    }

    @Test
    @DisplayName("그룹이 존재하지 않는 아이돌 조회")
    void testFindIdolsWithoutGroup() {


        // 서브쿼리: 아이돌이 특정 그룹에 속하는지 확인
        JPQLQuery<Long> subQuery = JPAExpressions
                .select(group.id)
                .from(group)
                .where(group.id.eq(idol.group.id));

        // 메인쿼리: 서브쿼리 결과가 존재하지 않는 아이돌 조회
        List<Idol> result = factory
                .selectFrom(idol)
                .where(subQuery.notExists())
                .fetch();

        assertFalse(result.isEmpty());
        for (Idol i : result) {
            System.out.println("\nIdol: " + i.getIdolName());
        }
    }

    @Test
    @DisplayName("아이브의 평균 나이보다 나이가 많은 여자 아이돌 조회")
    void testFindMaleIdolsOlderThanGroupAverageAge() {
        String groupName = "아이브";

        JPQLQuery<Double> subQuery = JPAExpressions
                .select(idol.age.avg())   //평균 나이
                .from(idol)
                .where(idol.group.groupName.eq(groupName));

        List<Idol> result = factory
                .selectFrom(idol)
                .where(idol.age.gt(subQuery).and(idol.gender.eq("여")))
                .fetch();

        assertFalse(result.isEmpty());
        for (Idol i : result) {
            System.out.println("\nIdol: " + i.getIdolName() + ", Age: " + i.getAge());
        }
    }


    @Test
    @DisplayName("특정 연도에 발매된 앨범이 없는 그룹 조회")
    void testFindGroupsWithoutAlbumsInYear() {
        int targetYear = 2023;

        JPQLQuery<Long> subQuery = JPAExpressions
                .select(album.group.id)
                .from(album)
                .where(album.releaseYear.eq(targetYear));

        List<Group> result = factory
                .selectFrom(group)
                .where(group.id.notIn(subQuery))   //없는 것을 걸러냄
                .fetch();

        assertFalse(result.isEmpty());
        for (Group g : result) {
            System.out.println("Group: " + g.getGroupName());
        }
    }

} //end