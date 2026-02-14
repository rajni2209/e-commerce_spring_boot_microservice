package com.ecommerce.profileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowNonPortable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String AddressType;

    private String houseNumber;

    private String state;

    private String city;

    private String zip;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
