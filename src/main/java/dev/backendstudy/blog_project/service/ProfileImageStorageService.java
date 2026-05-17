package dev.backendstudy.blog_project.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImageStorageService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private final Path uploadDir = Paths.get("uploads", "profile-images").toAbsolutePath().normalize();

    public String store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        try {
            Files.createDirectories(uploadDir);
            String storedFilename = UUID.randomUUID() + "." + extension;
            Path targetPath = uploadDir.resolve(storedFilename).normalize();

            if (!targetPath.startsWith(uploadDir)) {
                throw new IllegalArgumentException("잘못된 파일 경로입니다.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/profile-images/" + storedFilename;
        } catch (IOException e) {
            throw new IllegalStateException("프로필 이미지 저장에 실패했습니다.", e);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("이미지 파일 확장자가 필요합니다.");
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
