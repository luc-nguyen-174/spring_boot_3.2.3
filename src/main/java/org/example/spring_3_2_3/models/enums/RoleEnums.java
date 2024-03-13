package org.example.spring_3_2_3.models.enums;

import lombok.Getter;

@Getter
public enum RoleEnums {
    ROLE_USER(0, "ROLE_USER"),
    ROLE_ADMIN(1, "ROLE_ADMIN"),
    ROLE_MODERATOR(2, "ROLE_MODERATOR"),
    ROLE_SUPER_ADMIN(3, "ROLE_SUPER_ADMIN");

    private final int id;
    private final String name;

    RoleEnums(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleEnums getRoleByName(String name) {
        for (RoleEnums role : RoleEnums.values()) {
            if (role.name.equals(name)) {
                return role;
            }
        }
        return null;
    }
}
