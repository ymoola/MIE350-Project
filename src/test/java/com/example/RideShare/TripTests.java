package com.example.RideShare;

import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.keys.TripRequestKey;
import com.example.RideShare.model.repository.PassengerRepository;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.TripRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.RideShare.TestFunctions.login;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TripTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TripRequestRepository tripRequestRepository;
    @Test
    void addTrip() throws Exception {
        String email = "djkhaled@gmail.com";
        String token = login(email, "anothaOne", mockMvc);

        long numTripsBeforePost = tripRepository.count();

        //trip to add with car that does not belong to user
        ObjectNode tripJson = objectMapper.createObjectNode();
        tripJson.put("licensePlate", "ABC123");
        tripJson.put("sunday", false);
        tripJson.put("monday", true);
        tripJson.put("tuesday", true);
        tripJson.put("wednesday", true);
        tripJson.put("thursday", true);
        tripJson.put("friday", true);
        tripJson.put("saturday", false);
        tripJson.put("pickupTime", "9:00:00");
        tripJson.put("pickupAddress", "1035 Costigan Rd");
        tripJson.put("pickupCity", "Milton");
        tripJson.put("destinationAddress", "40 Temperance St");
        tripJson.put("destinationCity", "Toronto");
        tripJson.put("isRecurring", true);
        tripJson.put("startDate", "2023-06-10");
        tripJson.put("endDate", "2023-06-20");

        MockHttpServletResponse unAuthorizedPostResponse = mockMvc.perform(
                post("/trips")
                        .contentType("application/json")
                        .content(tripJson.toString())
                        .header("Authorization", token)
        ).andReturn().getResponse();

        //make sure no trip was posted
        assertEquals(403, unAuthorizedPostResponse.getStatus());
        assertEquals(numTripsBeforePost, tripRepository.count());

        //set vehicle to one owned by the user
        tripJson.put("licensePlate", "XYZ456");

        MockHttpServletResponse authorizedPostResponse = mockMvc.perform(
                post("/trips")
                        .contentType("application/json")
                        .content(tripJson.toString())
                        .header("Authorization", token)
        ).andReturn().getResponse();

        //make sure trip was successfully posted
        assertEquals(200, authorizedPostResponse.getStatus());
        assertEquals(1 + numTripsBeforePost, tripRepository.count());

        assertTrue(tripRepository.findById(4L).isPresent());
        Trip trip = tripRepository.findById(4L).get();

        assertEquals(trip.getDriver().getEmail(), "djkhaled@gmail.com");
        assertEquals(trip.getVehicle().getLicensePlate(), "XYZ456");
        assertFalse(trip.isSunday());
        assertTrue(trip.isMonday());
        assertTrue(trip.isTuesday());
        assertTrue(trip.isWednesday());
        assertTrue(trip.isThursday());
        assertTrue(trip.isFriday());
        assertFalse(trip.isSaturday());
        assertFalse(trip.isRecurring());
        assertEquals("9:00:00", trip.getPickupTime());
        assertEquals("2023-06-09", trip.getStartDate().toString());
        assertEquals("2023-06-19", trip.getEndDate().toString());
        assertEquals("1035 Costigan Rd", trip.getPickupAddress());
        assertEquals("Milton", trip.getPickupCity());
        assertEquals(43.51926, trip.getPickupLatitude());
        assertEquals(-79.85333, trip.getPickupLongitude());
        assertEquals("40 Temperance St", trip.getDestinationAddress());
        assertEquals("Toronto", trip.getDestinationCity());
        assertEquals(43.65094, trip.getDestinationLatitude());
        assertEquals(-79.38024, trip.getDestinationLongitude());
    }

    @Test
    void updateTripInfo() throws Exception{
        String driverEmail = "kevinjames@mallcop.com";
        String driverToken = login(driverEmail, "segway", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        ObjectNode tripJson = objectMapper.createObjectNode();
        tripJson.put("licensePlate", "PQR345");
        tripJson.put("sunday", false);
        tripJson.put("monday", true);
        tripJson.put("tuesday", true);
        tripJson.put("wednesday", true);
        tripJson.put("thursday", true);
        tripJson.put("friday", true);
        tripJson.put("saturday", false);
        tripJson.put("pickupTime", "9:00:00");
        tripJson.put("pickupAddress", "1035 Costigan Rd");
        tripJson.put("pickupCity", "Milton");
        tripJson.put("destinationAddress", "40 Temperance St");
        tripJson.put("destinationCity", "Toronto");
        tripJson.put("isRecurring", false);

        //try put request with incorrect account
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                put("/trips/1")
                        .contentType("application/json")
                        .content(tripJson.toString())
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());
        assertTrue(tripRepository.findById(1L).isPresent());
        Trip tripAfterUnauthorizedPut = tripRepository.findById(1L).get();

        assertEquals(tripAfterUnauthorizedPut.getDriver().getEmail(), "kevinjames@mallcop.com");
        assertEquals(tripAfterUnauthorizedPut.getVehicle().getLicensePlate(), "ABC123");
        assertFalse(tripAfterUnauthorizedPut.isSunday());
        assertFalse(tripAfterUnauthorizedPut.isMonday());
        assertTrue(tripAfterUnauthorizedPut.isTuesday());
        assertFalse(tripAfterUnauthorizedPut.isWednesday());
        assertTrue(tripAfterUnauthorizedPut.isThursday());
        assertFalse(tripAfterUnauthorizedPut.isFriday());
        assertFalse(tripAfterUnauthorizedPut.isSaturday());
        assertTrue(tripAfterUnauthorizedPut.isRecurring());
        assertEquals("8:30:00", tripAfterUnauthorizedPut.getPickupTime());
        assertEquals("2023-11-12", tripAfterUnauthorizedPut.getStartDate().toString());
        assertEquals("2023-11-16", tripAfterUnauthorizedPut.getEndDate().toString());
        assertEquals("427 Euclid Ave", tripAfterUnauthorizedPut.getPickupAddress());
        assertEquals("Toronto", tripAfterUnauthorizedPut.getPickupCity());
        assertEquals(43.65681, tripAfterUnauthorizedPut.getPickupLatitude());
        assertEquals(-79.41160, tripAfterUnauthorizedPut.getPickupLongitude());
        assertEquals("21 Park Ln Cir", tripAfterUnauthorizedPut.getDestinationAddress());
        assertEquals("Toronto", tripAfterUnauthorizedPut.getDestinationCity());
        assertEquals(43.73466, tripAfterUnauthorizedPut.getDestinationLatitude());
        assertEquals(-79.37427, tripAfterUnauthorizedPut.getDestinationLongitude());


        //try making put with a car not owned by correct user
        tripJson.put("licensePlate", "XYZ456");

        MockHttpServletResponse incorrectVehicleRes = mockMvc.perform(
                put("/trips/1")
                        .contentType("application/json")
                        .content(tripJson.toString())
                        .header("Authorization", driverToken)
        ).andReturn().getResponse();

        assertEquals(403, incorrectVehicleRes.getStatus());
        assertTrue(tripRepository.findById(1L).isPresent());
        Trip tripWrongVehicleSwitch = tripRepository.findById(1L).get();

        assertEquals(tripWrongVehicleSwitch.getDriver().getEmail(), "kevinjames@mallcop.com");
        assertEquals(tripWrongVehicleSwitch.getVehicle().getLicensePlate(), "ABC123");
        assertFalse(tripWrongVehicleSwitch.isSunday());
        assertFalse(tripWrongVehicleSwitch.isMonday());
        assertTrue(tripWrongVehicleSwitch.isTuesday());
        assertFalse(tripWrongVehicleSwitch.isWednesday());
        assertTrue(tripWrongVehicleSwitch.isThursday());
        assertFalse(tripWrongVehicleSwitch.isFriday());
        assertFalse(tripWrongVehicleSwitch.isSaturday());
        assertTrue(tripWrongVehicleSwitch.isRecurring());
        assertEquals("8:30:00", tripWrongVehicleSwitch.getPickupTime());
        assertEquals("2023-11-12", tripWrongVehicleSwitch.getStartDate().toString());
        assertEquals("2023-11-16", tripWrongVehicleSwitch.getEndDate().toString());
        assertEquals("427 Euclid Ave", tripWrongVehicleSwitch.getPickupAddress());
        assertEquals("Toronto", tripWrongVehicleSwitch.getPickupCity());
        assertEquals(43.65681, tripWrongVehicleSwitch.getPickupLatitude());
        assertEquals(-79.41160, tripWrongVehicleSwitch.getPickupLongitude());
        assertEquals("21 Park Ln Cir", tripWrongVehicleSwitch.getDestinationAddress());
        assertEquals("Toronto", tripWrongVehicleSwitch.getDestinationCity());
        assertEquals(43.73466, tripWrongVehicleSwitch.getDestinationLatitude());
        assertEquals(-79.37427, tripWrongVehicleSwitch.getDestinationLongitude());

        //make a valid put request
        tripJson.put("licensePlate", "JFK63");

        MockHttpServletResponse validPutRes = mockMvc.perform(
                put("/trips/1")
                        .contentType("application/json")
                        .content(tripJson.toString())
                        .header("Authorization", driverToken)
        ).andReturn().getResponse();

        assertEquals(200, validPutRes.getStatus());
        assertTrue(tripRepository.findById(1L).isPresent());
        Trip trip = tripRepository.findById(1L).get();

        assertEquals(trip.getDriver().getEmail(), "kevinjames@mallcop.com");
        assertEquals(trip.getVehicle().getLicensePlate(), "JFK63");
        assertFalse(trip.isSunday());
        assertTrue(trip.isMonday());
        assertTrue(trip.isTuesday());
        assertTrue(trip.isWednesday());
        assertTrue(trip.isThursday());
        assertTrue(trip.isFriday());
        assertFalse(trip.isSaturday());
        assertFalse(trip.isRecurring());
        assertEquals("9:00:00", trip.getPickupTime());
        assertEquals("2023-11-12", trip.getStartDate().toString());
        assertEquals("2023-11-16", trip.getEndDate().toString());
        assertEquals("1035 Costigan Rd", trip.getPickupAddress());
        assertEquals("Milton", trip.getPickupCity());
        assertEquals(43.51926, trip.getPickupLatitude());
        assertEquals(-79.85333, trip.getPickupLongitude());
        assertEquals("40 Temperance St", trip.getDestinationAddress());
        assertEquals("Toronto", trip.getDestinationCity());
        assertEquals(43.65094, trip.getDestinationLatitude());
        assertEquals(-79.38024, trip.getDestinationLongitude());
    }

    @Test
    void deleteTrip() throws Exception {
        String validEmail = "kevinjames@mallcop.com";
        String validToken = login(validEmail, "segway", mockMvc);
        String unauthorizedEmail = "djkhaled@gmail.com";
        String unauthorizedToken = login(unauthorizedEmail, "anothaOne", mockMvc);

        //unauthorized user makes req to delete trip
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/trips/1")
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());
        assertTrue(tripRepository.existsById(1L));

        MockHttpServletResponse res = mockMvc.perform(
                delete("/trips/1")
                        .header("Authorization", validToken)
        ).andReturn().getResponse();

        assertEquals(200, res.getStatus());
        assertFalse(tripRepository.existsById(1L));

        PassengerKey passengerKey = new PassengerKey(1L,"tswift@eras.com");
        assertFalse(passengerRepository.existsById(passengerKey));

        TripRequestKey tripRequestKey = new TripRequestKey(1L, "djkhaled@gmail.com");
        assertFalse(tripRequestRepository.existsById(tripRequestKey));
    }
}
