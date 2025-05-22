package com.cs3332.data.database.authentication;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemFileAuthenticationDatabase implements AuthenticationSource {
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, UserAuthInformation> authInformationHolder = new ConcurrentHashMap<>();
    private final Map<String, UserInformation> userInformationHolder = new ConcurrentHashMap<>();
    private transient final File file = new File(Paths.get("").toAbsolutePath()+File.separator+"data.json");

    public SystemFileAuthenticationDatabase(){
    }

    @Override
    public void save() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        Logger.debug("File Location: {}", file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("No save file found.");
            return;
        }

        try {
            StringBuilder stringBuilder = new StringBuilder();
            Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).forEach(stringBuilder::append);
            SystemFileAuthenticationDatabase loaded = gson.fromJson(stringBuilder.toString(), SystemFileAuthenticationDatabase.class);
            if(loaded==null)
                return;
            this.authInformationHolder.putAll(loaded.authInformationHolder);
            this.userInformationHolder.putAll(loaded.userInformationHolder);
            System.out.println("Loaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasUser(String user){
        return authInformationHolder.containsKey(user)&&userInformationHolder.containsKey(user);
    }
    @Override
    public UserInformation getUserInformation(String username) {
        return userInformationHolder.get(username);
    }

    @Override
    public Response verifyUser(String username, String password) {
        if(!hasUser(username)) return new Response("No user found!");
        if(authInformationHolder.get(username).getPassword().equals(password))
            return new Response();
        else return new Response("Wrong password");
    }

    @Override
    public Response updateUserInformation(String username, UserInformation userInformation) {
        if(!hasUser(username)) return new Response("No user found!");
        userInformationHolder.put(username, userInformation);
        save();
        return new Response();
    }

    @Override
    public Response availableUsername(String username) {
        if(!hasUser(username)) return new Response("Username already existed!");
        return new Response();
    }

    @Override
    public Response createUser(UserAuthInformation auth, UserInformation user) {
        if(hasUser(auth.getUsername())) return new Response("User already existed!");
        authInformationHolder.put(auth.getUsername(), auth);
        userInformationHolder.put(auth.getUsername(), user);
        save();
        return new Response();
    }

    @Override
    public Response deleteUser(String username) {
        if(!hasUser(username)) return new Response("No user found!");
        authInformationHolder.remove(username);
        userInformationHolder.remove(username);
        save();
        return new Response();
    }

    @Override
    public Response updateUserPassword(String username, String oldPassword, String newPassword) {
        if(!hasUser(username)) return new Response("No user found!");
        if(authInformationHolder.get(username).getPassword().equals(oldPassword)){
            authInformationHolder.put(username, new UserAuthInformation(username, newPassword));
            save();
            return new Response();
        }
        return new Response("Incorrect old password");
    }

}
