import React, { useState, useEffect } from 'react';
import { fetchCartItems, deleteCartItem } from '../assets/js/api.js';
import { useNavigate } from 'react-router-dom';
import '../assets/css/CartPage.css';

function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const loadCart = async () => {
            setLoading(true);
            setError("");
            try {
                const items = await fetchCartItems();
                if(items){
                  /* console.log("Cart data:", items)*/
                   setCartItems(items);
                }
                else {
                setCartItems([]);
                }
            } catch (err) {
                console.error("Could not load cart:", err);
                setError(err.message || "Failed to load cart");
                setCartItems([]);

                // Optional: redirect to login on 403
                if (err.message.includes("Access denied")) {
                    navigate("/login");
                }
            } finally {
                setLoading(false);
            }
        };
        loadCart();
    }, [navigate]);

       const subtotal = (cartItems || []).reduce((acc, item) => {
        const price = parseFloat(item.product?.price?.replace(',', '') || '0') || 0;
        return acc + price * item.quantity;
    }, 0);

    const handleProceedToBuy = () => {
        if (cartItems.length > 0) navigate('/checkout');
    };

    const handleRemoveItem = async (cartItemId) => {
        try {
            await deleteCartItem(cartItemId);
            setCartItems(prev => prev.filter(item => item.id !== cartItemId));
        } catch (err) {
            console.error("Failed to remove item:", err);
            alert(err.message || "Failed to delete item. Please check your connection.");
            if (err.message.includes("Access denied")) {
                navigate("/login");
            }
        }
    };

    if (loading) return <div className="cart-page-container">Loading Cart...</div>;
    if (error) return <div className="cart-page-container" style={{ color: "red" }}>{error}</div>;
    if (!cartItems || cartItems.length === 0) {
    return <div>Your cart is empty!</div>;
    }

    return (
        <div className="cart-page-container">
            <div className="cart-main">
                <h1 className="cart-title">Shopping Cart</h1>
                <div className="cart-items-list">
                    {cartItems.map((item) => {
                            const price = parseFloat(item.product?.price?.replace(',', '') || '0') || 0;
                            const itemTotal = price * item.quantity;
                            return (
                                <div key={item.id} className="cart-item-card">
                                    <div className="cart-item-details">
                                        <p className="cart-item-name">{item.product?.name}</p>
                                        <p className="cart-item-price">₹{price.toFixed(2)}</p>
                                        <p className="cart-item-description">{item.product?.description}</p>
                                        <div className="cart-item-actions">
                                            <label>Qty: {item.quantity}</label>
                                            <span className="cart-item-separator">|</span>
                                            <button onClick={() => handleRemoveItem(item.id)} className="cart-action-btn">Delete</button>
                                        </div>
                                        <p className="cart-item-subtotal">Total: ₹{itemTotal.toFixed(2)}</p>
                                    </div>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
            <div className="cart-summary">
                <div className="cart-subtotal-box">
                    <p className="subtotal-text">
                        Subtotal ({cartItems.length} items): <span className="subtotal-amount">₹{subtotal.toFixed(2)}</span>
                    </p>
                    <button onClick={handleProceedToBuy} className="proceed-btn" disabled={cartItems.length === 0}>
                        Proceed to Buy ({cartItems.length})
                    </button>
                </div>
            </div>
        </div>
    );
}

export default CartPage;
