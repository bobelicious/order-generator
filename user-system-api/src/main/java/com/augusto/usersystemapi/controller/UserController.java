package com.augusto.usersystemapi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.augusto.usersystemapi.dtos.UserInputDto;
import com.augusto.usersystemapi.dtos.UserInputUpdateDto;
import com.augusto.usersystemapi.dtos.UserOutputDto;
import com.augusto.usersystemapi.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ResponseEntity<UserOutputDto> createUser(@RequestBody UserInputDto userInputDto) {
        return new ResponseEntity<UserOutputDto>(userService.createUser(userInputDto),
                HttpStatus.CREATED);
    }

    @PostMapping("/new/photo")
    public ResponseEntity<UserOutputDto> createUser(@RequestPart("user") UserInputDto userInputDto,
            @RequestParam(name = "photoFile") MultipartFile file) {
        return new ResponseEntity<UserOutputDto>(userService.createUser(userInputDto, file),
                HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserOutputDto>> listAllUsers() {
        return new ResponseEntity<List<UserOutputDto>>(userService.listAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<UserOutputDto> getByCode(@PathVariable String code) {
        return new ResponseEntity<UserOutputDto>(userService.findByCode(code), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserOutputDto> updateUser(
            @RequestBody UserInputUpdateDto userInputUpdateDto) {
        return new ResponseEntity<UserOutputDto>(userService.updateUser(userInputUpdateDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<String>("success", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/image/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,
            @RequestParam(name = "photoFile") MultipartFile file) {
        userService.updateUserImage(file, id);
        return new ResponseEntity<String>("Image update successful", HttpStatus.OK);
    }
}

