package com.spring.jpastudy.chap06_querydsl.dto;

import lombok.*;

@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupAverageAgeDto {

    private String groupName;
    private double averageAge;

}