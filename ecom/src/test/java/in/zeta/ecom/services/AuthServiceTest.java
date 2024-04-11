package in.zeta.ecom.services;

import in.zeta.ecom.entity.*;
import in.zeta.ecom.exceptions.NotAuthorizedException;
import in.zeta.ecom.exceptions.WrongCredentialException;
import in.zeta.ecom.repo.SessionRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.HashUtil;
import in.zeta.ecom.utils.JwtUtils;
import in.zeta.ecom.utils.RandomIdGenerator;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private SessionRepo sessionRepo;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HashUtil hashUtil;

    @Mock
    private RandomIdGenerator randomIdGenerator;

    @InjectMocks
    private AuthService authService;

    @Test
    void testSignupUser() throws InvalidKeySpecException, NoSuchAlgorithmException {
        AuthSignupRequest request = new AuthSignupRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");
        request.setRole("USER");
        request.setProfileStatus("ACTIVE");
        request.setPhoneNumber("1234567890");

        Mockito.when(userRepo.findUserByEmail(anyString())).thenReturn(null);
        Mockito.when(randomIdGenerator.generateRandomId(Mockito.anyInt())).thenReturn("randomId");
        Mockito.when(jwtUtils.generateToken(anyString(), anyString(), anyString())).thenReturn("token");

        AuthSignupResponse response = authService.signupUser(request);

        assertEquals("User successfully added!!", response.getMsg());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getName());
        assertEquals("USER", response.getRole());
        assertEquals("1234567890", response.getPhoneNumber());
        assertEquals("token", response.getToken());
        assertEquals("randomId", response.getSessionId());
    }

    @Test
    void shouldSigninUserSuccessfully() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String email = "test@example.com";
        String password = "password123";
        String role = "ADMIN";
        String authorizationToken = "Bearer mockToken";

        User mockUser = new User();
        mockUser.setEmail(email);

        String hashedPassword = "hashedPassword123";
        mockUser.setPassword(hashedPassword);

        Mockito.when(userRepo.findUserByEmail(email)).thenReturn(mockUser);
        Mockito.when(hashUtil.hashPassword(password)).thenReturn(hashedPassword);

        Claims mockClaims = Mockito.mock(Claims.class);
        Mockito.when(jwtUtils.getAllClaimsFromToken(anyString())).thenReturn(mockClaims);
        Mockito.when(mockClaims.get("sessionId")).thenReturn("mockSessionId");

        Session mockSession = new Session("mockSessionId", "ACTIVE");
        Mockito.when(sessionRepo.findById("mockSessionId")).thenReturn(Optional.of(mockSession));

        try {
            AuthSigninResponse response = authService.signinUser(email, password, role, authorizationToken);

            assertEquals(email, response.getEmail());
            assertEquals(mockUser.getName(), response.getName());
            assertEquals(mockUser.getId(), response.getUserId());
            assertEquals(mockUser.getPhoneNumber(), response.getPhoneNum());
            assertEquals("mockSessionId", response.getSessionId());
            assertEquals("User sign in successful!", response.getMsg());
            assertEquals("mockToken", response.getTokenId());
        } catch (Exception e) {
            System.out.println("Exception Message: " + e.getMessage());
            throw e; // Rethrow the exception to fail the test
        }
    }


    @Test
    void shouldHandleWrongCredentialsException() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String email = "test@example.com";
        String password = "invalidPassword";
        String role = "ADMIN";
        String authorizationToken = "Bearer mockToken";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("correctPassword"); // Incorrect password intentionally

        Mockito.when(userRepo.findUserByEmail(email)).thenReturn(mockUser);
        Mockito.when(hashUtil.hashPassword(password)).thenReturn("hashedIncorrectPassword");

        assertThrows(WrongCredentialException.class,
                () -> authService.signinUser(email, password, role, authorizationToken),
                "Should throw WrongCredentialException for incorrect password");
    }

    @Test
    void shouldHandleExceptionWhenUserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password123";
        String role = "ADMIN";
        String authorizationToken = "Bearer mockToken";

        Mockito.when(userRepo.findUserByEmail(email)).thenThrow(new WrongCredentialException("User not found"));

        assertThrows(WrongCredentialException.class,
                () -> authService.signinUser(email, password, role, authorizationToken),
                "Should throw WrongCredentialException for nonexistent user");
    }

    @Test
    void shouldSignOutUserSuccessfully() {
        String email = "test@example.com";
        String validToken = "validToken";
        String authorizationToken = "Bearer " + validToken;

        Claims mockClaims = Mockito.mock(Claims.class);
        Mockito.when(jwtUtils.validateToken(validToken, email)).thenReturn(true);
        Mockito.when(jwtUtils.getAllClaimsFromToken(validToken)).thenReturn(mockClaims);
        Mockito.when(mockClaims.get("sessionId")).thenReturn("mockSessionId");

        Session mockSession = new Session("mockSessionId", "ACTIVE");
        Mockito.when(sessionRepo.findById("mockSessionId")).thenReturn(Optional.of(mockSession));

        String result = authService.signOutUser(email, authorizationToken);

        assertEquals("successfully logged out!", result);
        Mockito.verify(sessionRepo, Mockito.times(1)).updateSessionStatus("INACTIVE", "mockSessionId");
    }

    @Test
    void shouldHandleNotAuthorizedExceptionForInvalidToken() {
        String email = "test@example.com";
        String invalidToken = "invalidToken";
        String authorizationToken = "Bearer " + invalidToken;

        Mockito.when(jwtUtils.validateToken(invalidToken, email)).thenReturn(false);

        assertThrows(NotAuthorizedException.class,
                () -> authService.signOutUser(email, authorizationToken),
                "Should throw NotAuthorizedException for invalid token during sign out");
    }

    @Test
    void shouldHandleNotAuthorizedExceptionForTokenValidationFailure() {
        String email = "test@example.com";
        String validToken = "validToken";
        String authorizationToken = "Bearer " + validToken;

        Mockito.when(jwtUtils.validateToken(validToken, email)).thenThrow(new RuntimeException("Token validation error"));

        assertThrows(NotAuthorizedException.class,
                () -> authService.signOutUser(email, authorizationToken),
                "Should throw NotAuthorizedException for token validation error during sign out");
    }

}
