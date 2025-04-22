package com.augusto.usersystemapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.usersystemapi.client.ViaCepClient;
import com.augusto.usersystemapi.dtos.address.AddressInputDto;
import com.augusto.usersystemapi.dtos.address.AddressOutputDto;
import com.augusto.usersystemapi.dtos.address.AddressViaCepDto;
import com.augusto.usersystemapi.dtos.business.BusinessInsertDto;
import com.augusto.usersystemapi.dtos.business.BusinessOutPutDto;
import com.augusto.usersystemapi.exceptions.ResourceNotFoundException;
import com.augusto.usersystemapi.exceptions.UserException;
import com.augusto.usersystemapi.model.Address;
import com.augusto.usersystemapi.model.Business;
import com.augusto.usersystemapi.repository.BusinessRepository;
import com.augusto.usersystemapi.repository.UserRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ViaCepClient viaCepClient;

    public BusinessOutPutDto insertBusiness(BusinessInsertDto businessDto) {
        var user = userRepository.findByCpf(businessDto.ownerCpf())
                .orElseThrow(() -> new ResourceNotFoundException("insert business", "cpf", businessDto.ownerCpf()));
        var address = getFromViaCep(businessDto.address());
        var business = businessRepository.save(new Business(businessDto, user, address));
        return toBusisnesDto(business);
    }

    public List<BusinessOutPutDto> listUserBusiness() {
        var user = userService.getUserFromToken();
        var businessListDto = businessRepository.findAllByOwnerId(user.getId()).stream()
                .map((business) -> toBusisnesDto(business)).toList();
        return businessListDto;
    }

    public BusinessOutPutDto getBusinessByCnpj(String cnpj) {
        var user = userService.getUserFromToken();
        var business = businessRepository.findByCnpjAndOwnerId(cnpj, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("business", "cnpj", cnpj));
        return toBusisnesDto(business);
    }

    public BusinessOutPutDto updateBusinessMenu(List<Long>menu){
        return null;
    }

    private Address getFromViaCep(AddressInputDto adrsDto) {
        Optional<AddressViaCepDto> viaCepAdrs = Optional.ofNullable(viaCepClient.getAddress(adrsDto.cep()));
        if (viaCepAdrs.isEmpty()) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Endereco invalido");
        }
        var adrs = new Address(viaCepAdrs.get(), adrsDto.houseNumber(), adrsDto.type());
        return adrs;
    }

    private BusinessOutPutDto toBusisnesDto(Business business) {
        var addressDto = new AddressOutputDto(business.getAddress().getCep(), business.getAddress().getStreet(),
                business.getAddress().getNeighborhood(), business.getAddress().getCity(), business.getAddress().getUf(),
                business.getAddress().getHouseNumber());

        var businessDto = new BusinessOutPutDto(business.getBusinessName(),business.getCnpj(), business.getMenu(), addressDto,
                business.getLabelUrl(), business.getRating(), business.isActive(), business.getDeliveryTax());
        return businessDto;
    }
}
