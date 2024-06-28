package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QIdol;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.*;

@Repository
@RequiredArgsConstructor
public class IdolRepositoryCustomImpl implements IdolCustomRepository {

    private final JdbcTemplate template;

    private final JPAQueryFactory factory;

    @Override
    public List<Idol> findAllSortedByName() {
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
    public List<Idol> findByGroupName() {
        return factory
                .select(idol)
                .from(idol)
                .orderBy(idol.group.groupName.asc())
                .fetch()
                ;
    }
}
