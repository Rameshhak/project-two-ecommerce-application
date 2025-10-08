import React from 'react'
import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { fetchCartCount } from '../assets/js/api.js';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

function CartProvider({ children }) {

    const [cartCount, setCartCount] = useState(0);

    const refreshCart = useCallback(async () => {
        try {
            const count = await fetchCartCount();
            setCartCount(count);
        } catch (error) {
            console.error("Failed to refresh cart count:", error);
            setCartCount(0); // Reset or handle error state
        }
    }, []);

    // Load initial cart count when the app starts
    useEffect(() => {
        refreshCart();
    }, [refreshCart]);

  return (

    <CartContext.Provider value={{ cartCount, refreshCart, setCartCount }}>
            {children}
        </CartContext.Provider>
  );
}

export default CartProvider
