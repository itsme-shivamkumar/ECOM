package in.zeta.ecom.controller;

import in.zeta.ecom.entity.AuthSigninRequest;
import in.zeta.ecom.entity.AuthSigninResponse;
import in.zeta.ecom.entity.AuthSignupRequest;
import in.zeta.ecom.entity.AuthSignupResponse;
import in.zeta.ecom.exceptions.UserAlreadyExist;
import in.zeta.ecom.services.AuthService;
import in.zeta.ecom.services.OrderInfoService;
import in.zeta.ecom.services.ProductInfoServices;
import in.zeta.ecom.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class AuthController {
    private AuthService authService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthSignupResponse> addUser(@Valid @RequestBody AuthSignupRequest request) throws UserAlreadyExist, InvalidKeySpecException, NoSuchAlgorithmException {
        return ResponseEntity.ok(authService.signupUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSigninResponse> signInUser(@RequestHeader String authorization, @RequestBody AuthSigninRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return ResponseEntity.ok(authService.signinUser(request.getEmail(), request.getPassword(), request.getRole(), authorization));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> signOutUser(@RequestHeader String email, @RequestHeader String authorization) {
        return ResponseEntity.ok(authService.signOutUser(email, authorization));
    }
}
