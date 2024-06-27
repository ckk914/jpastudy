package com.spring.jpastudy.chap06_querydsl.repository;

import com.spring.jpastudy.chap06_querydsl.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public interface GroupRepository extends JpaRepository<Group,Long> {

}
