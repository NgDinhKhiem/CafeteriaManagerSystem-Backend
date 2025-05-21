package com.cs3332.data.constructor;

import com.cs3332.core.utils.Response;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.data.object.storage.Product;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ProductionDBSource extends DBSource{
    // ---------- Production/Storage ----------
    ItemStack createItemStack(ItemStack itemStack);
    ItemStack getItemStack(UUID itemStackID);
    List<ItemStack> getAllItemStack();
    Response removeItemStack(UUID itemStackID);
    Product createProduct(Product product);
    Product getProduct(UUID productID);
    List<Product> getAllProduct();
    List<Item> getItemByID(UUID... itemIDs);
    String removeProduct(UUID productID);
    Response importItem(Item item);
    Response exportItem(Item item);
    List<Item> getAllItem();
    Response deleteIOItem(UUID entryID);
    Item getItemInfo(UUID ID);

    // ---------- Order ----------

    Order createOrder(Order order);

    Order getOrder(UUID orderID);

    List<Order> getAllOrders();

    List<Order> getOrders(@Nullable String tableID, @Nullable OrderStatus status, @Nullable Long from, @Nullable Long to);

    Response updateOrderInformation(UUID orderID, Order order);
}
