package org.example.spring_3_2_3.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring_3_2_3.models.enums.RoleEnums;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnums roleEnums;
}

