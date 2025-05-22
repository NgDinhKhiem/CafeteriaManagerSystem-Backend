package com.cs3332.data;

import com.cs3332.core.object.Role;
import com.cs3332.data.object.auth.UserInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = new DataManager(null); // Server is not used in tested methods
    }

    @Test
    void testCreateAndValidateToken() {
        String id = "user1";
        String token = dataManager.createToken(id);
        assertNotNull(token);
        assertTrue(dataManager.isValidToken(token, id));
        assertFalse(dataManager.isValidToken(token, "otherUser"));
    }

    @Test
    void testFullAccessTokenAlwaysValid() {
        assertTrue(dataManager.isValidToken("FULL_ACCESS_TOKEN", "any"));
        assertTrue(dataManager.isValidToken("FULL_ACCESS_TOKEN"));
    }

    @Test
    void testGetRoleWithFullAccessToken() {
        List<Role> roles = dataManager.getRole("FULL_ACCESS_TOKEN");
        assertTrue(roles.contains(Role.MANAGER));
        assertTrue(roles.contains(Role.ADMIN));
    }

    @Test
    void testGetAccountInformationWithFullAccessToken() {
        UserInformation info = dataManager.getAccountInformation("FULL_ACCESS_TOKEN");
        assertNotNull(info);
        assertEquals("Console", info.getUsername());
        assertTrue(info.getRoles().contains("MANAGER"));
        assertTrue(info.getRoles().contains("ADMIN"));
    }
} 