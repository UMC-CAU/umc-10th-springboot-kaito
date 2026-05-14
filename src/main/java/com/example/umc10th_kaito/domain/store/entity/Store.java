package com.example.umc10th_kaito.domain.store.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "FLOAT DEFAULT 0")
    private Float score;
}
