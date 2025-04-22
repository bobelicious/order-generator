package com.augusto.usersystemapi.dtos.user;

import java.util.List;
import java.util.Set;

import com.augusto.usersystemapi.dtos.address.AddressOutputDto;

public record UserOutputDto(String name, String email, String phoneNumber, String userCode, String userName, List<AddressOutputDto> addresses, Set<String> roles ) {
}
