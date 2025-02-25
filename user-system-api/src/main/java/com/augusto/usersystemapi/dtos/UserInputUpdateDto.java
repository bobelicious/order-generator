package com.augusto.usersystemapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInputUpdateDto {
    private String email;
    private String phoneNumber;
    private String userCode;
}
