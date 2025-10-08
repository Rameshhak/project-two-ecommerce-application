import React from 'react'
import { useNavigate } from 'react-router-dom'
import { addToCart } from '../assets/js/api.js'
import { useCart } from '../context/CartProvider.jsx';

import '../assets/css/ProductCard.css'; 

const ProductCard = ({ product }) => {
    const navigate = useNavigate();
    const { refreshCart, setCartCount } = useCart(); 

    const handleAction = async (isBuyNow) => {
        try {
            const newResponse = await addToCart(product.id, 1);  // API call to Spring Boot
            
            if (isBuyNow) {
                navigate('/checkout'); // Redirect for Buy Now
            } else {
                // Stay on the same page and refresh the count
                refreshCart(newResponse.newCartCount); 
                alert(`${product.name} added to cart!`);
            }
        } catch (error) {
            alert('Failed to add item to cart. Please try again.');
        }
    };

    // Safely parse the price to a float, defaulting to 0 if null/bad data
    const safePrice = parseFloat(product.price.replace(',', '')) || 0; 

    return (
        <>
        <div className='product'>
            {/* <img src={product.imageUrl} alt={product.name} className="p-image" /> */}
            <div className='product_info'>
                <div className="p-details">
                <h3 className="p-name">{product.name}</h3>
                <p className="p-price"><strong>₹{safePrice.toFixed(2)}</strong></p>
                <div className="p-rating">⭐⭐⭐⭐⭐</div>
                <p className="product_description">{product.description.substring(0, 100)}...</p>
            </div>
            </div>
            
            <div className="product_buttons">
                 <button onClick={() => handleAction(false)} className='p-cart-btn'>Add to Cart</button>
                 <button onClick={() => handleAction(true)} className="p-buy-now-btn">Buy Now</button>
            </div>
        </div>
        </>
    );
};

export default ProductCard;