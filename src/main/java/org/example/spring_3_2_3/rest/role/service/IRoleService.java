package org.example.spring_3_2_3.rest.role.service;

import org.example.spring_3_2_3.domain.user.Role;
import org.example.spring_3_2_3.models.enums.RoleEnums;
import org.example.spring_3_2_3.rest.IGenericService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface IRoleService extends IGenericService<Role> {
    Role findByName(RoleEnums name);

    Set<Role> getRoleByName(Set<String> name);
}