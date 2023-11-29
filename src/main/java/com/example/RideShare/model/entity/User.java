package com.example.RideShare.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Nullable
    @JsonIgnoreProperties({"trip"})
    private List<Passenger> isPassengerInstances;

//    @OneToMany(mappedBy = "user")
//    @Nullable
//    private List<TripRequest> tripRequestsSent = new ArrayList<>();
}
