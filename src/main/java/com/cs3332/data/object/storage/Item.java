package com.cs3332.data.object.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Item {
    private final UUID itemStackID;
    private final UUID entryID;
    private final long importExportDate;
    private final long expiration_date;
    private final float quantity;
    private final String supplier;
}

