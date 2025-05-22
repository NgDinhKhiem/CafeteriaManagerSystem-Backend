package com.cs3332.data.object.auth;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInformationTest {
    @Test
    void testUserInformationConstructorAndGetters() {
        String username = "user1";
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";
        long dateOfBirth = 123456789L;
        String gender = "Male";
        List<String> roles = List.of("ADMIN", "MANAGER");

        UserInformation info = new UserInformation(username, name, email, phone, dateOfBirth, gender, roles);

        assertEquals(username, info.getUsername());
        assertEquals(name, info.getName());
        assertEquals(email, info.getEmail());
        assertEquals(phone, info.getPhone());
        assertEquals(dateOfBirth, info.getDateOfBirth());
        assertEquals(gender, info.getGender());
        assertEquals(roles, info.getRoles());
    }
} 