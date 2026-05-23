package com.example.umc10th_kaito.domain.mission.service;
import com.example.umc10th_kaito.domain.mission.converter.MissionConverter;
import com.example.umc10th_kaito.domain.mission.dto.MissionResDTO;
import com.example.umc10th_kaito.domain.mission.enums.MissionErrorCode;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class MissionService {
    private final UserMissionRepository userMissionRepository;
    // 미션 목록 조회 (진행중 / 진행완료, 페이징)
    @Transactional(readOnly = true)
    public Page<MissionResDTO.MissionItem> getMissions(Long userId, String status, int page, int size) {
        MissionStatus missionStatus;
        try {
            missionStatus = MissionStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ProjectException(MissionErrorCode.MISSION_NOT_FOUND);
        }
        return userMissionRepository
                .findByUserIdAndStatus(userId, missionStatus, PageRequest.of(page, size))
                .map(MissionConverter::toMissionItem);
    }
    // 미션 성공 누르기
    @Transactional
    public MissionResDTO.SuccessResult completeMission(Long userMissionId) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new ProjectException(MissionErrorCode.USER_MISSION_NOT_FOUND));
        if (userMission.getStatus() == MissionStatus.COMPLETED) {
            throw new ProjectException(MissionErrorCode.MISSION_ALREADY_COMPLETED);
        }
        userMission.updateStatus(MissionStatus.PENDING_APPROVAL);
        return MissionConverter.toSuccessResult(userMission);
    }
}
