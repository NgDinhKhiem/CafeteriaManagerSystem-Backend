package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.ProductInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.IngredientResponse;
import com.cs3332.core.response.object.product.ProductResponse;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductInfoHandler extends AbstractBodyHandler<ProductInfoPayload> {
    private String token;
    public ProductInfoHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getProductID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Product ID is required."));
        }
        Product product = server.getDataManager().getProductionDBSource().getProduct(payload.getProductID());
        if (product != null) {
            List<IngredientResponse> ingredientResponses = product.getRecipe().stream()
                    .map(ing -> {
                        ItemStack itemStack = server.getDataManager().getProductionDBSource().getItemStack(ing.getItemStackID());
                        return new IngredientResponse
                                (ing.getItemStackID(), itemStack.getName(), itemStack.getUnit(), ing.getQuantity());
                    })
                    .collect(Collectors.toList());
            
            // Calculate available product count based on ingredients
            int availableCount = calculateAvailableProductCount(product);
            
            return new ServerResponse(ResponseCode.FOUND, new ProductResponse(
                    product.getID(),
                    product.getName(),
                    product.getUnit(),
                    product.getPrice(),
                    ingredientResponses,
                    availableCount
            ));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Product not found"));
        }
    }
    
    /**
     * Calculates how many products can be made based on available ingredients
     * @param product The product to calculate for
     * @return The maximum number of products that can be made
     */
    private int calculateAvailableProductCount(Product product) {
        if (product.getRecipe() == null || product.getRecipe().isEmpty()) {
            return Integer.MAX_VALUE; // No ingredients needed
        }
        
        // Get available ingredient quantities
        Map<UUID, Float> availableIngredients = new HashMap<>();
        for (Item item : server.getDataManager().getProductionDBSource().getAllItem()) {
            UUID itemStackId = item.getItemStackID();
            availableIngredients.put(
                itemStackId,
                availableIngredients.getOrDefault(itemStackId, 0f) + item.getQuantity()
            );
        }
        
        // Calculate maximum count for each ingredient
        int minAvailable = Integer.MAX_VALUE;
        for (Ingredient ingredient : product.getRecipe()) {
            UUID itemStackId = ingredient.getItemStackID();
            float available = availableIngredients.getOrDefault(itemStackId, 0f);
            float required = ingredient.getQuantity();
            
            // Skip if required amount is zero (shouldn't happen, but just in case)
            if (required <= 0) continue;
            
            // Calculate how many products can be made with this ingredient
            int count = (int) Math.floor(available / required);
            
            // Update minimum available count
            minAvailable = Math.min(minAvailable, count);
        }
        
        return minAvailable;
    }
}