package project.bicos.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${app.storage.upload-dir:uploads}")
    private String uploadDir;

    private Path root;

    @PostConstruct
    public void init() {
        root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads: " + root, e);
        }
    }

    public String salvar(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + extension;

        try {
            Path target = root.resolve(fileName);
            Files.copy(file.getInputStream(), target);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + fileName, e);
        }
    }

    public void deletar(String url) {
        if (url == null || url.isBlank()) return;

        String fileName = url.substring(url.lastIndexOf("/") + 1);
        try {
            Path file = root.resolve(fileName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar arquivo: " + fileName, e);
        }
    }
}
