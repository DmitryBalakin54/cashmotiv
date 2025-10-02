package ru.cashmotiv.cashmotiv.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.transaction.annotation.Transactional;import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Admin;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.JWTException;


@Service
public class JwtService {
    private final UserService userService;

    private static final String SECRET = "3f7a9b2e4c8d1e5f6a0b9c8d7e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private final UserAccessHashService userAccessHashService;
    private final AdminAccessHashService adminAccessHashService;
    private final AdminService adminService;

    public JwtService(UserService userService, UserAccessHashService userAccessHashService, AdminAccessHashService adminAccessHashService, AdminService adminService) {
        this.userService = userService;
        this.userAccessHashService = userAccessHashService;
        this.adminAccessHashService = adminAccessHashService;
        this.adminService = adminService;
    }

    public String createToken(User user) {
        try {
            String hash = userAccessHashService.createHash(user);
            if (hash == null) {
                throw new JWTException(ErrorCode.CANT_CREATE_JWT);
            }

            return JWT.create()
                    .withClaim("userId", user.getId())
                    .withClaim("hash", hash)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTException(ErrorCode.CANT_CREATE_JWT);
        }
    }

    @Transactional
    public User verifyUserToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            var jwt = verifier.verify(token);
            Long userId = jwt.getClaim("userId").asLong();
            userAccessHashService.checkHashByUserId(jwt.getClaim("hash").asString(), userId);

            return userService.findById(userId);
        } catch (JWTVerificationException e) {
            throw new JWTException(ErrorCode.CANT_FIND_JWT);
        }
    }

    @Transactional
    public String createToken(Admin admin) {
        try {
            String hash = adminAccessHashService.createHash(admin);
            if (hash == null) {
                throw new JWTException(ErrorCode.CANT_CREATE_JWT);
            }

            return JWT.create()
                    .withClaim("adminId", admin.getId())
                    .withClaim("hash", hash)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTException(ErrorCode.CANT_CREATE_JWT);
        }
    }

    @Transactional
    public Admin verifyAdminToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            var jwt = verifier.verify(token);
            Long adminId = jwt.getClaim("adminId").asLong();
            adminAccessHashService.checkHashByAdminId(jwt.getClaim("hash").asString(), adminId);

            return adminService.findById(adminId);
        } catch (JWTVerificationException e) {
            throw new JWTException(ErrorCode.CANT_FIND_JWT);
        }
    }
}