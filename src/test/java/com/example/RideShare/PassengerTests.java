package com.example.RideShare;

import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.repository.PassengerRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PassengerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    void deletePassenger() throws Exception{

        String authorizedEmail = "kevinjames@mallcop.com";
        String authorizedToken = login(authorizedEmail, "segway", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        long driverTripId = 1;
        long passengerTripId = 3;
        String email = "kevinjames@mallcop.com";

        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/passengers/1/tswift@eras.com")
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();
        assertEquals(403, unauthorizedRes.getStatus());
        assertTrue(passengerRepository.existsById(new PassengerKey(driverTripId, "tswift@eras.com")));



        MockHttpServletResponse unauthorizedRes2 = mockMvc.perform(
                delete("/passengers/3/kevinjames@mallcop.com")
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes2.getStatus());
        assertTrue(passengerRepository.existsById(new PassengerKey(passengerTripId, email)));



        MockHttpServletResponse authorizedRes = mockMvc.perform(
                delete("/passengers/1/tswift@eras.com")
                        .header("Authorization", authorizedToken)
        ).andReturn().getResponse();

        assertEquals(200, authorizedRes.getStatus());
        assertFalse(passengerRepository.existsById(new PassengerKey(driverTripId, "tswift@eras.com")));



        MockHttpServletResponse authorizedRes2 = mockMvc.perform(
                delete("/passengers/3/kevinjames@mallcop.com")
                        .header("Authorization", authorizedToken)
        ).andReturn().getResponse();

        assertEquals(200, authorizedRes2.getStatus());
        assertFalse(passengerRepository.existsById(new PassengerKey(passengerTripId, email)));
    }
}
