package com.augusto.productsystemapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.productsystemapi.config.FeignSupportConfig;

@FeignClient(name = "images-system-api", configuration = FeignSupportConfig.class)
public interface FileUploadServiceClient {
    @PostMapping(value = "/api/v1/images/new", consumes = {"multipart/form-data"})
    public String createNewImage(@RequestPart(name= "imageMetadata") String imageMetadata,
            @RequestPart("photoFile") MultipartFile photoFile);
}
