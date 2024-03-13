package org.example.spring_3_2_3.rest.user.services;

import org.example.spring_3_2_3.domain.user.UserInfo;
import org.example.spring_3_2_3.rest.IGenericService;

public interface IUserInfoService extends IGenericService<UserInfo> {
    void addRoleToUser(Long userId, Long roleId);

}