package com.spring.jpastudy.chap05.repository;

import com.spring.jpastudy.chap05.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

}
