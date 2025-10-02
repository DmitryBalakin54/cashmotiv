package ru.cashmotiv.cashmotiv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.PromiseActionException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

@Service
public class PromiseImageService {

    @Value("${file.promise.image}")
    private String imagePath;

    private final Set<String> allowedExtensions = Set.of(
            "jpg",
            "jpeg",
            "png",
            "gif",
            "webp"
    );

    private final Set<String> allowedContentTypes = Set.of(
            "image/jpg",
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );


    public String saveImage(MultipartFile fileContent, Long promiseId) {
        validateImageContentType(fileContent);

        String originalFileName = fileContent.getOriginalFilename();

        if (!validateImageType(originalFileName)) {
            throw new PromiseActionException(ErrorCode.INCORRECT_IMAGE_TYPE);
        }


        Path uploadPath = Paths.get(imagePath).toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                return null;
            }
        }

        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        if (fileExtension == null) {
            return null;
        }

        String uniqueFileName = promiseId + "." + fileExtension.toLowerCase();

        Path targetLocation = uploadPath.resolve(uniqueFileName);

        try (InputStream stream = fileContent.getInputStream()) {
            Files.copy(stream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return null;
        }

        return uniqueFileName;
    }


    public boolean validateImageType(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        return extension != null && allowedExtensions.contains(extension.toLowerCase());
    }

    public void validateImageContentType(MultipartFile fileContent) {
        if (fileContent == null) {
            throw new PromiseActionException(ErrorCode.INCORRECT_IMAGE_IS_NULL);
        }

        String contentType = fileContent.getContentType();
        if (!allowedContentTypes.contains(contentType.toLowerCase())) {
            throw new PromiseActionException(ErrorCode.INCORRECT_IMAGE_MIME_TYPE);
        }
    }
}