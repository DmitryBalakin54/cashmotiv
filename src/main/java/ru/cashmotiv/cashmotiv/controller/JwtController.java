package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.Admin;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.dto.AdminDto;
import ru.cashmotiv.cashmotiv.dto.JwtDto;
import ru.cashmotiv.cashmotiv.dto.UserDto;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.NotFoundEntityException;
import ru.cashmotiv.cashmotiv.exception.ValidationException;
import ru.cashmotiv.cashmotiv.form.AdminCredentials;
import ru.cashmotiv.cashmotiv.form.UserCredentials;
import ru.cashmotiv.cashmotiv.service.AdminService;
import ru.cashmotiv.cashmotiv.service.JwtService;
import ru.cashmotiv.cashmotiv.service.UserService;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {
    private final JwtService jwtService;
    private final UserService userService;
    private final AdminService adminService;

    public JwtController(JwtService jwtService, UserService userService, AdminService adminService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/create")
    public ResponseEntity<JwtDto> createJwt(
            @RequestBody @Valid UserCredentials userCredentials,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = userService.findByLoginOrEmailAndPassword(userCredentials.getLoginOrEmail(), userCredentials.getPassword());
        if (user == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        return ResponseEntity.ok(new JwtDto(jwtService.createToken(user)));
    }

    @GetMapping("/find")
    public ResponseEntity<UserDto> find(
            @RequestParam String jwt)
    {
        User user = jwtService.verifyUserToken(jwt);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/create/admin")
    public ResponseEntity<JwtDto> createAdminJwt(
            @RequestBody @Valid AdminCredentials adminCredentials,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Admin admin = adminService.findByLoginAndAccessToken(adminCredentials.getLogin(), adminCredentials.getAccessToken());
        if (admin == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        return ResponseEntity.ok(new JwtDto(jwtService.createToken(admin)));
    }

    @GetMapping("/find/admin")
    public ResponseEntity<AdminDto> findAdmin(
            @RequestParam String jwt)
    {
        Admin admin = jwtService.verifyAdminToken(jwt);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new AdminDto(admin));
    }
}