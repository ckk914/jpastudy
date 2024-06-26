package com.spring.jpastudy.chap03_page;

import com.spring.jpastudy.chap02.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  StudentPageRepository extends JpaRepository<Student,String> {
    //전체 조회 상황에서 페이징 처리하기
    //Pageable은 data.domain 껄로 가져오기~!
    Page<Student> findAll(Pageable pageable);

    //검색 + 페이징
    Page<Student> findByNameContaining(String name, Pageable pageable);

//    @Query("")
//    Page<Student> getList();  //limit, orderby는 여기서 처리함~!

}
