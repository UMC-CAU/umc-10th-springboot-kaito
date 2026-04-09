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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private RewardType rewardType;

    @Column(nullable = false)
    private Integer rewardValue;

    @Column(nullable = false)
    private Integer deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MissionActiveStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

}
