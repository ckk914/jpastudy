package com.spring.jpastudy.chap06_querydsl.repository;


import com.spring.jpastudy.chap06_querydsl.entity.Idol;

import java.util.List;

//@Mapper  //이거 쓰고 다 처리하면 mybatis도 가능케할 수 있음./
public interface IdolCustomRepository {

    // 이름으로 오름차해서 전체 조회
    List<Idol> findAllSortedByName();

    // 그룹명으로 아이돌을 조회
    List<Idol> findByGroupName();

}
