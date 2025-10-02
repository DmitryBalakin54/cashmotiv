package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.Promise;
import ru.cashmotiv.cashmotiv.dto.PromiseDto;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;
import ru.cashmotiv.cashmotiv.exception.ValidationException;
import ru.cashmotiv.cashmotiv.form.CreatePromiseForm;
import ru.cashmotiv.cashmotiv.form.PromiseImageForm;
import ru.cashmotiv.cashmotiv.form.PromisePageRequestForm;
import ru.cashmotiv.cashmotiv.form.PromiseSimpleForm;
import ru.cashmotiv.cashmotiv.service.PromiseService;
import ru.cashmotiv.cashmotiv.service.UserAccessHashService;
import ru.cashmotiv.cashmotiv.util.PromisePageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/promise")
public class PromiseController {

    private final PromiseService promiseService;
    private final UserAccessHashService userAccessHashService;

    public PromiseController(PromiseService promiseService, UserAccessHashService userAccessHashService) {
        this.promiseService = promiseService;
        this.userAccessHashService = userAccessHashService;
    }

    @PostMapping("/create")
    public ResponseEntity<PromiseDto> createPromise(
            @RequestBody @Valid CreatePromiseForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Promise promise = promiseService.createFromCreatePromiseForm(form);
        if (promise == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_PROMISE);
        }

        return ResponseEntity.ok(new PromiseDto(promise));
    }

    @PostMapping("/send/proof/notice")
    public ResponseEntity<PromiseDto> sendProofNotice(
            @RequestBody @Valid PromiseSimpleForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Promise promise = promiseService.proofNoticePromiseByIds(form.getPromiseId(), form.getUserId());
        if (promise == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_PROMISE);
        }

        return ResponseEntity.ok(new PromiseDto(promise));
    }

    @PostMapping("/close")
    public ResponseEntity<PromiseDto> closePromise(
            @RequestBody @Valid PromiseSimpleForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Promise promise = promiseService.closeByIds(form.getPromiseId(), form.getUserId());
        if (promise == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_PROMISE);
        }

        return ResponseEntity.ok(new PromiseDto(promise));
    }

    @PostMapping("/get")
    public ResponseEntity<PromiseDto> getPromise(
            @RequestBody @Valid PromiseSimpleForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Promise promise = promiseService.findByIdAndCheckUserId(form.getPromiseId(), form.getUserId());
        return ResponseEntity.ok(new PromiseDto(promise));
    }

    @PostMapping("/get/page")
    public ResponseEntity<Page<PromiseDto>> getPromises(
            @RequestBody @Valid PromisePageRequestForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(promiseService.getPage(new PromisePageRequest(form)));
    }

    @PostMapping("/send/proof/image")
    public ResponseEntity<PromiseDto> sendProofImage(
            @ModelAttribute @Valid PromiseImageForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Promise promise = promiseService.proofImagePromiseByIds(form.getPromiseId(), form.getUserId(), form.getImage());
        if (promise == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_PROMISE);
        }

        return ResponseEntity.ok(new PromiseDto(promise));
    }
}
