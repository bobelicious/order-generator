package com.augusto.usersystemapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.usersystemapi.client.FileUploadServiceClient;
import com.augusto.usersystemapi.client.ViaCepClient;
import com.augusto.usersystemapi.dtos.AddressInputDto;
import com.augusto.usersystemapi.dtos.AddressOutputDto;
import com.augusto.usersystemapi.dtos.AddressViaCepDto;
import com.augusto.usersystemapi.dtos.ImageInsertDto;
import com.augusto.usersystemapi.dtos.UserInputDto;
import com.augusto.usersystemapi.dtos.UserInputUpdateDto;
import com.augusto.usersystemapi.dtos.UserOutputDto;
import com.augusto.usersystemapi.exceptions.ResourceNotFoundException;
import com.augusto.usersystemapi.exceptions.UserException;
import com.augusto.usersystemapi.model.Address;
import com.augusto.usersystemapi.model.Role;
import com.augusto.usersystemapi.model.User;
import com.augusto.usersystemapi.repository.AddressRepository;
import com.augusto.usersystemapi.repository.RoleRepository;
import com.augusto.usersystemapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private FileUploadServiceClient fileClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ViaCepClient viaCepClient;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    public UserOutputDto createUser(UserInputDto userInputDto) {
        var newUser = toUser(userInputDto);
        return toUserOutputDto(userRepository.save(newUser));
    }

    public UserOutputDto createUser(UserInputDto userInputDto, MultipartFile file) {
        var newUser = toUser(userInputDto);
        newUser.setPassword(encoder.encode(userInputDto.password()));
        newUser.setRoles(setUserRole(userInputDto.role()));
        var user = toUserOutputDto(
                createAddress(userInputDto.addressInputDto(), userRepository.save(newUser))
                        .getUser());
        callNewImgClient(newUser, file);
        return user;
    }

    private Set<Role> setUserRole(Long roleId) {
        var roleSet = new HashSet<Role>();
        roleSet.add(roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("find role", "role id", roleId)));
        return roleSet;
    }

    private String callNewImgClient(User user, MultipartFile file) {
        var imageDto = new ImageInsertDto("user", file.getOriginalFilename(), user.getUserCode());
        try {
            String imageMetadataJson = objectMapper.writeValueAsString(imageDto);
            var response = fileClient.createNewImage(imageMetadataJson, file);
            return response;
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public List<UserOutputDto> listAllUsers() {
        var usersOutDto = userRepository.findAllByDeletedFalse().stream()
                .map((u) -> toUserOutputDto(u)).toList();
        return usersOutDto;
    }

    public UserOutputDto findByCode(String code) {
        return toUserOutputDto(findUser(code));
    }

    public UserOutputDto updateUser(UserInputUpdateDto userDto) {
        var user = findUser(userDto.userCode());
        user.setEmail(userDto.email() == null ? user.getEmail() : userDto.email());
        user.setPhoneNumber(userDto.phoneNumber() == null ? user.getEmail() : userDto.phoneNumber());
        return toUserOutputDto(userRepository.save(user));
    }

    public void updateUserImage(MultipartFile file, String code) {
        findUser(code);
        fileClient.updateImage(code, file);
    }

    public void deleteUser(String userCode) {
        var user = findUser(userCode);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private Address createAddress(AddressInputDto adrsDto, User user) {
        Optional<AddressViaCepDto> viaCepAdrs = Optional.ofNullable(viaCepClient.getAddress(adrsDto.cep()));
        if (viaCepAdrs.isEmpty()) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Endereco invalido");
        }
        var adrs = new Address(viaCepAdrs.get(), adrsDto.houseNumber(), user, adrsDto.type());
        addressRepository.save(adrs);
        return adrs;
    }

    private User findUser(String userCode) {
        return userRepository.findByUserCode(userCode).orElseThrow(
                () -> new ResourceNotFoundException("find by code", "userCode", userCode));
    }

    private User toUser(UserInputDto userInputDto) {
        var user = new User(userInputDto, UUID.randomUUID().toString());
        return user;
    }

    private UserOutputDto toUserOutputDto(User user) {
        var addresses = user.getAddresses().stream()
                .map((address) -> new AddressOutputDto(address.getCep(), address.getStreet(), address.getNeighborhood(),
                        address.getCity(), address.getUf(), address.getHouseNumber()))
                .toList();
        var roles = user.getRoles().stream().map((role) -> role.getRole()).collect(Collectors.toSet());
        return new UserOutputDto(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserCode(),
                addresses,
                roles,
                user.getUserName());
    }
}
