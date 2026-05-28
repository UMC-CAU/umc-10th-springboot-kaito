package com.example.umc10th_kaito.domain.user.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.Gender;
import com.example.umc10th_kaito.domain.user.enums.MemberStatus;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, length = 20)
    @Builder.Default
    private SocialType socialType = SocialType.LOCAL;

    @Column(name = "social_uid", unique = true)
    private String socialUid;

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Gender gender = Gender.NONE;

    @Column(name = "birth_rate")
    private LocalDate birthRate;

    @Column(length = 255)
    private String address;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "is_phone_verified", nullable = false)
    @Builder.Default
    private Boolean isPhoneVerified = false;

    @Column(name = "total_point", nullable = false)
    @Builder.Default
    private Integer totalPoint = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberStatus status = MemberStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserMission> userMissionList = new ArrayList<>();
}
