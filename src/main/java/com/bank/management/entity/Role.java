package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

//Hibernate ko batata hai ki ye class database entity hai.
@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PostgreSQL automatically ID generate karega
    private Long id;

    @Setter
    @Column(nullable = false, unique = true) //column Null nhi ho sakta or dublicate nhi ho sakta
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

}