package com.augusto.usersystemapi.dtos;

import java.util.Date;

public record TokenDto(
        String username,
        Boolean authenticated,
        Date created,
        Date expiration,
        String accessToken,
        String refreshToken) {

}
