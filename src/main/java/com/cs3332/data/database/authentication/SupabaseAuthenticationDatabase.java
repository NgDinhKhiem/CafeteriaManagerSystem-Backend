package com.cs3332.data.database.authentication;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.database.lib.supabase.JSONBuilder;
import com.cs3332.data.database.lib.supabase.SupabaseClient;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;
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
    private transient final File configFile = new File(Paths.get("").toAbsolutePath()+File.separator+".env");
    private static final String AUTH_DATABASE = "user_auth_information";
    private static final String USER_INFO_DATABASE = "user_information";

    public SupabaseAuthenticationDatabase(){
        if(!configFile.exists())
            throw new RuntimeException("ERROR IN SETUP DATABASE");
        Properties env = getProperties();

        String URL = env.getProperty("NEXT_PUBLIC_SUPABASE_URL");
        String KEY = env.getProperty("NEXT_PUBLIC_SUPABASE_ANON_KEY");

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
        return null;
    }

    @Override
    public Response verifyUser(String username, String password) {
        JSONObject rs = supabaseClient.from(AUTH_DATABASE).select("username, password")
                .eq("username", username)
                .eq("password", password)
                .exec();
        Logger.debug(rs.toJSONString());
        return new Response();
    }

    @Override
    public Response updateUserInformation(String username, UserInformation userInformation) {
        return null;
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
        rs = supabaseClient.from(USER_INFO_DATABASE).upsert(JSONBuilder.toJSONObj(user)).exec();
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
        return null;
    }

    @Override
    public Response updateUserPassword(String username, String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }
}
