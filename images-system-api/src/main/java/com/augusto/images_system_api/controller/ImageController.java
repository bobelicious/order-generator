package com.augusto.images_system_api.controller;

import java.net.MalformedURLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.augusto.images_system_api.dtos.ImageInsertDto;
import com.augusto.images_system_api.service.ImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/api/v1/images")
public class ImageController {

  @Autowired
  private ImageService imageService;

  @PostMapping(value = "/new")
  public ResponseEntity<String> saveImage(@RequestPart("imageMetadata") String imageMetadataStr,
      @RequestParam MultipartFile photoFile) {
    ImageInsertDto imageMetadata;
    try {
      imageMetadata = new ObjectMapper().readValue(imageMetadataStr, ImageInsertDto.class);
      imageService.newImage(imageMetadata, photoFile);
      return new ResponseEntity<String>("Imagem salva com sucesso", HttpStatus.OK);
    } catch (JsonProcessingException e) {

      return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> putMethodName(@PathVariable String id, @RequestParam MultipartFile photoFile) {
    imageService.updateImage(id, photoFile);
    return new ResponseEntity<String>("Image updated successful", HttpStatus.OK);
  }

  @GetMapping("/{userID}")
  public ResponseEntity<Resource> getMethodName(@PathVariable String userID) {
    try {
      var image = imageService.getPhoto(userID);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + image.getFilename() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, "image/png").body(image);
    } catch (MalformedURLException e) {
      return ResponseEntity.badRequest().build();
    }

  }

}
