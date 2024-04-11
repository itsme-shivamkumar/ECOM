package in.zeta.ecom.services;


import in.zeta.ecom.entity.*;
import in.zeta.ecom.exceptions.NotAuthorizedException;
import in.zeta.ecom.exceptions.UserAlreadyExist;
import in.zeta.ecom.exceptions.WrongCredentialException;
import in.zeta.ecom.repo.SessionRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.HashUtil;
import in.zeta.ecom.utils.JwtUtils;
import in.zeta.ecom.utils.RandomIdGenerator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Objects;


@Service
public class AuthService {

    private static final String ACTIVE = "ACTIVE";
    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;
    private final JwtUtils jwtUtil;
    private final RandomIdGenerator rig;
    private final HashUtil hashUtil;

    @Autowired
    public AuthService(UserRepo userRepo, SessionRepo sessionRepo, JwtUtils jwtUtil, RandomIdGenerator rig, HashUtil hashUtil) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.jwtUtil = jwtUtil;
        this.rig = rig;
        this.hashUtil = hashUtil;
    }


    public AuthSignupResponse signupUser(AuthSignupRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        request.setPassword(hashUtil.hashPassword(request.getPassword()));

        User user1 = userRepo.findUserByEmail(request.getEmail());
        User user2 = userRepo.findUserByPhoneNumber(request.getPhoneNumber());

        if (user1 == null && user2 == null) {
            String sessionId = rig.generateRandomId(12);
            String token = jwtUtil.generateToken(request.getEmail(), sessionId, request.getRole());
            String id = rig.generateRandomId(36);

            User newUser = new User();
            newUser.setId(id);
            newUser.setEmail(request.getEmail());
            newUser.setName(request.getName());
            newUser.setRole(request.getRole());
            newUser.setProfileStatus(request.getProfileStatus());
            newUser.setCreatedDate(new Date());
            newUser.setUpdatedDate(new Date());
            newUser.setPassword(request.getPassword());
            newUser.setPhoneNumber(request.getPhoneNumber());
            userRepo.save(newUser);

            Session newSession = new Session(sessionId, ACTIVE);
            sessionRepo.save(newSession);

            return new AuthSignupResponse("User successfully added!!", request.getEmail(), request.getName(), request.getRole(), request.getPhoneNumber(), token, sessionId);
        } else {
            throw new UserAlreadyExist();
        }
    }

    public String extractor(String token) {
        Claims claims;
        try {
            if (Boolean.TRUE.equals(jwtUtil.isTokenExpired(token))){
                return "";
            }
            claims = jwtUtil.getAllClaimsFromToken(token);
            return claims.get("sessionId").toString();
        } catch (Exception e1) {
            return "";
        }
    }

    public AuthSigninResponse signinUser(String email, String pass, String role, String authorization) throws InvalidKeySpecException, NoSuchAlgorithmException {
        pass = hashUtil.hashPassword(pass);
        String token = "";
        if (authorization.length() > 7) token = authorization.substring(7);
        AuthSigninResponse response = new AuthSigninResponse();
        response.setEmail(email);
        User currUser = userRepo.findUserByEmail(email);
        if (currUser == null) throw new WrongCredentialException("Can not find the user");
        currUser.setUpdatedDate(new Date());
        if (!currUser.getPassword().equals(pass)) {
            throw new WrongCredentialException("Wrong Credential");
        }

        response.setName(currUser.getName());
        response.setUserId(currUser.getId());
        response.setPhoneNum(currUser.getPhoneNumber());

        String sessionIdFromToken;

        sessionIdFromToken = extractor(token);
        Session preExistingSession = sessionRepo.findSessionById(sessionIdFromToken);
        if(preExistingSession == null){
            preExistingSession = new Session("", "INACTIVE");
        }
        if (!Objects.equals(preExistingSession.getId(), "")) {
            response.setSessionId(sessionIdFromToken);
            response.setMsg("User sign in successful!");
            response.setTokenId(token);
            if (preExistingSession.getStatus().equals(ACTIVE)) {
                return response;
            } else {
                sessionRepo.save(new Session(sessionIdFromToken, ACTIVE));
                return response;
            }
        } else {
            String sessionId = rig.generateRandomId(12);

            response.setSessionId(sessionId);

            String newToken = jwtUtil.generateToken(email, sessionId, role);

            response.setTokenId(newToken);

            Session newSession = new Session(sessionId, ACTIVE);
            sessionRepo.save(newSession);

            response.setMsg("User sign in successful!");

            return response;
        }

    }

    public String signOutUser(String email, String authorization) {
        String token = "";
        if (authorization.length() > 7) {
            token = authorization.substring(7);
        }
        try {
            boolean isValidToken = jwtUtil.validateToken(token, email);
            if (!isValidToken) {
                throw new NotAuthorizedException("token not valid");
            } else {
                Claims claims = jwtUtil.getAllClaimsFromToken(token);
                String sessionId = claims.get("sessionId").toString();
                sessionRepo.updateSessionStatus("INACTIVE", sessionId);
                return "successfully logged out!";
            }
        } catch (Exception e) {
            throw new NotAuthorizedException("not authorized");
        }
    }
}
