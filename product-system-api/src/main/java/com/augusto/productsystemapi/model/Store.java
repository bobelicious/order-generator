package com.augusto.productsystemapi.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loja")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String cnpj;
  @Column(name = "nome_loja")
  private String storeName;
  @JoinColumn(name = "proprietario")
  private Long userOwner;
  @OneToMany(mappedBy = "store")
  private List<Product> products;
  @OneToOne(mappedBy = "store", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(name = "endereco")
  private Address address;

  public Store(String cnpj, String storeName, Long userOwner, Address address) {
    this.cnpj = cnpj;
    this.storeName = storeName;
    this.userOwner = userOwner;
    this.address = address;
  }
}
