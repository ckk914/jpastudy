package com.spring.jpastudy.chap05.repository;

import com.spring.jpastudy.chap05.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods,Long> {
}
