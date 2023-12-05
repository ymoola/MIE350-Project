package com.example.RideShare;

import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.keys.TripRequestKey;
import com.example.RideShare.model.repository.*;
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

import static com.example.RideShare.TestFunctions.login;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TripRequestRepository tripRequestRepository;

    @Test
    @Order(1)
    void addUser() throws Exception{
        long numUsersBefore = userRepository.count();

        String email = "tchalla@wakanda.com";
        ObjectNode userJson = objectMapper.createObjectNode();
        userJson.put("email", email);
        userJson.put("password", "vibranium");
        userJson.put("phoneNumber", "1245122");
        userJson.put("address", "51 Wakanda Blvd Wakanda City");
        userJson.put("postalCode", "23409");
        userJson.put("firstName", "T'Challa");
        userJson.put("lastName", "Udaku");


        MockHttpServletResponse postRes = mockMvc.perform(
                        post("/users").
                                contentType("application/json").
                                content(userJson.toString())
                ).
                andReturn().
                getResponse();

        assertEquals(200, postRes.getStatus());

        assertTrue(userRepository.findById(email).isPresent());
        User addedUser = userRepository.findById(email).get();

        assertEquals("vibranium", addedUser.getPassword());
        assertEquals(numUsersBefore + 1, userRepository.count());
    }

    @Test
    @Order(2)
    void updateUserInformation() throws Exception {
        String authorizedEmail = "kevinjames@mallcop.com";
        String authorizedToken = login(authorizedEmail, "segway", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        User userBeforeChanges = userRepository.findById("kevinjames@mallcop.com").get();

        ObjectNode userJson = objectMapper.createObjectNode();
        userJson.put("address", "32 Vibranium St Silver Spring");
        userJson.put("firstName", "T'Chaka");
        userJson.put("lastName", "Udaka");

        //try with an unauthorized account
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                put("/users/kevinjames@mallcop.com")
                        .contentType("application/json")
                        .content(userJson.toString())
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());

        //make sure the information of the vehicle is unchanged
        assertTrue(userRepository.findById("kevinjames@mallcop.com").isPresent());
        User unchangedUser = userRepository.findById("kevinjames@mallcop.com").get();

        assertEquals(userBeforeChanges.getAddress(), unchangedUser.getAddress());
        assertEquals(userBeforeChanges.getFirstName(), unchangedUser.getFirstName());
        assertEquals(userBeforeChanges.getLastName(), unchangedUser.getLastName());

        userJson.put("email", "kevinjames@mallcop.com");
        userJson.put("currentPassword", "segway");
        userJson.put("newPassword", "Wakanda");
        //try with an authorized account
        MockHttpServletResponse authorizedRes = mockMvc.perform(
                put("/users/kevinjames@mallcop.com")
                        .contentType("application/json")
                        .content(userJson.toString())
                        .header("Authorization", authorizedToken)
        ).andReturn().getResponse();

        assertEquals(200, authorizedRes.getStatus());

        //make sure the information of the vehicle is changed correctly
        assertTrue(userRepository.findById("kevinjames@mallcop.com").isPresent());
        User userAfterChanges = userRepository.findById("kevinjames@mallcop.com").get();

        assertEquals("32 Vibranium St Silver Spring", userAfterChanges.getAddress());
        assertEquals("T'Chaka", userAfterChanges.getFirstName());
        assertEquals("Udaka", userAfterChanges.getLastName());
        assertEquals("Wakanda", userAfterChanges.getPassword());
    }

    @Test
    @Order(3)
    void deletingUsers() throws Exception {
        String authorizedEmail = "kevinjames@mallcop.com";
        String authorizedToken = login(authorizedEmail, "Wakanda", mockMvc);
        String unauthorizedEmail = "curry@chef.com";
        String unauthorizedToken = login(unauthorizedEmail, "splash", mockMvc);

        long numUsersBefore = userRepository.count();

        ObjectNode passwordJson = objectMapper.createObjectNode();
        passwordJson.put("password", "Wakanda");


        //try deleting vehicle with an account where user is not the owner
        MockHttpServletResponse unauthorizedRes = mockMvc.perform(
                delete("/users/kevinjames@mallcop.com")
                        .contentType("application/json")
                        .content(passwordJson.toString())
                        .header("Authorization", unauthorizedToken)
        ).andReturn().getResponse();

        assertEquals(403, unauthorizedRes.getStatus());
        assertEquals(numUsersBefore, userRepository.count());

        //make sure vehicle is still there
        assertTrue(userRepository.existsById("kevinjames@mallcop.com"));

        //try deleting same vehicle with the account where user is the owner
        MockHttpServletResponse authorizedRes = mockMvc.perform(
                delete("/users/kevinjames@mallcop.com")
                        .contentType("application/json")
                        .content(passwordJson.toString())
                        .header("Authorization", authorizedToken)
        ).andReturn().getResponse();

        assertEquals(200, authorizedRes.getStatus());
        assertEquals(numUsersBefore - 1, userRepository.count());

        //ensure that the vehicle was successfully deleted
        assertFalse(userRepository.existsById("kevinjames@mallcop.com"));
        assertFalse(tripRepository.existsById(1L));
        assertFalse(vehicleRepository.existsById("ABC123"));
        assertFalse(passengerRepository.existsById(new PassengerKey(3L, "kevinjames@mallcop.com")));
        assertFalse(tripRequestRepository.existsById(new TripRequestKey(2L, "kevinjames@mallcop.com")));

    }
}
