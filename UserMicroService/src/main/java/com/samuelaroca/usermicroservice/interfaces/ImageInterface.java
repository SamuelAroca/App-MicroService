package com.samuelaroca.usermicroservice.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageInterface {

    String saveImage(MultipartFile file) throws IOException;

}
