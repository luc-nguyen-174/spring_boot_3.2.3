package org.example.spring_3_2_3.rest.role.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.spring_3_2_3.domain.user.Role;
import org.example.spring_3_2_3.models.enums.RoleEnums;
import org.example.spring_3_2_3.repositories.IRoleRepository;
import org.example.spring_3_2_3.rest.role.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(RoleEnums name) {
        return roleRepository.findByRoleEnums(name);
    }

    @Override
    public Set<Role> getRoleByName(Set<String> roleName) {
        Set<Role> roles = new HashSet<>();
        for (String role : roleName) {
            RoleEnums roleEnums = RoleEnums.getRoleByName(role);
            Optional<Role> roleOptional = Optional.ofNullable(findByName(roleEnums));
            roleOptional.ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void remove(Long id) {
        roleRepository.deleteById(id);
    }

    @PostConstruct
    public void init() {
        for (RoleEnums roleEnums : RoleEnums.values()) {
            Role role = new Role();
            //check if role is already exist
            if (roleRepository.findByRoleEnums(roleEnums) != null) {
                continue;
            }
            role.setRoleEnums(roleEnums);
            save(role);
        }
    }
}