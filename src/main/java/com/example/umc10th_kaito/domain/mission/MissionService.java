package com.example.umc10th_kaito.domain.mission;

import com.example.umc10th_kaito.domain.user.UserMission;
import com.example.umc10th_kaito.domain.user.UserMissionRepository;
import com.example.umc10th_kaito.global.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final UserMissionRepository userMissionRepository;

    /**
     * 내가 진행중인 미션 조회 - 오프셋 기반 페이지네이션
     * userId + status로 UserMission을 Page 조회 후 커스텀 DTO로 래핑
     */
    @Transactional(readOnly = true)
    public MissionResDTO.OffsetPageResponse<MissionResDTO.MissionItem> getMissions(
            Long userId, String status, int page, int size) {

        MissionStatus missionStatus;
        try {
            missionStatus = MissionStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ProjectException(MissionErrorCode.MISSION_NOT_FOUND);
        }

        Page<MissionResDTO.MissionItem> missionPage = userMissionRepository
                .findByUserIdAndStatus(userId, missionStatus, PageRequest.of(page, size))
                .map(MissionConverter::toMissionItem);

        return MissionConverter.toOffsetPageResponse(missionPage);
    }

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
