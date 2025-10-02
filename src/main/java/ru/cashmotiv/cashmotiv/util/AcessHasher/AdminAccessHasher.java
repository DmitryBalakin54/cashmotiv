package ru.cashmotiv.cashmotiv.util.AcessHasher;

import ru.cashmotiv.cashmotiv.domain.Admin;

import static ru.cashmotiv.cashmotiv.util.AcessHasher.AccessHasherLib.createByIdAndLoginAndCreationTime;

public class AdminAccessHasher extends AccessHasherAbstract<Admin> {
    private static String SALT = "kahf784kjfnHGJHH67^%($#kghjgjhUUU!";

    @Override
    public String create(Admin admin) {
        if (admin == null) {
            return null;
        }

        return createByIdAndLoginAndCreationTime(admin.getId(), admin.getLogin(), admin.getCreationTime(), SALT);
    }
}
