package ru.cashmotiv.cashmotiv.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.AccessException;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.util.AcessHasher.UserAccessHasher;

@Service
public class UserAccessHashService {

    private final UserService userService;
    private final UserAccessHasher userAccessHasher;

    public UserAccessHashService(UserService userService) {
        this.userService = userService;
        this.userAccessHasher = new UserAccessHasher();
    }

    @Transactional
    public void checkHashByUserId(String hash, Long userId) {
        System.out.println("----------hash----------");
        System.out.println(hash);
        System.out.println("------------------------");

        User user = userService.findById(userId);
        if (user == null) {
            throw new AccessException(ErrorCode.ACCESS_DENIED);
        }

        if (!userAccessHasher.verify(hash, user)) {
            throw new AccessException(ErrorCode.ACCESS_DENIED);
        }
    }

    public String createHash(User user) {
        return userAccessHasher.create(user);
    }
}
