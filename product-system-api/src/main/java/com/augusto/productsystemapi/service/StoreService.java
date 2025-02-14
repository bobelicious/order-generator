package com.augusto.productsystemapi.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augusto.productsystemapi.client.ViaCepClient;
import com.augusto.productsystemapi.dtos.address.AddressInputDto;
import com.augusto.productsystemapi.dtos.address.AddressOutputDto;
import com.augusto.productsystemapi.dtos.product.ProductOutputDto;
import com.augusto.productsystemapi.dtos.store.StoreInputDto;
import com.augusto.productsystemapi.dtos.store.StoreOutputDto;
import com.augusto.productsystemapi.model.Address;
import com.augusto.productsystemapi.model.Store;
import com.augusto.productsystemapi.repository.StoreRepository;

@Service
public class StoreService {
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ViaCepClient viaCepClient;
  @Autowired
  private ProductService productService;

  public Store createStore(StoreInputDto storeDto) {
    var store = mapStore(storeDto);
    store = storeRepository.save(store);
    return store;
  }

  public StoreOutputDto getStoreByCnpj(String cnpj) {
    var store =
        storeRepository.findByCnpj(cnpj).orElseThrow(() -> new RuntimeException("store not found"));
    return toStoreOutDto(store);
  }

  private Store mapStore(StoreInputDto storeDto) {
    var store = new Store(storeDto.cnpj(), storeDto.storeName(), storeDto.userOwner(),
        createAddress(storeDto.address()));
    return store;
  }

  private Address createAddress(AddressInputDto addressDto) {
    var addressVC = viaCepClient.getAddress(addressDto.cep());
    var address = new Address(addressVC, addressDto.houseNumber(), "store");
    return address;
  }

  private AddressOutputDto toAddressOutputDto(Address address) {
    return new AddressOutputDto(address.getCep(), address.getStreet(), address.getNeighborhood(),
        address.getCity(), address.getUf(), address.getHouseNumber());
  }

  private StoreOutputDto toStoreOutDto(Store store) {
    var productDto = new ArrayList<ProductOutputDto>();
    if (store.getProducts() != null) {
      store.getProducts()
          .forEach((product) -> productDto.add(productService.toProductOutputDto(product)));
    }
    var addressDto = toAddressOutputDto(store.getAddress());
    return new StoreOutputDto(store.getCnpj(), store.getStoreName(), productDto, addressDto);
  }

}
