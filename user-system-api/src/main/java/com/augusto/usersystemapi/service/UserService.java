package com.augusto.usersystemapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.usersystemapi.client.FileUploadServiceClient;
import com.augusto.usersystemapi.client.ViaCepClient;
import com.augusto.usersystemapi.dtos.ImageInsertDto;
import com.augusto.usersystemapi.dtos.address.AddressInputDto;
import com.augusto.usersystemapi.dtos.address.AddressOutputDto;
import com.augusto.usersystemapi.dtos.address.AddressViaCepDto;
import com.augusto.usersystemapi.dtos.user.UserInputDto;
import com.augusto.usersystemapi.dtos.user.UserInputUpdateDto;
import com.augusto.usersystemapi.dtos.user.UserOutputDto;
import com.augusto.usersystemapi.exceptions.ResourceNotFoundException;
import com.augusto.usersystemapi.exceptions.UserException;
import com.augusto.usersystemapi.model.Address;
import com.augusto.usersystemapi.model.Role;
import com.augusto.usersystemapi.model.User;
import com.augusto.usersystemapi.repository.RoleRepository;
import com.augusto.usersystemapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
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

    @Transactional
    public UserOutputDto createUser(UserInputDto userInputDto) {
        var newUser = toUser(userInputDto);
        newUser.setPassword(encoder.encode(userInputDto.password()));
        newUser.setRoles(setUserRole(userInputDto.role()));
        var address = getFromViaCep(userInputDto.addressInputDto());
        newUser.getAddresses().add(address);
        var user = toUserOutputDto(userRepository.save(newUser));
        return user;
    }

    @Transactional
    public UserOutputDto createUser(UserInputDto userInputDto, MultipartFile file) {
        var newUser = toUser(userInputDto);
        newUser.setPassword(encoder.encode(userInputDto.password()));
        newUser.setRoles(setUserRole(userInputDto.role()));
        var address = getFromViaCep(userInputDto.addressInputDto());
        newUser.getAddresses().add(address);
        var user = toUserOutputDto(userRepository.save(newUser));
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

    @Transactional
    public UserOutputDto updateUser(UserInputUpdateDto userDto) {
        var user = findUser(userDto.userCode());
        validateUser(user.getId());
        user.setEmail(userDto.email() == null ? user.getEmail() : userDto.email());
        user.setPhoneNumber(userDto.phoneNumber() == null ? user.getEmail() : userDto.phoneNumber());
        return toUserOutputDto(userRepository.save(user));
    }

    @Transactional
    public void updateUserImage(MultipartFile file, String code) {
        var user = findUser(code);
        validateUser(user.getId());
        fileClient.updateImage(code, file);
    }

    @Transactional
    public void deleteUser(String userCode) {
        var user = findUser(userCode);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private Address getFromViaCep(AddressInputDto adrsDto) {
        Optional<AddressViaCepDto> viaCepAdrs = Optional.ofNullable(viaCepClient.getAddress(adrsDto.cep()));
        if (viaCepAdrs.isEmpty()) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Endereco invalido");
        }
        var adrs = new Address(viaCepAdrs.get(), adrsDto.houseNumber(), adrsDto.type());
        return adrs;
    }

    private void validateUser(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        var userLogedId = userDetails.getUsername();
        var user = userRepository.findByEmailOrCpfOrUserNameAndDeletedFalse(userLogedId, userLogedId, userLogedId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userLogedId));
        if (user.getId() != id) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "You can't change other user data");
        }
    }
    
    public User getUserFromToken(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        var userLogedId = userDetails.getUsername();
        var user = userRepository.findByEmailOrCpfOrUserNameAndDeletedFalse(userLogedId, userLogedId, userLogedId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userLogedId));
        return user;
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
                user.getUserName(),
                addresses,
                roles);
    }
}
