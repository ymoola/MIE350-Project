package com.example.RideShare.model.entity;

import com.example.RideShare.model.keys.TripRequestKey;
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
@Table(name = "tripRequest")
public class TripRequest {

    @EmbeddedId
    TripRequestKey tripRequestKey;

    @ManyToOne
    @MapsId("userEmail")
    @JoinColumn(name = "userEmail")
    @JsonIgnoreProperties({"tripRequests"}) //do we NEED this?
    private User user;

    @ManyToOne
    @MapsId("tripId")
    @JoinColumn(name = "tripId")
    @JsonIgnoreProperties({"tripRequests"})
    private Trip trip;

    private Date dateIssued;

    private String message;

}
