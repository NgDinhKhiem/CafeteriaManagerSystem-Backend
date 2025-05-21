package com.cs3332.core.response.object.product;

import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ItemResponse extends AbstractResponse {
    private final UUID id;
    private final UUID itemStackID;
    private final String ItemStackName;
    private final String unit;
    private final long import_export_time;
    private final long expiration_date;
    private final double quantity;
    private final String reason;
    private final String supplier;
}
