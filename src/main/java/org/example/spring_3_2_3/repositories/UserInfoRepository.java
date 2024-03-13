package org.example.spring_3_2_3.repositories;

import jakarta.transaction.Transactional;
import org.example.spring_3_2_3.domain.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);

    @Query(value = " select * from user_info ui join security.user_role ur on ui.id = ur.user_id " +
            "where ui.username = :username " +
            "and ur.role_id = (select id from role where name = :role)",
            nativeQuery = true)
    Optional<UserInfo> findByUsernameAndRolesName(String username, String role);

    @Query(value = "select count(u.id) from security.user_info u where u.username = :username", nativeQuery = true)
    int countByUsername(String username);

    @Query(value = "insert into user_role (user_id, role_id) values (:userId, :roleId)",
            nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

}