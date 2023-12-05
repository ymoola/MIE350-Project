package com.example.RideShare;

import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.entity.TripRequest;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.keys.TripRequestKey;
import com.example.RideShare.model.repository.PassengerRepository;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.TripRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.RideShare.TestFunctions.login;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TripRequestTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TripRequestRepository tripRequestRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TripRepository tripRepository;

    @Test
    @Order(1)
    void makeTripRequest() throws Exception {
        String email = "aubreyg@yahoo.com";
        String token = login(email, "money4fun", mockMvc);
        String alreadyPassengerEmail = "curry@chef.com";
        String alreadyPassengerToken = login(alreadyPassengerEmail, "splash", mockMvc);
        String msg = "HAIIIIII UwU";
        ObjectNode tripReqjson = objectMapper.createObjectNode();
        tripReqjson.put("tripId", 3L);
        tripReqjson.put("message", msg);

        TripRequestKey tripRequestKeyFullTrip = new TripRequestKey(3L, "aubreyg@yahoo.com");

        MockHttpServletResponse fullTripRes = mockMvc.perform(
                post("/triprequests")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(tripReqjson.toString())
        ).andReturn().getResponse();

        assertEquals(409, fullTripRes.getStatus());

        MockHttpServletResponse alreadyPassengerRes = mockMvc.perform(
                post("/triprequests")
                        .header("Authorization", alreadyPassengerToken)
                        .contentType("application/json")
                        .content(tripReqjson.toString())
        ).andReturn().getResponse();

        assertEquals(409, alreadyPassengerRes.getStatus());

        tripReqjson.put("tripId", 1L);

        MockHttpServletResponse validTripRes = mockMvc.perform(
                post("/triprequests")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(tripReqjson.toString())
        ).andReturn().getResponse();

        assertEquals(200, validTripRes.getStatus());

        assertTrue(tripRequestRepository.findById(new TripRequestKey(1L, "aubreyg@yahoo.com")).isPresent());
        TripRequest tripRequest = tripRequestRepository.findById(new TripRequestKey(1L, "aubreyg@yahoo.com")).get();
        assertEquals("aubreyg@yahoo.com", tripRequest.getUser().getEmail());
        assertEquals(1L, tripRequest.getTrip().getTripId());
        assertEquals(msg, tripRequest.getMessage());
    }

    @Test
    @Order(2)
    void updateReq() throws Exception {
        String email = "aubreyg@yahoo.com";
        String token = login(email, "money4fun", mockMvc);
        String msg = "I need a one dance, kiki you still luv me ;)";

        ObjectNode tripReqJson = objectMapper.createObjectNode();
        tripReqJson.put("message", msg);

        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                put("/triprequests/1/djkhaled@gmail.com")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(tripReqJson.toString())
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());
        assertTrue(tripRequestRepository.findById(new TripRequestKey(1L, "djkhaled@gmail.com")).isPresent());
        TripRequest unchangedReq = tripRequestRepository.findById(new TripRequestKey(1L, "djkhaled@gmail.com")).get();
        assertEquals("DJ KHALED!!!!!", unchangedReq.getMessage());

        MockHttpServletResponse res = mockMvc.perform(
                put("/triprequests/2/" + email)
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(tripReqJson.toString())
        ).andReturn().getResponse();

        assertEquals(200, res.getStatus());
        assertTrue(tripRequestRepository.findById(new TripRequestKey(2L, email)).isPresent());
        TripRequest changedReq = tripRequestRepository.findById(new TripRequestKey(2L, email)).get();
        assertEquals(msg, changedReq.getMessage());
    }

    @Test
    @Order(3)
    void cancelAndDecline() throws Exception {
        String driverEmail = "kevinjames@mallcop.com";
        String driverToken = login(driverEmail, "segway", mockMvc);
        String requesterEmail = "tswift@eras.com";
        String requesterToken = login(requesterEmail, "scootersuckz", mockMvc);
        String unauthorizedEmail = "aubreyg@yahoo.com";
        String unauthorizedToken = login(unauthorizedEmail, "money4fun", mockMvc);

        //make request to decline from unauthorized account
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/triprequests/decline/1/tswift@eras.com")
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());

        //make sure request is still there
        assertTrue(tripRequestRepository.existsById(new TripRequestKey(1L, "tswift@eras.com")));

        //make request to decline from driver account
        MockHttpServletResponse driverRes = mockMvc.perform(
                delete("/triprequests/decline/1/djkhaled@gmail.com")
                        .header("Authorization", driverToken)
        ).andReturn().getResponse();

        assertEquals(200, driverRes.getStatus());

        //make sure request was successfully deleted
        assertFalse(tripRequestRepository.existsById(new TripRequestKey(1L, "djkhaled@gmail.com")));

        //make request to decline from requester account
        MockHttpServletResponse requesterRes = mockMvc.perform(
                delete("/triprequests/decline/1/tswift@eras.com")
                        .header("Authorization", requesterToken)
        ).andReturn().getResponse();

        assertEquals(200, requesterRes.getStatus());

        //make sure request was successfully deleted
        assertFalse(tripRequestRepository.existsById(new TripRequestKey(1L, "tswift@eras.com")));
    }

    @Test
    @Order(4)
    void acceptingRequests() throws Exception {
        String driverEmail = "lebron@king.net";
        String driverToken = login(driverEmail, "Strive4Gr8ness", mockMvc);
        String unauthorizedEmail = "aubreyg@yahoo.com";
        String unauthorizedToken = login(unauthorizedEmail, "money4fun", mockMvc);
        String fullTripDriverEmail = "tswift@eras.com";
        String fullTripDriverToken = login(fullTripDriverEmail, "scootersuckz", mockMvc);

        //make request to accept from account which is not the trip driver
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/triprequests/accept/2/aubreyg@yahoo.com")
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());
        assertTrue(tripRequestRepository.existsById(new TripRequestKey(2L, unauthorizedEmail)));
        assertFalse(passengerRepository.existsById(new PassengerKey(2L, unauthorizedEmail)));

        //make request for a full trip
        MockHttpServletResponse fullTripRes = mockMvc.perform(
                delete("/triprequests/accept/3/curry@chef.com")
                        .header("Authorization", fullTripDriverToken)
        ).andReturn().getResponse();

        assertEquals(409, fullTripRes.getStatus());
        assertTrue(tripRequestRepository.existsById(new TripRequestKey(3L, "curry@chef.com")));
        assertFalse(passengerRepository.existsById(new PassengerKey(3L, "curry@chef.com")));

        //make valid trip accept request
        MockHttpServletResponse tripAcceptRes = mockMvc.perform(
                delete("/triprequests/accept/2/aubreyg@yahoo.com")
                        .header("Authorization", driverToken)
        ).andReturn().getResponse();

        assertEquals(200, tripAcceptRes.getStatus());
        assertFalse(tripRequestRepository.existsById(new TripRequestKey(2L, "aubreyg@yahoo.com")));
        assertTrue(passengerRepository.existsById(new PassengerKey(2L, "aubreyg@yahoo.com")));

        //ensure the remaining requests get deleted as this was the last slot taken for the trip
        assertFalse(tripRequestRepository.existsById(new TripRequestKey(2L,"kevinjames@mallcop.com")));

    }
}
