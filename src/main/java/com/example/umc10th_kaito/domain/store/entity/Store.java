package com.example.umc10th_kaito.domain.store.entity;

import com.example.umc10th_kaito.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder

public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 255)
    private String address;

    private Float score;

    // 연관관계 매핑. 가게(Many)는 지역(One)에 속한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

}
