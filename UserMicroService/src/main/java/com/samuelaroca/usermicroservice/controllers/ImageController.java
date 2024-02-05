package com.samuelaroca.usermicroservice.controllers;

import com.samuelaroca.usermicroservice.entities.User;
import com.samuelaroca.usermicroservice.entities.UserProfile;
import com.samuelaroca.usermicroservice.repositories.UserProfileRepository;
import com.samuelaroca.usermicroservice.repositories.UserRepository;
import com.samuelaroca.usermicroservice.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/user/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> handleImageUpload(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            // Obtener el usuario por su ID usando el UserRepository
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

            // Obtener o crear la entidad UserProfile
            UserProfile userProfile = user.getUserProfile();
            if (userProfile == null) {
                userProfile = new UserProfile();
                userProfile.setUser(user);
            }

            // Llamar al servicio para manejar la carga de la imagen
            String imageUrl = imageService.uploadImage(userId, file);

            // Guardar la URL en la entidad UserProfile
            userProfile.setImageUrl(imageUrl);
            userProfileRepository.save(userProfile);

            return ResponseEntity.ok("Imagen cargada con éxito.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar la imagen." + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long userId) throws IOException {
        UserProfile userProfile = userProfileRepository.findByUser_Id(userId);

        if (userProfile != null && userProfile.getImageUrl() != null) {
            String fileName = userProfile.getImageUrl().substring(userProfile.getImageUrl().lastIndexOf("/") + 1);
            Path imagePath = Paths.get(uploadDir).resolve(fileName);

            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE).body(resource);
            } else {
                throw new FileNotFoundException("Image not found for user ID: " + userId);
            }
        } else {
            throw new FileNotFoundException("Image URL not found for user ID: " + userId);
        }
    }
}
