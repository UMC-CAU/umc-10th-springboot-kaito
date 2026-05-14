package com.example.umc10th_kaito.domain.home.service;
import com.example.umc10th_kaito.domain.home.dto.HomeResDTO;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import com.example.umc10th_kaito.domain.mission.repository.MissionRepository;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    // 홈 화면 상단 현황 조회
    @Transactional(readOnly = true)
    public HomeResDTO.Summary getHomeSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
        long totalCount = userMissionRepository.countByUser_Id(userId);
        long completedCount = userMissionRepository.countByUser_IdAndStatus(userId, MissionStatus.COMPLETED);
        Integer rewardPoint = userMissionRepository.sumRewardByUserIdAndStatus(userId, MissionStatus.CHALLENGING);
        return HomeResDTO.Summary.builder()
                .completedCount((int) completedCount)
                .totalCount((int) totalCount)
                .rewardPoint(rewardPoint != null ? rewardPoint : 0)
                .currentPoint(user.getTotalPoint())
                .nickname(user.getName())
                .location(user.getAddress() != null ? user.getAddress() : "")
                .build();
    }
    // 홈 화면 하단 지역 미션 목록 (페이징)
    @Transactional(readOnly = true)
    public Page<HomeResDTO.MissionItem> getHomeMissions(Long areaId, int page, int size) {
        return missionRepository
                .findByAreaIdAndStatus(areaId, MissionActiveStatus.ACTIVE, PageRequest.of(page, size))
                .map(this::toMissionItem);
    }
    private HomeResDTO.MissionItem toMissionItem(Mission m) {
        return HomeResDTO.MissionItem.builder()
                .missionId(m.getId())
                .storeName(m.getStore().getName())
                .storeCategory(m.getStore().getCategory())
                .condition(m.getContent())
                .rewardPoint(m.getRewardValue())
                .rewardType(m.getRewardType().name())  // 추가
                .dDay(m.getDeadline())
                .status(m.getStatus().name())
                .build();
    }
}
