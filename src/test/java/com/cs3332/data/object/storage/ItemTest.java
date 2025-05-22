package com.cs3332.data.object.storage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    @Test
    void testSumImportExportQuantity() {
        Item import1 = new Item(UUID.randomUUID(), UUID.randomUUID(), 0L, 0L, 10.0f, "SupplierA", "Import");
        Item export1 = new Item(import1.getItemStackID(), UUID.randomUUID(), 0L, 0L, -3.0f, "SupplierA", "Export");
        Item import2 = new Item(import1.getItemStackID(), UUID.randomUUID(), 0L, 0L, 5.0f, "SupplierA", "Import");
        List<Item> items = List.of(import1, export1, import2);
        float sum = (float) items.stream().mapToDouble(Item::getQuantity).sum();
        assertEquals(12.0f, sum, 0.0001);
    }

    @Test
    void testSumImportExportQuantityForMultipleStacks() {
        UUID stack1 = UUID.randomUUID();
        UUID stack2 = UUID.randomUUID();
        Item i1 = new Item(stack1, UUID.randomUUID(), 0L, 0L, 10.0f, "SupplierA", "Import");
        Item i2 = new Item(stack1, UUID.randomUUID(), 0L, 0L, -2.0f, "SupplierA", "Export");
        Item i3 = new Item(stack2, UUID.randomUUID(), 0L, 0L, 7.0f, "SupplierB", "Import");
        Item i4 = new Item(stack2, UUID.randomUUID(), 0L, 0L, -1.0f, "SupplierB", "Export");
        List<Item> items = List.of(i1, i2, i3, i4);
        float sumStack1 = (float) items.stream().filter(it -> it.getItemStackID().equals(stack1)).mapToDouble(Item::getQuantity).sum();
        float sumStack2 = (float) items.stream().filter(it -> it.getItemStackID().equals(stack2)).mapToDouble(Item::getQuantity).sum();
        assertEquals(8.0f, sumStack1, 0.0001);
        assertEquals(6.0f, sumStack2, 0.0001);
    }

    @Test
    void testSumOnlyExports() {
        UUID stack = UUID.randomUUID();
        Item e1 = new Item(stack, UUID.randomUUID(), 0L, 0L, -5.0f, "SupplierA", "Export");
        Item e2 = new Item(stack, UUID.randomUUID(), 0L, 0L, -2.0f, "SupplierA", "Export");
        List<Item> items = List.of(e1, e2);
        float sum = (float) items.stream().mapToDouble(Item::getQuantity).sum();
        assertEquals(-7.0f, sum, 0.0001);
    }

    @Test
    void testSumOnlyImports() {
        UUID stack = UUID.randomUUID();
        Item i1 = new Item(stack, UUID.randomUUID(), 0L, 0L, 4.0f, "SupplierA", "Import");
        Item i2 = new Item(stack, UUID.randomUUID(), 0L, 0L, 6.0f, "SupplierA", "Import");
        List<Item> items = List.of(i1, i2);
        float sum = (float) items.stream().mapToDouble(Item::getQuantity).sum();
        assertEquals(10.0f, sum, 0.0001);
    }

    @Test
    void testSumZeroQuantities() {
        UUID stack = UUID.randomUUID();
        Item i1 = new Item(stack, UUID.randomUUID(), 0L, 0L, 0.0f, "SupplierA", "Import");
        Item i2 = new Item(stack, UUID.randomUUID(), 0L, 0L, 0.0f, "SupplierA", "Import");
        List<Item> items = List.of(i1, i2);
        float sum = (float) items.stream().mapToDouble(Item::getQuantity).sum();
        assertEquals(0.0f, sum, 0.0001);
    }

    @Test
    void testSumBySupplier() {
        UUID stack = UUID.randomUUID();
        Item i1 = new Item(stack, UUID.randomUUID(), 0L, 0L, 5.0f, "SupplierA", "Import");
        Item i2 = new Item(stack, UUID.randomUUID(), 0L, 0L, 3.0f, "SupplierB", "Import");
        Item i3 = new Item(stack, UUID.randomUUID(), 0L, 0L, -2.0f, "SupplierA", "Export");
        List<Item> items = List.of(i1, i2, i3);
        float sumA = (float) items.stream().filter(it -> it.getSupplier().equals("SupplierA")).mapToDouble(Item::getQuantity).sum();
        float sumB = (float) items.stream().filter(it -> it.getSupplier().equals("SupplierB")).mapToDouble(Item::getQuantity).sum();
        assertEquals(3.0f, sumA, 0.0001);
        assertEquals(3.0f, sumB, 0.0001);
    }
} 