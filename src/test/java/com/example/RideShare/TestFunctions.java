package com.example.RideShare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestFunctions {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Takes email and password of user and logs into the account.
     * @param email The email of the account.
     * @param password The password of the account.
     * @param mockMvc The mockMvc object in the test class to make the http request.
     * @return :type String: The JWT (JSON Web Token) of the authenticated user.
     */
    public static String login(String email, String password, MockMvc mockMvc) throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("username", email);
        loginJson.put("password", password);

        MockHttpServletResponse res = mockMvc.perform(post("/login").
                        contentType("application/json").
                        content(loginJson.toString())).
                andReturn().getResponse();

        assertEquals(200, res.getStatus());
        assertTrue(res.containsHeader("Authorization"));
        return (String) res.getHeaderValue("Authorization");
    }
}
