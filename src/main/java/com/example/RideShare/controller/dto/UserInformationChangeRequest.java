package com.example.RideShare.controller.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserInformationChangeRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private String firstName;
    private String lastName;

    public UserInformationChangeRequest(String email,
                                        String currentPassword,
                                        String newPassword,
                                        String phoneNumber,
                                        String address,
                                        String postalCode,
                                        String firstName,
                                        String lastName) {

        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword=newPassword;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
