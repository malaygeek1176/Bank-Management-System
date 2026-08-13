package com.bank.management.repository;

import com.bank.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> { //Role → kis Entity ke saath kaam karna hai,
                                                                    //Long → Entity ki Primary Key ka type

    Optional<Role> findByName(String name);//Optional isliye use kiya kyuki role mil bhi skta hai or nhi bhi
}