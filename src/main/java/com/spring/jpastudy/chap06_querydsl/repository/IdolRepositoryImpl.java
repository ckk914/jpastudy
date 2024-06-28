package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.*;

@Repository
@RequiredArgsConstructor
public class IdolRepositoryImpl implements IdolCustomRepository {

    private final JdbcTemplate template;

    private final JPAQueryFactory factory;

    @Override
    public Page<Idol> foundAllByPaging(Pageable pageable) {

        // 페이징을 통한 조회
        List<Idol> idolList = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 조회건수
        Long totalCount = Optional.ofNullable(
                factory
                        .select(idol.count())
                        .from(idol)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(idolList, pageable, totalCount);
    }

    @Override
    public List<Idol> foundAllName2() {
        String sql = "SELECT * FROM tbl_idol ORDER BY idol_name ASC";
        return template.query(sql, (rs, n) -> {

            String idolName = rs.getString("idol_name");
            int age = rs.getInt("age");

            return new Idol(
                    idolName,
                    age,
                    null
            );
        });
    }

    @Override
    public List<Idol> foundByGroupName() {
        return factory
                .select(idol)
                .from(idol)
                .orderBy(idol.group.groupName.asc())
                .fetch()
                ;
    }
}