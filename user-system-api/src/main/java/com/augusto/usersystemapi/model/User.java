package com.augusto.usersystemapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.augusto.usersystemapi.dtos.user.UserInputDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "usuarios")
@Table(name = "usuarios")
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
    @Column(name= "nome_usuario")
    private String userName;
    @Column(name = "email", unique = true)
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
    @Column(name = "senha")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
        )
    private Set<Role> roles;

    public User(UserInputDto userInputDto, String userCode) {
        this.name = userInputDto.name();
        this.email = userInputDto.email();
        this.cpf = userInputDto.cpf();
        this.phoneNumber = userInputDto.phoneNumber();
        this.userCode = userCode;
        this.userName = userInputDto.userName();
    }

}
