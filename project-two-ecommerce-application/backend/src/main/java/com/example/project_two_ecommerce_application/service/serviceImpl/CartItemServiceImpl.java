package com.example.project_two_ecommerce_application.service.serviceImpl;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.repository.CartItemRepository;
import com.example.project_two_ecommerce_application.repository.ProductRepository;
import com.example.project_two_ecommerce_application.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartService {

     private CartItemRepository cartItemRepository;
     private ProductRepository productRepository;


    @Transactional(readOnly = true)
    @Override
    public List<CartItem> getCartItems(String identifier) {
        return cartItemRepository.findByIdentifier(identifier);
    }

    @Override
    public String getIdentifier(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String identifier = (String) session.getAttribute("identifier");

        if (identifier == null) {
            identifier = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            session.setAttribute("identifier", identifier);
        }

        return identifier;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CartItem> addItem(String identifier, int productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // To check for existing item, you need a method that gets ALL items by identifier
        // This assumes findByIdentifier returns List<CartItem>
        Optional<CartItem> existingItem = cartItemRepository.findByIdentifier(identifier).stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        CartItem itemToSave;

        if (existingItem.isPresent()) {
            // Update existing
            itemToSave = existingItem.get();
            itemToSave.setQuantity(itemToSave.getQuantity() + quantity);
        } else {
            // Add new
            itemToSave = new CartItem();
            itemToSave.setIdentifier(identifier);
            itemToSave.setProduct(product);
            itemToSave.setQuantity(quantity);
        }

        // Save the updated/new item
        cartItemRepository.save(itemToSave);

        // Force the transaction to commit changes immediately before the read query
        cartItemRepository.flush();

        // Return the FULL LIST of cart items for the given identifier
        // This list will now include the item you just saved/updated
        return cartItemRepository.findByIdentifier(identifier);
    }

    // Method to remove or update quantity
    @Override
    public void removeItem(int cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
