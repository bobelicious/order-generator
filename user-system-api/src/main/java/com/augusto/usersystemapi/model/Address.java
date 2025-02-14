package com.augusto.usersystemapi.model;

import com.augusto.usersystemapi.dtos.AddressViaCepDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
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
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  private String type;

  public Address(AddressViaCepDto addressViaCepDto, String houseNumber, User user, String type) {
    this.cep = addressViaCepDto.cep();
    this.street = addressViaCepDto.logradouro();
    this.neighborhood = addressViaCepDto.bairro();
    this.city = addressViaCepDto.localidade();
    this.uf = addressViaCepDto.uf();
    this.houseNumber = houseNumber;
    this.user = user;
    this.type = type;
  }
}
