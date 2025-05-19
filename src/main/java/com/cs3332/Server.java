package com.cs3332;

import com.cs3332.data.DataManager;
import com.cs3332.handler.TestHandler;
import com.cs3332.handler.authentication.*;
import com.cs3332.handler.product.*;
import com.cs3332.handler.order.*;
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

        // Product/Inventory
        registerHandler("/item_stack_create", new CreateItemStackHandler(this));
        registerHandler("/item_stack_info", new ItemStackInfoHandler(this));
        registerHandler("/item_stack_list", new GetAllItemStacksHandler(this));
        registerHandler("/item_stack_delete", new DeleteItemStackHandler(this));
        registerHandler("/product_create", new CreateProductHandler(this));
        registerHandler("/product_info", new ProductInfoHandler(this));
        registerHandler("/product_list", new GetAllProductsHandler(this));
        registerHandler("/product_delete", new DeleteProductHandler(this));
        registerHandler("/item_import", new ImportItemHandler(this));
        registerHandler("/item_export", new ExportItemHandler(this));
        registerHandler("/item_list", new GetAllItemsHandler(this));
        registerHandler("/item_delete", new DeleteIOItemHandler(this));
        registerHandler("/item_info", new ItemInfoHandler(this));

        // Order Management
        registerHandler("/order_create", new CreateOrderHandler(this));
        registerHandler("/order_list", new OrderListHandler(this));
        registerHandler("/order_status_update", new UpdateOrderStatusHandler(this));

        // Product Categories
        registerHandler("/product_categories", new GetProductCategoriesHandler(this));
        registerHandler("/products_by_category", new GetProductsByCategoryHandler(this));

        // Cart Management
        registerHandler("/cart_add", new AddToCartHandler(this));
        registerHandler("/cart_remove", new RemoveFromCartHandler(this));
        registerHandler("/cart_update", new UpdateCartHandler(this));
        registerHandler("/cart_get", new GetCartHandler(this));
        registerHandler("/cart_checkout", new CheckoutCartHandler(this));
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
