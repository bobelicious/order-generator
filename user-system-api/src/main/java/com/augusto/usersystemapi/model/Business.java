package com.augusto.usersystemapi.model;

import java.math.BigDecimal;
import java.util.List;

import com.augusto.usersystemapi.dtos.business.BusinessInsertDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String cnpj;
    private String businessName;
    private List<Long> menu;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    private String labelUrl;
    private Double rating;
    private boolean active;
    private BigDecimal deliveryTax;

    public Business(BusinessInsertDto businessDto, User user, Address address) {
        this.cnpj = businessDto.cnpj();
        this.businessName = businessDto.businessName();
        this.owner = user;
        this.address = address;
        this.deliveryTax = businessDto.deliveryTax();
    }
}
