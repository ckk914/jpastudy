package com.spring.jpastudy.chap02.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_student")
public class Student {
    //랜덤 문자로 pk 지정
    @Id
    @Column(name = "stu_id")
    @GeneratedValue(generator = "abc")
    @GenericGenerator(strategy = "uuid", name="abc")
    private String id;

    @Column(name = "stu_name", nullable = false)
    private String name;

    private String city;

    private String major;


}
