package com.augusto.productsystemapi.model;

import com.augusto.productsystemapi.dtos.address.AddressViaCepDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String cep;
  private String street;
  private String neighborhood;
  private String city;
  private String uf;
  private String houseNumber;
  @OneToOne
  @JoinColumn(name = "store_id")
  private Store store;
  private String type;

  public Address(AddressViaCepDto addressViaCepDto, String houseNumber, String type) {
    this.cep = addressViaCepDto.cep();
    this.street = addressViaCepDto.logradouro();
    this.neighborhood = addressViaCepDto.bairro();
    this.city = addressViaCepDto.localidade();
    this.uf = addressViaCepDto.uf();
    this.houseNumber = houseNumber;
    this.type = type;
  }
}
