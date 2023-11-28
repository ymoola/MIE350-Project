package com.example.RideShare.model.keys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class TripRequestKey implements Serializable {

    @Column(name= "tripId") // the trip's id not tripRequest
    Long tripId;

    @Column(name = "userEmail") // define the column in SQL??
    String userEmail;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(tripId.hashCode()) +
                String.valueOf(userEmail.hashCode());
        return concatString.hashCode();
    }

    public TripRequestKey() {}

    public TripRequestKey(Long newTripId, String newUserEmail){
        this.setTripId(newTripId);
        this.setUserEmail(newUserEmail);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this)
            return true;
        if (!(o instanceof TripRequestKey))
            return false;
        TripRequestKey other = (TripRequestKey) o;
        return tripId.equals(other.tripId) &&
                userEmail.equals(other.userEmail);
    }
}
