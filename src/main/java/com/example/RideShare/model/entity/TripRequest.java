package com.example.RideShare.model.entity;

import com.example.RideShare.model.keys.TripRequestKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tripRequests")
public class TripRequest {


    // if a trip request is accepted it will be deleted
    // all of the trip requests active are outstanding/havent been accepted
    @EmbeddedId
    TripRequestKey tripRequestKey;

    @ManyToOne
    @MapsId("userEmail")
    @JoinColumn(name = "userEmail")
    @JsonIgnoreProperties({"tripRequests"}) //do we NEED this?
    private User user; // whoever makes the request

    @ManyToOne
    @MapsId("tripId")
    @JoinColumn(name = "tripId")
    @JsonIgnoreProperties({"requestsForTrip"})
    private Trip trip;

    @Temporal(TemporalType.DATE)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateIssued;
    private String message;

}
