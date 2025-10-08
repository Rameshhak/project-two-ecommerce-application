package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
@Tag(name = "CartController",
        description = "Manage the cart products actions for adding products to cart, reading all cart products, and deleting products from cart.")
public class CartController {

    private CartService cartService;

    @Data
    public static class AddItemRequest {
        @Schema(description = "AddItemRequest productId")
        private int productId;
        @Schema(description = "AddItemRequest quantity")
        private int quantity;
    }

    @Data
    public static class CartUpdateResponse {
        @Schema(description = "CartUpdate message")
        private String message;
        @Schema(description = "CartUpdate Count")
        private int newCartCount;
    }

    // Helper to get a unique cart identifier (Use User ID if logged in, otherwise Session ID)
    private String getIdentifier(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @Operation(summary = "Display cart products REST API",
            description = "Get all cart products in the database by using the session ID.")
    @ApiResponse(responseCode = "200",
            description = "HTTP Status 200 OK")
    @GetMapping()
    public ResponseEntity<List<CartItem>> getCartItems(HttpServletRequest request, Authentication authentication) {
        String identifier;

        // Prefer userId if authenticated, otherwise fallback to session identifier
        if (authentication != null && authentication.isAuthenticated()) {
            identifier = authentication.getName(); // or extract userId from principal
        } else {
            identifier = cartService.getIdentifier(request);
        }

        List<CartItem> cartItems = cartService.getCartItems(identifier);
        System.out.println("Cart requested, identifier: " + identifier);
        System.out.println("Cart size: " + cartItems.size());

        return ResponseEntity.ok(cartItems);
    }


    @Operation(summary = "Create cart product REST API",
            description = "Add products to the cart page for the user whenever they collect the items to buy it. All products are stored in the database.")
    @ApiResponse(responseCode = "200",
            description = "HTTP Status 200 OK")
    @PostMapping("/add")
    public ResponseEntity<CartUpdateResponse> addItemToCart(
            @RequestBody AddItemRequest request,
            HttpServletRequest httpRequest, Authentication authentication) {

        // Use the authenticated user's name (or ID) if available, otherwise fallback to session
        String identifier;
        if (authentication != null && authentication.isAuthenticated()) {
            identifier = authentication.getName(); // e.g., "Mark"
        } else {
            identifier = cartService.getIdentifier(httpRequest); // Fallback to session ID
        }

        List<CartItem> updatedCartItems = cartService.addItem(
                identifier,
                request.getProductId(),
                request.getQuantity()
        );

        int totalCount = updatedCartItems.stream().mapToInt(CartItem::getQuantity).sum();

        CartUpdateResponse response = new CartUpdateResponse();
        response.setMessage("Item added successfully.");
        response.setNewCartCount(totalCount);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete cart product REST API",
            description = "Remove products from the cart page that are stored in the database.")
    @ApiResponse(responseCode = "200",
            description = "HTTP Status 200 OK")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") Integer cartItemId){
        cartService.removeItem(cartItemId);
        return new ResponseEntity<>("Cart item deleted successfully!!!", HttpStatus.OK);
    }

}

@Data
class CartRequest {
    @Schema(description = "CartRequest productID")
    private int productId;
    @Schema(description = "CartRequest quantity")
    private int quantity;

}


