package ru.cashmotiv.cashmotiv.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.Promise;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.dto.PromiseDto;
import ru.cashmotiv.cashmotiv.exception.*;
import ru.cashmotiv.cashmotiv.form.CreatePromiseForm;
import ru.cashmotiv.cashmotiv.repository.PromiseRepository;
import ru.cashmotiv.cashmotiv.util.PromisePageRequest;

import java.text.ParseException;
import java.util.Date;

@Service
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final PromiseImageService promiseImageService;

    public PromiseService(PromiseRepository promiseRepository, UserService userService, EmailSenderService emailSenderService, PromiseImageService promiseImageService) {
        this.promiseRepository = promiseRepository;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.promiseImageService = promiseImageService;
    }

    @Transactional
    public Promise createFromCreatePromiseForm(CreatePromiseForm form) {
        Promise promise = new Promise();
        promise.setTitle(form.getTitle());
        promise.setDescription(form.getDescription());
        promise.setType(Promise.PromiseType.valueOf(form.getType()));
        promise.setStatus(Promise.Status.IN_PROGRESS);

        try {
            promise.setExpiryDate(Promise.parseDate(form.getDeadline()));
            if (form.getNotificationTime() != null) {
                promise.setNotificationDate(Promise.parseDate(form.getNotificationTime()));
            }
        } catch (ParseException e) {
            throw new IncorrectDate(ErrorCode.INCORRECT_DATE_FORMAT);
        }

        if (promise.getExpiryDate().before(new Date())) {
            throw new IncorrectDate(ErrorCode.INCORRECT_DATE_IN_PAST);
        }

        if (promise.getExpiryDate().getTime() - new Date().getTime() <= 60 * 60 * 1000) {
            throw new IncorrectDate(ErrorCode.INCORRECT_DATE_PROMISE_BOUND);
        }

        if (promise.getNotificationDate() != null) {
            if (promise.getNotificationDate().after(promise.getExpiryDate())) {
                throw new IncorrectDate(ErrorCode.INCORRECT_NOTIFICATION_DATE_AFTER_EXPIRE_DATE);
            }

            if (promise.getExpiryDate().getTime() - promise.getNotificationDate().getTime() <= 60 * 15 * 1000) {
                throw new IncorrectDate(ErrorCode.INCORRECT_NOTIFICATION_DATE_TOO_LITTLE_TIME);
            }

            if (promise.getNotificationDate().before(new Date())) {
                throw new IncorrectDate(ErrorCode.INCORRECT_NOTIFICATION_DATE_BEFORE_CREATE);
            }
        }

        User user = userService.findById(form.getUserId());
        if (user == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        promise.setUser(user);
        promise.setNotification(form.getNotification() && user.getNotificationsEnabled());
        return promiseRepository.save(promise);
    }

    public Promise save(Promise promise) {
        return promiseRepository.save(promise);
    }

    public Promise findById(Long promiseId) {
        return promiseRepository.findById(promiseId).orElse(null);
    }

    @Transactional
    public Promise findByIdAndCheckUserId(Long promiseId, Long userId) {
        Promise promise = findById(promiseId);
        if (promise == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_PROMISE);
        }

        if (!promise.getUser().getId().equals(userId)) {
            throw new PromiseActionException(ErrorCode.INCORRECT_PROMISE_ID);
        }

        return promise;
    }

    @Transactional
    public Promise findByIdAndCheckUserIdAndCheckNotClosed(Long promiseId, Long userId) {
        Promise promise = findByIdAndCheckUserId(promiseId, userId);

        if (promise.getClosed()) {
            throw new PromiseActionException(ErrorCode.PROMISE_IS_ALREADY_CLOSE);
        }

        return promise;
    }

    @Transactional
    public Promise findByIdAndCheckUserIdAndCheckNotClosedAndCheckNotExpired(Long promiseId, Long userId) {
        Promise promise = findByIdAndCheckUserIdAndCheckNotClosed(promiseId, userId);

        if (promise.getExpiryDate().before(new Date())) {
            throw new IncorrectDate(ErrorCode.INCORRECT_DATE_IN_PAST);
        }

        return promise;
    }

    @Transactional
    public Promise proofNoticePromiseByIds(Long promiseId, Long userId) {
        Promise promise = findByIdAndCheckUserIdAndCheckNotClosedAndCheckNotExpired(promiseId, userId);

        Promise savedIfExpired = checkPromiseIsValid(Promise.PromiseType.NOTICE, promise);
        if (savedIfExpired != null) {
            return savedIfExpired;
        }

        promise.setStatus(Promise.Status.VERIFIED);
        promise.setFulfilled(true);
        promise.setClosed(true);
        return save(promise);
    }

    @Transactional
    public Promise proofImagePromiseByIds(Long promiseId, Long userId, MultipartFile image) {
        Promise promise = findByIdAndCheckUserIdAndCheckNotClosedAndCheckNotExpired(promiseId, userId);

        Promise savedIfExpired = checkPromiseIsValid(Promise.PromiseType.IMAGE, promise);
        if (savedIfExpired != null) {
            return savedIfExpired;
        }

        String path = promiseImageService.saveImage(image, promiseId);
        if (path == null) {
            throw new SystemException(ErrorCode.FILES_ERROR);
        }

        promise.setProof(path);
        promise.setStatus(Promise.Status.UNDER_REVIEW);
        promise.setClosed(true);
        return save(promise);
    }

    private Promise checkPromiseIsValid(Promise.PromiseType type, Promise promise) {
        if (!promise.getType().equals(type)) {
            throw new PromiseActionException(ErrorCode.INCORRECT_PROMISE_TYPE);
        }

        if (!promise.getStatus().equals(Promise.Status.IN_PROGRESS)) {
            throw new PromiseActionException(ErrorCode.PROMISE_NOT_IN_PROGRESS);
        }

        if (promise.isExpired()) {
            promise.setStatus(Promise.Status.FAILED);
            promise.setClosed(true);
            return save(promise);
        }

        return null;
    }

    @Transactional
    public Promise closeByIds(Long promiseId, Long userId) {
        Promise promise = findByIdAndCheckUserIdAndCheckNotClosedAndCheckNotExpired(promiseId, userId);

        return close(promise);
    }

    @Transactional
    public Promise close(Promise promise) {
        if (promise.getCloseDate().before(new Date())) {
            throw new PromiseActionException(ErrorCode.TOO_LATE_CLOSE_PROMISE);
        }

        promise.setStatus(Promise.Status.CLOSED);
        promise.setClosed(true);
        return save(promise);
    }

    @Transactional
    public Page<PromiseDto> getPage(PromisePageRequest promisePageRequest) {
        return promiseRepository.findPromisesByFilters(
                        promisePageRequest.getUserId(),
                        promisePageRequest.getNoticeEnabled(),
                        promisePageRequest.getImageEnabled(),
                        promisePageRequest.getInProgressEnabled(),
                        promisePageRequest.getUnderReviewEnabled(),
                        promisePageRequest.getVerifiedEnabled(),
                        promisePageRequest.getFailedEnabled(),
                        promisePageRequest.getClosedEnabled(),
                        promisePageRequest.getSort(),
                        PageRequest.of(promisePageRequest.getPageNum(), promisePageRequest.getPageSize())
                )
                .map(PromiseDto::new);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void checkExpiredPromises() {
        System.out.println("Checking expired promises");
        int page = 0;
        int pageSize = 100;
        boolean hasMore = true;
        while (hasMore) {
            Page<Promise> expiredPromises = promiseRepository
                    .findByClosedFalseAndExpiryDateBefore(
                            new Date(),
                            PageRequest.of(page, pageSize)
                    );

            if (expiredPromises == null || expiredPromises.isEmpty()) {
                break;
            }

            expiredPromises.getContent().forEach(promise -> {
                promise.setStatus(Promise.Status.FAILED);
                promise.setClosed(true);
                save(promise);
            });

            hasMore = expiredPromises.hasNext();
            page++;
        }
    }

    @Async
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void checkNotificationPromises() {
        System.out.println("Checking notification promises");
        int page = 0;
        int pageSize = 100;
        boolean hasMore = true;
        while (hasMore) {
            Page<Promise> notificationPromises = promiseRepository
                    .findByClosedFalseAndNotificationTrueAndNotificationDateBeforeAndNotificationSentFalse(
                            new Date(),
                            PageRequest.of(page, pageSize)
                    );

            if (notificationPromises == null || notificationPromises.isEmpty()) {
                break;
            }

            notificationPromises.getContent().forEach(promise -> {
                Email email = promise.getUser().getMainEmail();
                emailSenderService.sendPromiseNotification(email, promise);
                promise.setNotificationSent(true);
                save(promise);
            });

            hasMore = notificationPromises.hasNext();
            page++;
        }
    }

}
