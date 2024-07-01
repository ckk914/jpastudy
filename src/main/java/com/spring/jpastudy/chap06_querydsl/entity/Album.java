package com.spring.jpastudy.chap06_querydsl.entity;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = "group")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "tbl_album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    private String albumName;
    private int releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;


    public Album(String albumName, int releaseYear, Group group) {
        this.albumName = albumName;
        this.releaseYear = releaseYear;
        this.group = group;
    }
}
