package com.augusto.usersystemapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageInsertDto {
    private String origin; 
    private String imageName;
    private String userID;
}
