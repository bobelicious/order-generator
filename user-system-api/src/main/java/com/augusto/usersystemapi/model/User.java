package com.augusto.usersystemapi.model;

import java.util.ArrayList;
import java.util.List;

import com.augusto.usersystemapi.dtos.UserInputDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "usuarios")
@Table(name =  "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String name;
    @Column(name = "email",  unique = true)
    private String email;
    @Column(name = "cpf", unique = true)
    private String cpf;
    @Column(name = "numero_telefone")
    private String phoneNumber;
    @Column(name = "codigo_usuario", unique = true)
    private String userCode;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();
    @Column(name = "is_deleted")
    private boolean deleted = false;

    public User(UserInputDto userInputDto, String userCode) {
        this.name = userInputDto.getName();
        this.email = userInputDto.getEmail();
        this.cpf = userInputDto.getCpf();
        this.phoneNumber = userInputDto.getPhoneNumber();
        this.userCode = userCode;
    }

    
}
