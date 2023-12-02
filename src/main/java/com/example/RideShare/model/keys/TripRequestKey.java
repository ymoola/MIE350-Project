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

    @Column(name= "tripId")
    Long tripId;

    @Column(name = "requesterEmail")
    String requesterEmail;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(tripId.hashCode()) +
                String.valueOf(requesterEmail.hashCode());
        return concatString.hashCode();
    }

    public TripRequestKey() {}

    public TripRequestKey(Long newTripId, String email){
        this.setTripId(newTripId);
        this.setRequesterEmail(email);
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
                requesterEmail.equals(other.getRequesterEmail());
    }
}
