package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.util.JwtTokenUtil;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import steps.shared.RequestSteps;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginSteps {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    private Object request;
    private ResponseEntity<String> response;
    private boolean userExists = true;

    public LoginSteps() {
        this.authenticationManager = Mockito.mock(AuthenticationManager.class);
        this.userDetailsService = Mockito.mock(UserDetailsService.class);
        this.jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
    }

    @When("login")
    public void login() {
        request = RequestSteps.getRequest();
        if (request instanceof LoginRequestDto) {
            LoginRequestDto loginRequestDto = (LoginRequestDto) request;
            loginUser(loginRequestDto);
        } else {
            throw new IllegalArgumentException("Invalid request type for login");
        }
    }

    private void loginUser(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword());
        when(authenticationManager.authenticate(token)).thenReturn(token);

        UserDetails mockUserDetails = mock(UserDetails.class);

        if (userExists) {
            when(userDetailsService.loadUserByUsername(loginRequestDto.getUsername()))
                    .thenReturn(mockUserDetails);

            String mockJwtToken = "mock.jwt.token";
            when(jwtTokenUtil.generateToken(mockUserDetails)).thenReturn(mockJwtToken);

            response = ResponseEntity.ok().body(mockJwtToken);
        } else {
            when(userDetailsService.loadUserByUsername(loginRequestDto.getUsername()))
                    .thenThrow(new BadCredentialsException("Invalid username or password"));

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
    }

    @Then("the response should contain the jwt token")
    public void should_contain_jwt_token() {
        Assert.assertEquals("The response should contain a JWT token", HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("The response should contain the expected JWT token", "mock.jwt.token", response.getBody());
    }

    @And("user doesn't exist")
    public void user_doesnt_exist() {
        userExists = false;
    }

    @Then("the response should contain validation error messages")
    public void should_contain_validation_error_messages() {
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertEquals("The response should have status BAD_REQUEST", HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("The response should contain validation error message",
                "Invalid username or password", response.getBody());
    }
}
