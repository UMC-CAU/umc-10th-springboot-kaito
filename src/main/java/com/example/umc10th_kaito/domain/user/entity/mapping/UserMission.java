package com.example.umc10th_kaito.domain.user.entity.mapping;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_mission")
public class UserMission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MissionStatus status = MissionStatus.CHALLENGING;
    @Column(name = "verify_num", length = 10)
    private String verifyNum;
    public void updateStatus(MissionStatus status) {
        this.status = status;
    }
}
