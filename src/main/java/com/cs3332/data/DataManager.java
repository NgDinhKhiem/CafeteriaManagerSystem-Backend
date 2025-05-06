package com.cs3332.data;

import com.cs3332.Server;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.database.authentication.SystemFileAuthenticationBase;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class DataManager{
    private final Server server;
    private final Map<String, String> sectionTokenHolder = new HashMap<>();
    @Getter
    private final AuthenticationSource authenticationSource;
    public DataManager(Server server){
        this.server = server;
        authenticationSource = new SystemFileAuthenticationBase();
    }

    public String createToken(String ID){
        String key = Utils.generateRDKey(32);
        this.sectionTokenHolder.put(ID,key);
        return key;
    }

    public boolean isValidToken(String ID, String token){
        if(!this.sectionTokenHolder.containsKey(ID))
            return false;
        return this.sectionTokenHolder.get(ID).equals(token);
    }
}
