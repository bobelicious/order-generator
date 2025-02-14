package com.augusto.images_system_api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.augusto.images_system_api.dtos.ImageInsertDto;
import com.augusto.images_system_api.model.ImageDetails;
import com.augusto.images_system_api.repository.ImageDetailsRepository;

@Service
public class ImageService {
  @Autowired
  private ImageDetailsRepository imageDetailsRepository;

  public void newImage(ImageInsertDto imageDto, MultipartFile file) {
    var imageName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename() + ".png";
    var path = "D://aa_JAVA/images/%s/%s".formatted(imageDto.origin(), imageName);
    var imagefile = new File(path);
    System.out.println(imagefile);
    try {
      // Salva a imagem no formato PNG
      var imgFOS = new FileOutputStream(imagefile);
      imgFOS.write(file.getBytes());
      imgFOS.close();
      imageDetailsRepository.save(new ImageDetails(path, imageDto.userID(), imageDto.origin(),
          LocalDateTime.now().toString()));
    } catch (IOException e) {
      System.err.println("Erro ao salvar a imagem: " + e.getMessage());
    }
  }

  public void updateImage(String userId, MultipartFile file) {
    var path = imageDetailsRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Image Not Found")).getImageName();
    var imgFile = new File(path);
    try {
      var imgFOS = new FileOutputStream(imgFile);
      imgFOS.write(file.getBytes());
      imgFOS.close();
    } catch (Exception e) {
      System.err.println("Erro ao atualizar a imagem: " + e.getMessage());
    }

  }

  public Resource getPhoto(String userID) throws MalformedURLException {
    var imgDetail = imageDetailsRepository.findByUserId(userID)
        .orElseThrow(() -> new RuntimeException("Photo not found"));
    var file = new File(imgDetail.getImageName());
    Resource resource = new UrlResource(file.toURI());
    return resource;
  }
}
