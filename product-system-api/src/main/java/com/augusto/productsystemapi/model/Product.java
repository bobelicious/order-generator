package com.augusto.productsystemapi.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "produtos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "data_adicao", nullable = false)
    private String addedDate;
    @Column(name = "data_atualizado", nullable = false)
    private List<String> updatedDate;
    @Column(name = "quantidade", nullable = false)
    private int quantity;
    @Column(name = "preco", nullable = false)
    private Float price;
    @Column(name = "codigo_produto", nullable = false, unique = true)
    private String code;
    @ Column(name = "cnpj_restaurante", nullable = false)
    private String cnpjOwner;
    @Column(name = "descricao", nullable = false)
    private String description;
}
