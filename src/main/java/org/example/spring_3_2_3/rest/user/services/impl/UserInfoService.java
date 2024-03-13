package org.example.spring_3_2_3.rest.user.services.impl;

import org.example.spring_3_2_3.domain.user.Role;
import org.example.spring_3_2_3.domain.user.UserInfo;
import org.example.spring_3_2_3.models.dto.userDto.UserDTO;
import org.example.spring_3_2_3.models.enums.RoleEnums;
import org.example.spring_3_2_3.repositories.UserInfoRepository;
import org.example.spring_3_2_3.rest.role.service.IRoleService;
import org.example.spring_3_2_3.rest.user.services.IUserInfoService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements UserDetailsService, IUserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final IRoleService roleService;
    private final PasswordEncoder encoder;

    public UserInfoService(UserInfoRepository userInfoRepository, IRoleService roleService, PasswordEncoder encoder) {
        this.userInfoRepository = userInfoRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = userInfoRepository.findByUsername(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String addUser(UserDTO userDTO) {
        if (userInfoRepository.countByUsername(userDTO.getUsername()) != 0) {
            Optional<UserInfo> userInfoOptional = userInfoRepository.findByUsername(userDTO.getUsername());
            if (userInfoOptional.isPresent()
                    && userInfoOptional.get()
                    .getRoles()
                    .stream()
                    .map(Role::getRoleEnums)
                    .collect(Collectors.toSet())
                    .containsAll(userDTO.getRoles())) {
                return "User already exists";
            } else {
                //chinh sua lai code de khi dang ki, neu user da ton tai thi se them role moi cho user do
                //new role
                Set<String> roles = userDTO.getRoles();
                //get currentRole
                Set<Role> currentRole = userInfoOptional.get().getRoles();
                //add roles and currentRole to new Set
                currentRole.addAll(roles.stream()
                        .map(role -> {
                            RoleEnums roleEnums = RoleEnums.getRoleByName(role);
                            return roleService.findByName(roleEnums);
                        })
                        .collect(Collectors.toSet()));
                // add new role to user
                userInfoOptional.get().setRoles(currentRole);
                save(userInfoOptional.get());
                return "New role added to user";
            }
        } else if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
            return "Username or Password cannot be empty";
        } else if (userDTO.getPassword().length() < 8) {
            return "Password must be at least 8 characters";
        } else if (userDTO.getUsername().length() < 4) {
            return "Username must be at least 4 characters";
        } else if (userDTO.getUsername().length() > 20) {
            return "Username must be less than 20 characters";
        } else if (userDTO.getPassword().length() > 20) {
            return "Password must be less than 20 characters";
        } else if (userDTO.getUsername().contains(" ") || userDTO.getPassword().contains(" ")) {
            return "Username or Password cannot contain spaces";
        }
        UserInfo userInfo = userDTO.toUserInfo();
        Set<String> roles = userDTO.getRoles();
        Set<Role> roleSet = roleService.getRoleByName(roles);

        userInfo.setRoles(roleSet);
        userInfo.setEmail(userDTO.getEmail());
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        save(userInfo);
        return "User Added Successfully";
    }

    @Override
    public Iterable<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        return userInfoRepository.findById(id);
    }

    //not used
    @Override
    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    @Override
    public void remove(Long id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        userInfoRepository.addRoleToUser(userId, roleId);
    }
}