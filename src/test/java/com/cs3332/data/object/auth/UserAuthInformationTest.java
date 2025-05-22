package com.cs3332.data.object.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthInformationTest {
    @Test
    void testUserAuthInformationConstructorAndGetters() {
        String username = "user1";
        String password = "password123";

        UserAuthInformation info = new UserAuthInformation(username, password);

        assertEquals(username, info.getUsername());
        assertEquals(password, info.getPassword());
    }
} 