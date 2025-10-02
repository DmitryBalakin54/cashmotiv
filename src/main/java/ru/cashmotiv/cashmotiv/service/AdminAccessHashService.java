package ru.cashmotiv.cashmotiv.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cashmotiv.cashmotiv.domain.Admin;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.AccessException;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.util.AcessHasher.AdminAccessHasher;

@Service
public class AdminAccessHashService {

    private final AdminService adminService;
    private final AdminAccessHasher adminAccessHasher;

    public AdminAccessHashService(AdminService adminService) {
        this.adminService = adminService;
        this.adminAccessHasher = new AdminAccessHasher();
    }

    @Transactional
    public void checkHashByAdminId(String hash, Long adminId) {
        Admin admin = adminService.findById(adminId);
        if (admin == null) {
            throw new AccessException(ErrorCode.ACCESS_DENIED);
        }

        if (!adminAccessHasher.verify(hash, admin)) {
            throw new AccessException(ErrorCode.ACCESS_DENIED);
        }
    }

    public String createHash(Admin admin) {
        return adminAccessHasher.create(admin);
    }
}
