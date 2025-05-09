package com.cs3332.data.constructor;

import com.cs3332.core.utils.Response;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.data.object.storage.Product;

import java.util.List;
import java.util.UUID;

public interface ProductionDBSource extends DBSource{
    ItemStack createItemStack(ItemStack itemStack);
    ItemStack getItemStack(UUID itemStackID);
    List<ItemStack> getAllItemStack();
    Response removeItemStack(UUID itemStackID);
    Product createProduct(Product product);
    Product getProduct(UUID productID);
    List<Product> getAllProduct();
    String removeProduct(UUID productID);
    Response importItem(Item item);
    Response exportItem(Item item);
    List<Item> getAllItem();
    Response deleteIOItem(UUID entryID);
    Item getItemInfo(UUID ID);
}
