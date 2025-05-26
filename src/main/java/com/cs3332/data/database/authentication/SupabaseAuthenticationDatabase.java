package com.cs3332.data.database.authentication;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.database.lib.supabase.JSONBuilder;
import com.cs3332.data.database.lib.supabase.SupabaseClient;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class SupabaseAuthenticationDatabase implements AuthenticationSource {
    public final SupabaseClient supabaseClient;
    private static final File configFile = new File(Paths.get("").toAbsolutePath()+File.separator+".env");
    private static final String AUTH_DATABASE = "user_auth_information";
    private static final String USER_INFO_DATABASE = "user_information";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SupabaseAuthenticationDatabase(){
        if(!configFile.exists())
            throw new RuntimeException("ERROR IN SETUP DATABASE");
        Properties env = getProperties();

        String URL = env.getProperty("SUPABASE_URL");
        String KEY = env.getProperty("SUPABASE_ANON_KEY");

        if (URL == null || KEY == null) {
            throw new RuntimeException("Supabase URL or Key not found in .env file");
        }

        supabaseClient = new SupabaseClient(URL, KEY);
        Logger.debug( "Connected To Supabase DataSource For Authentication!");
        initializeDataBase();
    }

    @NotNull
    private Properties getProperties() {
        Properties env = new Properties();

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // skip comments/empty
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    env.setProperty(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read .env file", e);
        }
        return env;
    }

    private void initializeDataBase(){
        Logger.debug( "Initialize Supabase Database Entries for Authentication!");
        //
    }

    @Override
    public UserInformation getUserInformation(String username) {
        JSONObject rs = supabaseClient.from(USER_INFO_DATABASE)
                .eq("username", username)
                .select("info")
                .single()
                .exec();
        return gson.fromJson((((JSONObject)rs.get("data")).get("info")).toString(),UserInformation.class);
    }

    @Override
    public Response verifyUser(String username, String password) {
        JSONObject rs = supabaseClient.from(AUTH_DATABASE).select("username")
                .eq("username", username)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new Response("Not found!");
        return new Response();
    }

    @Override
    public Response updateUserInformation(String username, UserInformation userInformation) {
        JSONObject rs = supabaseClient.from(USER_INFO_DATABASE).upsert(
                new JSONBuilder()
                        .pl("username",username)
                        .pl("info", gson.toJson(userInformation))
                        .build()).exec();
        if(rs.get("error")!=null){
            return new  Response("Username not found!");
        }
        return new Response();
    }

    @Override
    public Response availableUsername(String username) {
        return null;
    }

    @Override
    public Response createUser(UserAuthInformation auth, UserInformation user) {
        JSONObject rs = supabaseClient.from(AUTH_DATABASE).insert(JSONBuilder.toJSONObj(auth)).exec();
        if(rs.get("error")!=null){
            return new Response("Already exited username");
        }
        rs = supabaseClient.from(USER_INFO_DATABASE).upsert(
                new JSONBuilder()
                        .pl("username",user.getUsername())
                        .pl("info", gson.toJson(user))
                .build()).exec();
        if(rs.get("error")!=null){
            Logger.error(rs.toJSONString());
            rs = supabaseClient.from(AUTH_DATABASE).select("username").eq("username", auth.getUsername()).delete().exec();
            if(rs.get("error")!=null){
                Logger.warn("DELETE USER INFO: "+rs.get("error"));
            }
            return new Response("Error");
        }
        return new Response();
    }

    @Override
    public Response deleteUser(String username) {
        JSONObject rs = supabaseClient.from(AUTH_DATABASE).eq("username", username).delete().exec();
        if(rs.get("error")!=null){
            return new Response("Invalid username");
        }
        rs = supabaseClient.from(USER_INFO_DATABASE).eq("username", username).delete().exec();
        if(rs.get("error")!=null){
            return new Response("Invalid username");
        }
        return new Response();
    }

    @Override
    public Response updateUserPassword(String username, String oldPassword, String newPassword) {
        JSONObject rs = supabaseClient.from(AUTH_DATABASE)
                .eq("username", username)
                .eq("password", oldPassword)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new Response("Not found!");
        rs = supabaseClient.from(AUTH_DATABASE).upsert(
                new JSONBuilder()
                        .pl("username",username)
                        .pl("password", newPassword)
                        .build()).exec();
        if(rs.get("error")!=null){
            return new Response("Error");
        }
        return new Response();
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }
}
