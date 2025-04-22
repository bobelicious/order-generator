package com.augusto.usersystemapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.usersystemapi.dtos.business.BusinessInsertDto;
import com.augusto.usersystemapi.dtos.business.BusinessOutPutDto;
import com.augusto.usersystemapi.service.BusinessService;

@RestController
@RequestMapping("/api/v1/business")
@RefreshScope
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @PostMapping("/new")
    public ResponseEntity<BusinessOutPutDto> createBusiness(@RequestBody BusinessInsertDto businessInsertDto) {
        return new ResponseEntity<BusinessOutPutDto>(businessService.insertBusiness(businessInsertDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/list-business")
    public ResponseEntity<List<BusinessOutPutDto>> createBusiness() {
        return new ResponseEntity<List<BusinessOutPutDto>>(businessService.listUserBusiness(),
                HttpStatus.CREATED);
    }

    @GetMapping("/list-business/{cnpj}")
    public ResponseEntity<BusinessOutPutDto> getBusinessById(@PathVariable String cnpj) {
        return new ResponseEntity<BusinessOutPutDto>(businessService.getBusinessByCnpj(cnpj),
                HttpStatus.OK);

    }
}
