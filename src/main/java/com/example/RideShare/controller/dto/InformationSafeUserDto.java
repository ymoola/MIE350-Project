package com.example.RideShare.controller.dto;


import lombok.Getter;
import lombok.Setter;

//DTO for users that keeps sensitive information like password
//and address hidden by not including them
@Getter
@Setter
public class InformationSafeUserDto {
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public InformationSafeUserDto(String email, String phoneNumber, String firstName, String lastName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
