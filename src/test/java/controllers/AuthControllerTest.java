package controllers;

import org.example.controller.AuthController;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.exceptions.GlobalExceptionHandler;
import org.example.service.GymUserDetailsService;
import org.example.service.LoginAttemptService;
import org.example.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private GymUserDetailsService userDetailsService;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testLogin_Successful() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("testuser");
        loginRequestDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn("jwt-token");
        when(loginAttemptService.isBlocked("testuser")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));

        verify(loginAttemptService).loginSucceeded("testuser");
    }

    @Test
    void testLogin_UserBlocked() throws Exception {
        when(loginAttemptService.isBlocked("blockeduser")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"blockeduser\",\"password\":\"password\"}"))
                .andExpect(status().isLocked())
                .andExpect(content().string("User is blocked due to too many login attempts. Please try again later."));
    }

    @Test
    void testLogin_BadCredentials() throws Exception {
        when(loginAttemptService.isBlocked("testuser")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));

        verify(loginAttemptService).loginFailed("testuser");
    }

    @Test
    void testLogin_AccountDisabled() throws Exception {
        when(loginAttemptService.isBlocked("disableduser")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenThrow(new DisabledException("Account disabled"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"disableduser\",\"password\":\"password\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Account disabled"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully logged out"));
    }
}
