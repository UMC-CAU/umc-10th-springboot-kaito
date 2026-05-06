package com.example.umc10th_kaito.domain.mission.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import com.example.umc10th_kaito.domain.mission.enums.RewardType;
import com.example.umc10th_kaito.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission")
public class Mission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false, length = 10)
    @Builder.Default
    private RewardType rewardType = RewardType.POINT;
    @Column(name = "reward_value", nullable = false)
    private Integer rewardValue;
    @Column(nullable = false)
    private Integer deadline;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MissionActiveStatus status = MissionActiveStatus.ACTIVE;
}
