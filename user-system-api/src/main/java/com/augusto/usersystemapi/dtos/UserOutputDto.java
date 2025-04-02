package com.augusto.usersystemapi.dtos;

import java.util.List;
import java.util.Set;

public record UserOutputDto(String name, String email, String phoneNumber, String userCode, List<AddressOutputDto> addresses, Set<String> roles, String userName) {
}
