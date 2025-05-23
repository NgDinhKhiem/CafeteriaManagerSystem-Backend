package com.cs3332.data.database.product;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.ProductionDBSource;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.data.object.storage.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SystemFileProductionDB implements ProductionDBSource {
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private transient final File file = new File(Paths.get("").toAbsolutePath() + File.separator + "production-data.json");

    private final Map<UUID, ItemStack> itemStacks = new ConcurrentHashMap<>();
    private final Map<UUID, Product> products = new ConcurrentHashMap<>();
    private final Map<UUID, Item> items = new ConcurrentHashMap<>();
    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    @Override
    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        Logger.debug("File Location: {}", file.getAbsolutePath());
        if (!file.exists()) {
            Logger.debug("No save file found nothing to load!");
            return;
        }

        try {
            StringBuilder stringBuilder = new StringBuilder();
            Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).forEach(stringBuilder::append);
            SystemFileProductionDB loaded = gson.fromJson(stringBuilder.toString(), SystemFileProductionDB.class);
            if (loaded == null) return;

            this.itemStacks.clear();
            this.itemStacks.putAll(loaded.itemStacks);
            this.products.clear();
            this.products.putAll(loaded.products);
            this.items.clear();
            this.items.putAll(loaded.items);
            this.orders.clear();
            this.orders.putAll(loaded.orders);

            Logger.debug("Loaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- ItemStack ----------

    @Override
    public ItemStack createItemStack(ItemStack itemStack) {
        if (itemStacks.containsKey(itemStack.getID())) return null;
        itemStacks.put(itemStack.getID(), itemStack);
        save();
        return itemStack;
    }

    @Override
    public ItemStack getItemStack(UUID itemStackID) {
        return itemStacks.get(itemStackID);
    }

    @Override
    public List<ItemStack> getAllItemStack() {
        return new ArrayList<>(itemStacks.values());
    }

    @Override
    public Response removeItemStack(UUID itemStackID) {
        if (!itemStacks.containsKey(itemStackID)) return new Response("ItemStack not found.");
        itemStacks.remove(itemStackID);
        save();
        return new Response();
    }

    // ---------- Product ----------

    @Override
    public Product createProduct(Product product) {
        if (products.containsKey(product.getID())) return null;

        for (Ingredient ingredient : product.getRecipe()) {
            if (!itemStacks.containsKey(ingredient.getItemStackID())) {
                return null;
            }
        }

        products.put(product.getID(), product);
        save();
        return product;
    }

    @Override
    public Product getProduct(UUID productID) {
        return products.get(productID);
    }

    @Override
    public List<Product> getAllProduct() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Item> getItemByID(UUID... itemIDs) {
        List<UUID> filter = Arrays.asList(itemIDs);
        List<Item> stash = new ArrayList<>();
        this.items.values().forEach(item -> {if(filter.contains(item.getItemStackID())) stash.add(item);});
        return stash;
    }

    @Override
    public String removeProduct(UUID productID) {
        if (!products.containsKey(productID)) return "Product not found.";
        products.remove(productID);
        save();
        return null;
    }

    // ---------- Item ----------

    @Override
    public Response importItem(Item item) {
        if (!itemStacks.containsKey(item.getItemStackID()))
            return new Response("Associated ItemStack does not exist.");
        if (items.containsKey(item.getEntryID()))
            return new Response("Item with this entry ID already exists.");

        items.put(item.getEntryID(), item);
        save();
        return new Response();
    }

    @Override
    public Response exportItem(Item item) {
        if (!itemStacks.containsKey(item.getItemStackID()))
            return new Response("Associated ItemStack does not exist.");
        if (items.containsKey(item.getEntryID()))
            return new Response("Item with this entry ID already exists.");

        items.put(item.getEntryID(), item);
        save();
        return new Response();
    }

    @Override
    public List<Item> getAllItem() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Response deleteIOItem(UUID entryID) {
        if (!items.containsKey(entryID)) return new Response("Item not found.");
        items.remove(entryID);
        save();
        return new Response();
    }

    @Override
    public Item getItemInfo(UUID ID) {
        return items.get(ID);
    }

    // ---------- Order ----------

    @Override
    public Order createOrder(Order order) {
        if (orders.containsKey(order.getOrderID())) return null;
        for (OrderItem item : order.getItems()) {
            if (!products.containsKey(item.getProductID())) {
                Logger.warn("Product with ID {} not found while creating order {}.", item.getProductID(), order.getOrderID());
                return null;
            }
        }
        orders.put(order.getOrderID(), order);
        save();
        return order;
    }

    @Override
    public Order getOrder(UUID orderID) {
        return orders.get(orderID);
    }

    @Override
    public List<Order> getAllOrders() {
        return orders.values().stream().map(this::updateOrder).collect(Collectors.toList());
    }

    @Override
    public List<Order> getOrders(@Nullable String tableID, @Nullable OrderStatus status, @Nullable Long from, @Nullable Long to) {
        List<Order> or = new ArrayList<>(orders.values());
        if(tableID!=null)
            or.removeIf(s-> !Objects.equals(tableID, s.getTableID()));
        if(status!=null)
            or.removeIf(s-> !Objects.equals(status, s.getStatus()));
        if(from!=null)
            or.removeIf(s-> s.getOrderTimestamp()<from);
        if(to!=null)
            or.removeIf(s-> s.getOrderTimestamp()>to);
        return new ArrayList<>(orders.values()).stream().map(this::updateOrder).toList();
    }

    Order updateOrder(Order order) {
        List<OrderItem> orderItems = order.getItems();
        for (OrderItem orderItem : orderItems) {
            Product product = products.get(orderItem.getProductID());
            if(product!=null) {
                orderItem.setProductName(product.getName());
                orderItem.setUnit(product.getUnit());
            }
        }
        order.setItems(orderItems);
        return order;
    }

    @Override
    public Response updateOrderInformation(UUID orderID, Order order) {
        if(!orders.containsKey(orderID))
            return new Response("Order not found!");
        orders.put(orderID, order);
        save();
        return new Response();
    }
}
