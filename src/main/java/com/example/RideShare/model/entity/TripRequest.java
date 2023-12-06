package com.example.RideShare.model.entity;

import com.example.RideShare.model.keys.TripRequestKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tripRequests")
public class TripRequest {
    @EmbeddedId
    TripRequestKey tripRequestKey;

    @ManyToOne
    @MapsId("requesterEmail")
    @JoinColumn(name = "requesterEmail")
    @JsonIgnoreProperties({"tripRequestsSent",
                            "password",
                            "phoneNumber",
                            "address",
                            "postalCode",
                            "isPassengerInstances"
                            })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @MapsId("tripId")
    @JoinColumn(name = "tripId")
    @JsonIgnoreProperties({"requestsForTrip",
                            "vehicle",
                            "passengers",
                            "sunday",
                            "monday",
                            "tuesday",
                            "wednesday",
                            "thursday",
                            "friday",
                            "saturday",
                            "pickupLatitude",
                            "pickupLongitude",
                            "destinationLatitude",
                            "destinationLongitude"
                            })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trip trip;

    @Temporal(TemporalType.DATE)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateIssued;
    private String message;

}
