package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.service.GymUserDetailsService;
import org.example.service.LoginAttemptService;
import org.example.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User", description = "API for user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final GymUserDetailsService userDetailsService;
    private final LoginAttemptService loginAttemptService;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          GymUserDetailsService userDetailsService,
                          LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.loginAttemptService = loginAttemptService;
    }

    @Operation(summary = "Login user", description = "Login a user using username and password", tags = {"User Login"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "423", description = "Locked"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        if (loginAttemptService.isBlocked(username)) {
            return new ResponseEntity<>("User is blocked due to too many login attempts. Please try again later.", HttpStatus.LOCKED);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwt = jwtTokenUtil.generateToken(userDetails);
            loginAttemptService.loginSucceeded(username);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (BadCredentialsException | DisabledException ex) {
            loginAttemptService.loginFailed(username);
            throw ex;
        }
    }


    @Operation(summary = "Logout user", description = "Logout a user using username and password", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }
}