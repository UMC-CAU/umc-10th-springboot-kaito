package com.example.umc10th_kaito.domain.user.repository;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findBySocialTypeAndSocialUid(SocialType socialType, String socialUid); // security/service/CustomOAuthService 에서 호출
}
