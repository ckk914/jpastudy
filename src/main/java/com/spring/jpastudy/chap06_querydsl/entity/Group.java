package com.spring.jpastudy.chap06_querydsl.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"idols","albums"})
@Table(name = "tbl_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Idol> idols = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums = new ArrayList<>();


    public Group(String groupName) {
        this.groupName = groupName;
    }

     //양방향
    public void addIdol(Idol idol) {
        idols.add(idol);
        idol.setGroup(this);
    }
    //양방향
    public void removeIdol(Idol idol) {
        idols.remove(idol);
        idol.setGroup(null);
    }
}
