package com.example.RideShare;

import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.VehicleRepository;
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
public class VehicleTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void addVehicle() throws Exception {
        String email = "curry@chef.com";
        String token = login(email, "splash", mockMvc);

        ObjectNode vehicleJson = objectMapper.createObjectNode();
        vehicleJson.put("licensePlate", "GOLKERS");
        vehicleJson.put("passengerSeats", 4);
        vehicleJson.put("ownerEmail", email);


        MockHttpServletResponse postRes = mockMvc.perform(
                post("/vehicles").
                contentType("application/json").
                content(vehicleJson.toString()).
                header("Authorization", token)
        ).
            andReturn().
            getResponse();

        assertEquals(200, postRes.getStatus());

        assertTrue(vehicleRepository.existsById("GOLKERS"));
        Vehicle addedVehicle = vehicleRepository.findById("GOLKERS").get();

        assertEquals(addedVehicle.getLicensePlate(), "GOLKERS");
        assertEquals(addedVehicle.getPassengerSeats(), 4);
        assertEquals(addedVehicle.getOwner().getEmail(), email);
    }

    @Test
    void deletingVehicles() throws Exception {
        String authorizedEmail = "tswift@eras.com";
        String authorizedToken = login(authorizedEmail, "scootersuckz", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        //try deleting vehicle with an account where user is not the owner
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/vehicles/TS1989")
                        .header("Authorization", unauthorizedToken)
        )
        .andReturn()
        .getResponse();

        assertEquals(403, unauthorizedRes.getStatus());

        //make sure vehicle is still there
        assertTrue(vehicleRepository.existsById("TS1989"));

        //try deleting same vehicle with the account where user is the owner
        MockHttpServletResponse authorizedRes = mockMvc.perform(
                        delete("/vehicles/TS1989")
                                .header("Authorization", authorizedToken)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, authorizedRes.getStatus());

        //ensure that the vehicle was successfully deleted
        assertFalse(vehicleRepository.existsById("TS1989"));
    }

    @Test
    void updateVehicleInformation() throws Exception{
        String authorizedEmail = "kevinjames@mallcop.com";
        String authorizedToken = login(authorizedEmail, "segway", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        ObjectNode vehicleJson = objectMapper.createObjectNode();
        vehicleJson.put("color", "maroon");
        vehicleJson.put("model", "Toyota");

        //try with an unauthorized account
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                put("/vehicles/ABC123")
                        .contentType("application/json")
                        .content(vehicleJson.toString())
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());

        //make sure the information of the vehicle is unchanged
        assertTrue(vehicleRepository.findById("ABC123").isPresent());
        Vehicle vehicleBeforeChanges = vehicleRepository.findById("ABC123").get();

        assertEquals("ABC123", vehicleBeforeChanges.getLicensePlate());
        assertEquals(5, vehicleBeforeChanges.getPassengerSeats());
        assertEquals("kevinjames@mallcop.com", vehicleBeforeChanges.getOwner().getEmail());
        assertNull(vehicleBeforeChanges.getColor());
        assertNull(vehicleBeforeChanges.getModel());

        //try with an authorized account
        MockHttpServletResponse authorizedRes = mockMvc.perform(
                put("/vehicles/ABC123")
                        .contentType("application/json")
                        .content(vehicleJson.toString())
                        .header("Authorization", authorizedToken)
        ).andReturn().getResponse();

        assertEquals(200, authorizedRes.getStatus());

        //make sure the information of the vehicle is changed correctly
        assertTrue(vehicleRepository.findById("ABC123").isPresent());
        Vehicle vehicleAfterChanges = vehicleRepository.findById("ABC123").get();

        assertEquals("ABC123", vehicleAfterChanges.getLicensePlate());
        assertEquals(5, vehicleAfterChanges.getPassengerSeats());
        assertEquals("kevinjames@mallcop.com", vehicleAfterChanges.getOwner().getEmail());
        assertEquals("maroon", vehicleAfterChanges.getColor());
        assertEquals("Toyota", vehicleAfterChanges.getModel());
    }
}
