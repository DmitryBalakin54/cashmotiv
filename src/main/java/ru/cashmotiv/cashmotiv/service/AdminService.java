package ru.cashmotiv.cashmotiv.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Admin;
import ru.cashmotiv.cashmotiv.dto.AdminDto;
import ru.cashmotiv.cashmotiv.repository.AdminRepository;
import ru.cashmotiv.cashmotiv.util.AdminPageRequest;

import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Admin create(String login) {
        Admin admin = new Admin();
        admin.setLogin(login);
        admin.setAccessToken(UUID.randomUUID().toString());

        return save(admin);
    }

    private Admin save(Admin admin) {
        if (admin == null) {
            return null;
        }

        return adminRepository.save(admin);
    }

    public Admin findById(Long adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    public Admin findByLoginAndAccessToken(String login, String accessToken) {
        Admin admin = adminRepository.findByLogin(login);
        if (admin == null) {
            //TODO
            return null;
        }

        if (!admin.getAccessToken().equals(accessToken)) {
            // TODO
            return null;
        }

        return admin;
    }

    @Transactional
    public Admin setActive(Long targetAdminId, Boolean activeStatus) {
        Admin admin = findById(targetAdminId);
        if (admin == null) {
            // TODO
            return null;
        }

        admin.setIsActive(activeStatus);

        return adminRepository.save(admin);
    }

    @Transactional
    public Page<AdminDto> getPage(AdminPageRequest adminPageRequest) {
        return adminRepository.findAdminsByFilters(
                adminPageRequest.getIsActiveEnabled(),
                adminPageRequest.getNotIsActiveEnabled(),
                adminPageRequest.getSort(),
                PageRequest.of(adminPageRequest.getPageNum(), adminPageRequest.getPageSize())
        ).map(AdminDto::new);
    }
}
