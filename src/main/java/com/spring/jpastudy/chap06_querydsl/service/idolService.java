package com.spring.jpastudy.chap06_querydsl.service;

import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.repository.IdolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // jpa, 쿼리dsl 은  꼭 붙이자~! 안붙이면 안돌아감!⭐️
public class idolService {

    private final IdolRepository idolRepository;
//    아이돌을 나이 순으로 "내림차 정렬" 해서 조회
    public List<Idol> getIdols(){

//        List<Idol> idolList = idolRepository.findAllBySorted();  // 방법2
        List<Idol> idolList = idolRepository.findByGroupName();  // 방법3
            return idolList;
//        List<Idol> idolList = idolRepository.findAll();  //방법1
//        return idolList.stream()
//                .sorted(Comparator.comparing(Idol::getAge))
//                .collect(Collectors.toList());

    }
}
