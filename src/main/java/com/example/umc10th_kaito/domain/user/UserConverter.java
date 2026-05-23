package com.example.umc10th_kaito.domain.user;

public class UserConverter {

    /**
     * 회원가입 요청 DTO → User 엔티티 변환
     * 비밀번호는 Service에서 BCrypt 암호화 후 전달받음
     */
    public static User toUser(UserReqDTO.Register request, String encodedPassword) {
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .gender(request.getGender() != null
                        ? Gender.valueOf(request.getGender())
                        : Gender.NONE)
                .birthRate(request.getBirthDate())
                .address(request.getAddress())
                .socialType(SocialType.LOCAL)
                .build();
    }

    public static UserResDTO.RegisterResult toRegisterResult(User user) {
        return UserResDTO.RegisterResult.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static UserResDTO.MyPageResult toMyPageResult(User user) {
        return UserResDTO.MyPageResult.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalPoint(user.getTotalPoint())
                .build();
    }
}
