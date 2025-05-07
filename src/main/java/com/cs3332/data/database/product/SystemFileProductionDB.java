package com.cs3332.data.database.product;

import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.data.constructor.ProductionDBSource;
import com.cs3332.data.object.storage.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SystemFileProductionDB implements ProductionDBSource {
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private transient final File file = new File(Paths.get("").toAbsolutePath() + File.separator + "production-data.json");

    private final Map<UUID, ItemStack> itemStacks = new ConcurrentHashMap<>();
    private final Map<UUID, Product> products = new ConcurrentHashMap<>();
    private final Map<UUID, Item> items = new ConcurrentHashMap<>();

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
            System.out.println("No save file found.");
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

            System.out.println("Loaded successfully.");
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
}
