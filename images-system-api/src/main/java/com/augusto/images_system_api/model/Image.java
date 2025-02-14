package com.augusto.images_system_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {
    private String imageUUID;
    private String imageName;
    private byte[] imageBytes;
}
