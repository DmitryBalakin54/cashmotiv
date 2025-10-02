package ru.cashmotiv.cashmotiv.util.AcessHasher;

import ru.cashmotiv.cashmotiv.domain.User;

import static ru.cashmotiv.cashmotiv.util.AcessHasher.AccessHasherLib.createByIdAndLoginAndCreationTime;

public class UserAccessHasher extends AccessHasherAbstract<User> {
    private static String SALT = "asd@@kjgshjGFVAJLH##FK454JGFGAD!!!JHFGjhg";

    public String create(User user) {
        if (user == null) {
            return null;
        }

        return createByIdAndLoginAndCreationTime(user.getId(), user.getLogin(), user.getCreationTime(), SALT);
    }
}
