package org.example.spring_3_2_3.models.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spring_3_2_3.domain.user.UserInfo;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private Set<String> roles;

    public UserInfo toUserInfo() {
        return new UserInfo(username, password, email);
    }

}