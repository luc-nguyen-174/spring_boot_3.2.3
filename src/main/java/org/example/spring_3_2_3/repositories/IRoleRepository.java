package org.example.spring_3_2_3.repositories;

import org.example.spring_3_2_3.domain.user.Role;
import org.example.spring_3_2_3.models.enums.RoleEnums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleEnums(RoleEnums roleEnums);
}