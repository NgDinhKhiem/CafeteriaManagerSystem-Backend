package com.cs3332.data.database.product;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.AuthenticationSource;
import com.cs3332.data.constructor.ProductionDBSource;
import com.cs3332.data.database.lib.supabase.JSONBuilder;
import com.cs3332.data.database.lib.supabase.SupabaseClient;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.data.object.storage.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SupabaseProductionDatabase implements ProductionDBSource {
    public final SupabaseClient supabaseClient;
    private static final File configFile = new File(Paths.get("").toAbsolutePath()+File.separator+".env");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SupabaseProductionDatabase(){
        if(!configFile.exists())
            throw new RuntimeException("ERROR IN SETUP DATABASE");
        Properties env = getProperties();

        String URL = env.getProperty("SUPABASE_URL");
        String KEY = env.getProperty("SUPABASE_ANON_KEY");

        if (URL == null || KEY == null) {
            throw new RuntimeException("Supabase URL or Key not found in .env file");
        }

        supabaseClient = new SupabaseClient(URL, KEY);
        Logger.debug( "Connected To Supabase DataSource For Production Datas!");
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

    private static final String TABLE_ITEM_STACK = "item_stacks";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_ITEM = "items";
    private static final String TABLE_ORDER = "orders";

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }

    // ---------- ItemStack ----------

    @Override
    public ItemStack createItemStack(ItemStack itemStack) {
         /*= supabaseClient.from(TABLE_ITEM_STACK).select("id")
                .eq("id", itemStack.getID().toString())
                .exec();
        if(!rs.toJSONString().contains("[]")) {
            Logger.debug(rs.toJSONString());
            return null;
        }*/
        JSONObject rs = supabaseClient.from(TABLE_ITEM_STACK).upsert(
                new JSONBuilder()
                        .pl("id",itemStack.getID().toString())
                        .pl("data", gson.toJson(itemStack))
                        .build()).exec();
        if(rs.get("error")!=null){
            Logger.debug(rs.toJSONString());
            return null;
        }
        return itemStack;
    }

    @Override
    public ItemStack getItemStack(UUID itemStackID) {
        JSONObject rs = supabaseClient.from(TABLE_ITEM_STACK)
                .eq("id", itemStackID.toString())
                .single()
                .exec();
        if(rs.toJSONString().contains("data\":{}")
                ||rs.toJSONString().contains("data\":null")){
            Logger.debug(rs.toJSONString());
            return null;
        }

        return gson.fromJson(
                (((JSONObject)rs.get("data")).get("data")).toString(),
                ItemStack.class
        );
    }

    @Override
    public List<ItemStack> getAllItemStack() {
        JSONObject rs = supabaseClient.from(TABLE_ITEM_STACK)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new ArrayList<>();
        List<ItemStack> itemStackList = new ArrayList<>();
        for (Object o : ((JSONArray) rs.get("data"))) {
            JSONObject object = (JSONObject) o;
            itemStackList.add(
                    gson.fromJson(
                            (object.get("data")).toString(),
                            ItemStack.class
                    )
            );
        }
        return itemStackList;
    }

    @Override
    public Response removeItemStack(UUID itemStackID) {
        JSONObject rs = supabaseClient.from(TABLE_ITEM_STACK)
                .eq("id", itemStackID.toString()).delete().exec();
        if(rs.get("error")!=null){
            return new Response("Invalid itemStack ID");
        }
        return new Response();
    }

    // ---------- Product ----------

    @Override
    public Product createProduct(Product product) {
        JSONObject rs = supabaseClient.from(TABLE_PRODUCT).upsert(
                new JSONBuilder()
                        .pl("id",product.getID().toString())
                        .pl("data", gson.toJson(product))
                        .build()).exec();
        if(rs.get("error")!=null){
            Logger.debug(rs.toJSONString());
            return null;
        }
        return product;
    }

    @Override
    public Product getProduct(UUID productID) {
        JSONObject rs = supabaseClient.from(TABLE_PRODUCT)
                .eq("id", productID.toString())
                .single()
                .exec();
        if(rs.toJSONString().contains("data\":{}")
                ||rs.toJSONString().contains("data\":null"))
            return null;
        return gson.fromJson(
                (((JSONObject)rs.get("data")).get("data")).toString(),
                Product.class
        );
    }

    @Override
    public List<Product> getAllProduct() {
        JSONObject rs = supabaseClient.from(TABLE_PRODUCT)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new ArrayList<>();

        List<Product> itemStackList = new ArrayList<>();
        for (Object o : ((JSONArray) rs.get("data"))) {
            JSONObject object = (JSONObject) o;
            itemStackList.add(
                    gson.fromJson(
                            (object.get("data")).toString(),
                            Product.class
                    )
            );
        }

        return itemStackList;
    }

    @Override
    public List<Item> getItemByID(UUID... itemIDs) {
        List<UUID> filter = Arrays.asList(itemIDs);
        List<Item> stash = new ArrayList<>();
        getAllItem().forEach(item -> {if(filter.contains(item.getItemStackID())) stash.add(item);});
        return stash;
    }

    @Override
    public String removeProduct(UUID productID) {
        JSONObject rs = supabaseClient.from(TABLE_PRODUCT)
                .eq("id", productID.toString()).delete().exec();
        if(rs.get("error")!=null){
            return ("Invalid itemStack ID");
        }
        return null;
    }

    // ---------- Item ----------

    @Override
    public Response importItem(Item item) {
        if(getItemStack(item.getItemStackID())==null){
            return new Response("Associated ItemStack does not exist.");
        }

        JSONObject rs = supabaseClient.from(TABLE_ITEM).upsert(
                new JSONBuilder()
                        .pl("id",item.getEntryID().toString())
                        .pl("data", gson.toJson(item))
                        .build()).exec();
        if(rs.get("error")!=null){
            return new Response("Duplicated Item ID");
        }
        return new Response();
    }

    @Override
    public Response exportItem(Item item) {
        if(getItemStack(item.getItemStackID())==null){
            return new Response("Associated ItemStack does not exist.");
        }

        JSONObject rs = supabaseClient.from(TABLE_ITEM).upsert(
                new JSONBuilder()
                        .pl("id",item.getEntryID().toString())
                        .pl("data", gson.toJson(item))
                        .build()).exec();
        if(rs.get("error")!=null){
            return new Response("Duplicated Item ID");
        }
        return new Response();
    }

    @Override
    public List<Item> getAllItem() {
        JSONObject rs = supabaseClient.from(TABLE_ITEM)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new ArrayList<>();
        List<Item> itemStackList = new ArrayList<>();
        for (Object o : ((JSONArray) rs.get("data"))) {
            JSONObject object = (JSONObject) o;
            itemStackList.add(
                    gson.fromJson(
                            (object.get("data")).toString(),
                            Item.class
                    )
            );
        }
        return itemStackList;
    }

    @Override
    public Response deleteIOItem(UUID entryID) {
        JSONObject rs = supabaseClient.from(TABLE_ITEM)
                .eq("id", entryID.toString()).delete().exec();
        if(rs.get("error")!=null){
            return new Response("Invalid Item ID");
        }
        return new Response();
    }

    @Override
    public Item getItemInfo(UUID ID) {
        JSONObject rs = supabaseClient.from(TABLE_ITEM)
                .eq("id", ID.toString())
                .single()
                .exec();
        if(rs.toJSONString().contains("data\":{}")
                ||rs.toJSONString().contains("data\":null"))
            return null;
        return gson.fromJson(
                (((JSONObject)rs.get("data")).get("data")).toString(),
                Item.class
        );
    }

    // ---------- Order ----------

    @Override
    public Order createOrder(Order order) {
        List<UUID> products = getAllProduct().stream().map(Product::getID).toList();
        for (OrderItem item : order.getItems()) {
            if (!products.contains(item.getProductID())) {
                Logger.warn("Product with ID {} not found while creating order {}.", item.getProductID(), order.getOrderID());
                return null;
            }
        }

        JSONObject rs = supabaseClient.from(TABLE_ORDER).upsert(
                new JSONBuilder()
                        .pl("id",order.getOrderID().toString())
                        .pl("data", gson.toJson(order))
                        .build()).exec();

        if(rs.get("error")!=null){
            return null;
        }

        return order;
    }

    @Override
    public Order getOrder(UUID orderID) {
        JSONObject rs = supabaseClient.from(TABLE_ORDER)
                .eq("id", orderID.toString())
                .single()
                .exec();
        if(rs.toJSONString().contains("data\":{}")
                ||rs.toJSONString().contains("data\":null"))
            return null;

        return gson.fromJson(
                (((JSONObject)rs.get("data")).get("data")).toString(),
                Order.class
        );
    }

    @Override
    public List<Order> getAllOrders() {
        JSONObject rs = supabaseClient.from(TABLE_ORDER)
                .exec();
        if(rs.toJSONString().contains("[]"))
            return new ArrayList<>();

        List<Order> itemStackList = new ArrayList<>();
        for (Object o : ((JSONArray) rs.get("data"))) {
            JSONObject object = (JSONObject) o;
            itemStackList.add(
                    gson.fromJson(
                            (object.get("data")).toString(),
                            Order.class
                    )
            );
        }

        return itemStackList;
    }

    @Override
    public List<Order> getOrders(@Nullable String tableID, @Nullable OrderStatus status, @Nullable Long from, @Nullable Long to) {
        List<Order> or = getAllOrders();
        if(tableID!=null)
            or.removeIf(s-> !Objects.equals(tableID, s.getTableID()));
        if(status!=null)
            or.removeIf(s-> !Objects.equals(status, s.getStatus()));
        if(from!=null)
            or.removeIf(s-> s.getOrderTimestamp()<from);
        if(to!=null)
            or.removeIf(s-> s.getOrderTimestamp()>to);
        return or.stream().map(this::updateOrder).toList();
    }

    Order updateOrder(Order order) {
        List<OrderItem> orderItems = order.getItems();
        order.setItems(orderItems);
        return order;
    }

    @Override
    public Response updateOrderInformation(UUID orderID, Order order) {
        if(getOrder(orderID)==null)
            return new Response("Order not found!");
        JSONObject rs = supabaseClient.from(TABLE_ORDER).upsert(
                new JSONBuilder()
                        .pl("id",orderID)
                        .pl("data", gson.toJson(order))
                        .build()).exec();
        if(rs.get("error")!=null){
            return new Response("Error");
        }
        return new Response();
    }
}
