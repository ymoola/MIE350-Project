package com.example.RideShare.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String address;    //doesn't include postal code

    @Nullable
    private String postalCode;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}
