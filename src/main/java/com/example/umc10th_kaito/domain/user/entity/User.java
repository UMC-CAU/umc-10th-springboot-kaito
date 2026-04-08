package com.example.umc10th_kaito.domain.user.entity;

import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.user.enums.Gender;
import com.example.umc10th_kaito.domain.user.enums.MemberStatus;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private SocialType socialType;

    @Column(nullable = false, length = 255)
    private String socialUid;

    @Column(length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Gender gender;

    private LocalDate birthRate;

    @Column(length = 255)
    private String address;

    @Column(length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean isPhoneVerified;

    @Column(nullable = false)
    private Integer totalPoint;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MemberStatus status;
}
