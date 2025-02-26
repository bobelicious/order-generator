package com.augusto.usersystemapi.dtos;

import java.util.List;

public record UserOutputDto(String name, String email, String phoneNumber, String userCode, List<AddressOutputDto> addresses) {
}
