package com.samuelaroca.usermicroservice.services;

import com.samuelaroca.usermicroservice.entities.UserProfile;
import com.samuelaroca.usermicroservice.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserProfileRepository userProfileRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String uploadImage(Long userId, MultipartFile file) throws IOException {
        // Lógica para manejar la carga de la imagen y obtener la URL
        if (!file.isEmpty()) {
            // Obtener la entidad UserProfile actual del usuario
            UserProfile userProfile = userProfileRepository.findByUser_Id(userId);

            // Si existe una imagen anterior, eliminarla
            if (userProfile != null && userProfile.getImageUrl() != null) {
                String previousImageUrl = userProfile.getImageUrl();
                String previousFileName = previousImageUrl.substring(previousImageUrl.lastIndexOf("/") + 1);
                Path previousFilePath = Paths.get(uploadDir, previousFileName);

                try {
                    Files.deleteIfExists(previousFilePath);
                    // Log para verificar que la imagen anterior fue eliminada con éxito
                    System.out.println("Imagen anterior eliminada: " + previousFilePath);
                } catch (IOException e) {
                    // Log de cualquier error al intentar eliminar la imagen anterior
                    System.err.println("Error al eliminar la imagen anterior: " + previousFilePath);
                    e.printStackTrace();
                }
            }

            // Lógica para guardar la imagen en tu sistema de archivos y obtener la URL
            String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent()); // Asegura que los directorios existan
            Files.write(filePath, file.getBytes());

            // Devolver la URL completa
            return uploadDir + fileName;
        } else {
            throw new IllegalArgumentException("El archivo está vacío.");
        }
    }
}

