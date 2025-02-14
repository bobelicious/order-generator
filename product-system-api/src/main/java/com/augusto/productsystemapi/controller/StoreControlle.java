package com.augusto.productsystemapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.augusto.productsystemapi.dtos.store.StoreInputDto;
import com.augusto.productsystemapi.dtos.store.StoreOutputDto;
import com.augusto.productsystemapi.model.Store;
import com.augusto.productsystemapi.service.StoreService;


@Controller
@RequestMapping("/api/v1/store")
public class StoreControlle {
  @Autowired
  private StoreService storeService;

  @PostMapping("/new")
  public ResponseEntity<Store> postMethodName(@RequestBody StoreInputDto store) {
    return new ResponseEntity<Store>(storeService.createStore(store), HttpStatus.CREATED);
  }

  @GetMapping("/{cnpj}")
  public ResponseEntity<StoreOutputDto> getByCnpj(@PathVariable String cnpj){
    return new ResponseEntity<StoreOutputDto>(storeService.getStoreByCnpj(cnpj), HttpStatus.OK);
  }
}
