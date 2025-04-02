package com.augusto.order_gen_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.order_gen_auth.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    
}
