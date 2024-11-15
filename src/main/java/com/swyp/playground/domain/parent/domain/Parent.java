package com.swyp.playground.domain.parent.domain;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "ID", nullable = false, length = 20, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 20)
    private String password;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 11, unique = true)
    private String phoneNumber;

    @Column(name = "NICKNAME", nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(name = "ADDRESS", nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private String birthDay;

    @Column(name = "CHILD_COUNT")
    private Integer childCount = 0;

    @Column(name = "MANNER_TEMP")
    private Integer mannerTemp = 50;

    @Column(name = "INTEREST", length = 255)
    private String interest;

    @Column(name = "PROFILE_IMG")
    private String profileImg = "default_profile_image.jpg"; // 기본 프로필 이미지 파일 이름

    @Column(name = "JOINED_AT")
    private LocalDate joinedAt = LocalDate.now();

    // Getters, Setters, Constructor 생략
}
