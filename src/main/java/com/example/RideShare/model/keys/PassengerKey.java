package com.example.RideShare.model.keys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class PassengerKey implements Serializable {

    @Column(name="passengerTripId")
    Long passengerTripId;

    @Column(name= "passengerEmail")
    String passengerEmail;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(passengerTripId.hashCode()) +
                String.valueOf(passengerEmail.hashCode());
        return concatString.hashCode();
    }

    public PassengerKey() {}

    public PassengerKey(Long newPassengerTripId, String newPassengerEmail){
        this.setPassengerTripId(newPassengerTripId);
        this.setPassengerEmail(newPassengerEmail);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this)
            return true;
        if (!(o instanceof PassengerKey))
            return false;
        PassengerKey other = (PassengerKey) o;
        return passengerTripId.equals(other.passengerTripId) &&
                passengerEmail.equals(other.passengerEmail);
    }
}
