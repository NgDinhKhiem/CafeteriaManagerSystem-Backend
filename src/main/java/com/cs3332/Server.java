package com.cs3332;

import com.cs3332.data.DataManager;
import com.cs3332.handler.authentication.*;
import com.cs3332.handler.TestHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final HttpServer server;
    private boolean isStarted = false;
    @Getter
    private final DataManager dataManager = new DataManager(this);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Server(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port),0);
        this.server.setExecutor(executorService);
        this.registerContexts();
    }
    private void registerContexts(){
        registerHandler("/test", new TestHandler(this));
        registerHandler("/login", new LoginHandler(this));
        registerHandler("/user_info", new UserInformationHandler(this));
        registerHandler("/user_info_update", new UpdateInformationHandler(this));
        registerHandler("/user_create", new RegisterHandler(this));
        registerHandler("/user_delete", new DeleteUserHandler(this));
        registerHandler("/user_password_update", new UpdatePasswordHandler(this));
    }

    private void registerHandler(String route, HttpHandler handler){
        this.server.createContext(route, handler);
    }

    public void start(){
        if(isStarted)
            return;
        server.start();
        System.out.println("Server started on port "+port);
        this.dataManager.load();
        isStarted = true;
    }

    public void stop() {
        if (!isStarted) {
            return;
        }
        server.stop(0);
        executorService.shutdown(); // Shut down the executor
        System.out.println("Server stopped");
        isStarted = false;
    }
    public static void main(String[] args){
        try {
            final int PORT = 8080;
            Server server = new Server(PORT);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
