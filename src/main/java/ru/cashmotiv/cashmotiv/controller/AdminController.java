package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.Admin;
import ru.cashmotiv.cashmotiv.dto.AdminDto;
import ru.cashmotiv.cashmotiv.exception.ValidationException;
import ru.cashmotiv.cashmotiv.form.AdminCreateForm;
import ru.cashmotiv.cashmotiv.form.GetAdminForm;
import ru.cashmotiv.cashmotiv.form.GetAdminPageForm;
import ru.cashmotiv.cashmotiv.form.SetAdminActiveForm;
import ru.cashmotiv.cashmotiv.service.AdminAccessHashService;
import ru.cashmotiv.cashmotiv.service.AdminService;
import ru.cashmotiv.cashmotiv.util.AdminPageRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminAccessHashService adminAccessHashService;
    private final AdminService adminService;

    public AdminController(AdminAccessHashService adminAccessHashService, AdminService adminService) {
        this.adminAccessHashService = adminAccessHashService;
        this.adminService = adminService;
    }

    @PostMapping("/create")
    public ResponseEntity<AdminDto> createAdmin(
            @RequestBody @Valid AdminCreateForm form,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Admin admin = adminService.create(form.getLogin());
        if (admin == null) {
            // TODO
            return null;
        }

        return ResponseEntity.ok(new AdminDto(admin));
    }

    @PostMapping("/set/active")
    public ResponseEntity<AdminDto> setActiveAdmin(
            @RequestBody @Valid SetAdminActiveForm form,
            BindingResult bindingResult)
    {
        adminAccessHashService.checkHashByAdminId(form.getHash(), form.getAdminId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Admin admin = adminService.setActive(form.getTargetAdminId(), form.getActiveStatus());
        if (admin == null) {
            // TODO
            return null;
        }

        return ResponseEntity.ok(new AdminDto(admin));
    }

    @PostMapping("/get/admin/")
    public ResponseEntity<AdminDto> getAdmin(
            @RequestBody @Valid GetAdminForm form,
            BindingResult bindingResult)
    {
        adminAccessHashService.checkHashByAdminId(form.getHash(), form.getAdminId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Admin admin = adminService.findById(form.getTargetAdminId());
        if (admin == null) {
            // TODO
            return null;
        }

        return ResponseEntity.ok(new AdminDto(admin));
    }

    @PostMapping("/get/admin/page")
    public ResponseEntity<Page<AdminDto>> getAdminPage(
            @RequestBody @Valid GetAdminPageForm form,
            BindingResult bindingResult)
    {
        adminAccessHashService.checkHashByAdminId(form.getHash(), form.getAdminId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(adminService.getPage(new AdminPageRequest(form)));
    }
}
