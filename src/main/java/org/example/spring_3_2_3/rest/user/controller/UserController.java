package org.example.spring_3_2_3.rest.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.spring_3_2_3.config.auth.JwtService;
import org.example.spring_3_2_3.domain.user.AuthRequest;
import org.example.spring_3_2_3.domain.user.UserInfo;
import org.example.spring_3_2_3.models.dto.userDto.UserDTO;
import org.example.spring_3_2_3.rest.user.services.impl.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tutorial", description = "Tutorial management APIs")
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService userInfoService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserInfoService userInfoService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(
            summary = "Retrieve a Tutorial by Id",
            description = "Get a Tutorial object by specifying its id. The response is Tutorial object with id, title, description and published status.",
            tags = {"tutorials", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    /*----------------------  add new user  ---------------------*/
    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserDTO userDTO) {
        return userInfoService.addUser(userDTO);
    }

    /*----------------------  user profile  ---------------------*/
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    /*----------------------  admin profile  ---------------------*/
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }


    @GetMapping("/get-all-user")
    public ResponseEntity<Iterable<UserInfo>> getAllUser() {
        Iterable<UserInfo> userInfos = userInfoService.findAll();
        return new ResponseEntity<>(userInfos, HttpStatus.OK);
    }

    /*----------------------gen token---------------------*/
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}