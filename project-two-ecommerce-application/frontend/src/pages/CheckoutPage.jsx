// src/pages/CheckoutPage.jsx

import React, { useState, useEffect } from 'react';
import { fetchCartItems } from '../assets/js/api.js';
import { useNavigate } from 'react-router-dom';

import '../assets/css/CheckoutPage.css';

const CheckoutPage = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    // Fetch the cart items to show the final summary
    useEffect(() => {
        const loadCart = async () => {
            try {
                const items = await fetchCartItems();
                setCartItems(items);
            } catch (error) {
                console.error("Could not load cart for checkout:", error);
                // Redirect if cart is empty or loading fails
                navigate('/cart'); 
            } finally {
                setLoading(false);
            }
        };
        loadCart();
    }, [navigate]);

    const subtotal = cartItems.reduce((acc, item) => {
        const priceString = String(item.product?.price || item.productPrice || '0');
        const price = parseFloat(priceString.replace(',', '')) || 0; 
        return acc + (price * item.quantity)
    }, 0);
    const shipping = 49.99; // fixed shipping cost

    const safeSubtotal = parseFloat(subtotal) || 0; 
    const totalAmount = safeSubtotal + shipping;

    const handlePlaceOrder = () => {
        alert(`Order placed successfully! Total: ₹${totalAmount.toFixed(2)}`);
        navigate('/order-confirmation'); 
    };

    if (loading) {
        return <div className="checkout-page-container">Loading Checkout Details...</div>;
    }
    
    if (cartItems.length === 0) {
        return <div className="checkout-page-container" style={{fontSize: '30px', textAlign: 'center'}}>Your cart is empty. Please add items to proceed.</div>;
    }

    return (
        <div className="checkout-page-container">
            <div className="checkout-shipping-info">
                <h2>1. Shipping Address</h2>
                {/* Simplified input fields for address simulation */}
                <p>Delivery to: **Joel Doe**, 123 Main St, Chennai, India</p>
                <button className="change-btn">Change Address</button>
                <hr/>

                <h2>2. Payment Method</h2>
                <p>Using: **Visa ending in XXXX**</p>
                <button className="change-btn">Change Payment</button>
            </div>

            <div className="checkout-summary-box">
                <h2 className="summary-title">Order Summary</h2>
                <div className="summary-details">
                    <p>Items Subtotal ({cartItems.length} items): <span>${safeSubtotal.toFixed(2)}</span></p>
                    <p>Shipping & Handling: <span>₹{shipping.toFixed(2)}</span></p>
                    <hr/>
                    <h3 className="order-total">Order Total: <span>₹{totalAmount.toFixed(2)}</span></h3>
                </div>

                <button 
                    onClick={handlePlaceOrder} 
                    className="place-order-btn"
                >
                    Place Your Order
                </button>
                <p className="agreement-text">
                    By placing your order, you agree to our privacy notice and conditions of use.
                </p>
            </div>
        </div>
    );
};

export default CheckoutPage;