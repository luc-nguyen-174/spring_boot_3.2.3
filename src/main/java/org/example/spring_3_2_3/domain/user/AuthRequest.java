package org.example.spring_3_2_3.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
  
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
  
    private String username;
    private String password;
  
}