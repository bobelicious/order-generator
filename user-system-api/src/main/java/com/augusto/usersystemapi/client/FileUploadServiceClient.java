package com.augusto.usersystemapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.augusto.usersystemapi.config.FeignSupportConfig;

@FeignClient(name = "images-system-api", configuration = FeignSupportConfig.class)
public interface FileUploadServiceClient {
    @PostMapping(value = "/api/v1/images/new", consumes = {"multipart/form-data"})
    public String createNewImage(@RequestPart(name = "imageMetadata") String imageMetadata,
            @RequestPart("photoFile") MultipartFile photoFile);

    @PutMapping(value = "/api/v1/images/update/{id}", consumes = {"multipart/form-data"})
    public String updateImage(@PathVariable(name = "id") String id,
            @RequestPart("photoFile") MultipartFile photoFile);
}
