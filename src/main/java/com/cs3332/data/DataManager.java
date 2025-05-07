package com.cs3332.data;

import com.cs3332.Server;
import com.cs3332.core.object.Role;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.constructor.ProductionDBSource;
import com.cs3332.data.database.authentication.SystemFileAuthenticationBase;
import com.cs3332.data.database.product.SystemFileProductionDB;
import com.cs3332.data.object.auth.UserInformation;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager{
    private final Server server;
    private final Map<String, String> sectionTokenHolder = new HashMap<>();
    @Getter
    private final AuthenticationSource authenticationSource;
    @Getter
    private final ProductionDBSource productionDBSource;
    public DataManager(Server server){
        this.server = server;
        authenticationSource = new SystemFileAuthenticationBase();
        productionDBSource = new SystemFileProductionDB();
    }

    public void load(){
        this.authenticationSource.load();
        this.productionDBSource.load();
    }

    public void save(){
        this.authenticationSource.save();
    }

    public String createToken(String ID){
        String key = Utils.generateRDKey(32);
        this.sectionTokenHolder.put(key,ID);
        return key;
    }

    public boolean isValidToken(String token,String ID){
        if(token.equals("FULL_ACCESS_TOKEN"))
            return true;
        if(!this.sectionTokenHolder.containsKey(token))
            return false;
        return this.sectionTokenHolder.get(token).equals(ID);
    }

    @Nullable
    public UserInformation getAccountInformation(String token){
        if(!isValidToken(token))
            return null;
        if(token.equals("FULL_ACCESS_TOKEN")) {
            UserInformation userInformation = this.authenticationSource.getUserInformation(sectionTokenHolder.get(token));
            userInformation.getRoles().add(Role.MANAGER.name());
            userInformation.getRoles().add(Role.ADMIN.name());
            return userInformation;
        }
        return this.authenticationSource.getUserInformation(sectionTokenHolder.get(token));
    }
    @NotNull
    public List<Role> getRole(String token){
        if(token.equals("FULL_ACCESS_TOKEN")) {
            return List.of(Role.MANAGER, Role.ADMIN);
        }
        UserInformation information = getAccountInformation(token);
        if(information==null)
            return new ArrayList<>();
        return information.getRoles().stream().map(Role::valueOf).toList();
    }

    public boolean isValidToken(String token){
        return this.sectionTokenHolder.containsKey(token);
    }
}
