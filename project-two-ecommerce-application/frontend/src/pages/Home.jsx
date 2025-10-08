import React from 'react'
import home_img from '../assets/images/home_background_img.jpg'
import ProductCard from './ProductCard.jsx';
import Header from '../components/layout/Header.jsx'
import { fetchProducts, fetchCart } from '../assets/js/api.js'; 
import { useState, useEffect } from 'react';

import '../assets/css/Home.css'

function Home() {

  const [products, setProducts] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [cartCount, setCartCount] = useState(0);

    // --- Data Fetching Logic ---
    const loadProducts = async (query = '') => {
        try {
            // API Call: GET /api/product?q={query}
            const response = await fetchProducts(query);
            // This assumes your Spring Boot API returns a List<Product> in response.data
            setProducts(response.data); 
        } catch (error) {
            console.error("Error fetching products:", error);
            setProducts([]); 
        }
    };
    
    const loadCartCount = async () => {
        try {
            // API Call: GET /api/cart
            const response = await fetchCart();
            // Calculate total quantity from cart items
            const count = response.data.reduce((sum, item) => sum + item.quantity, 0);
            setCartCount(count);
        } catch (error) {
            console.error("Error fetching cart:", error);
            setCartCount(0);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault(); 
        loadProducts(searchQuery); 
    };

    // Load initial data on component mount
    useEffect(() => {
        loadProducts(); 
        loadCartCount();
    }, []);


  return (
    <>
       {/* Pass state and handlers to Header */}
        <Header searchQuery={searchQuery} setSearchQuery={setSearchQuery} handleSearch={handleSearch} cartCount={cartCount} />
        <div className='home'>
          <img src={home_img} className='home_img' alt='Home Background' />
           
           {/* Placeholder for the main product content area */}
           <div className='home_product_row'>
                {products.length > 0 ? (
                    // Map over the fetched products and render ProductCard for each
                    products.map(product => (
                        <ProductCard key={product.id} product={product} refreshCart={loadCartCount} />
                    ))
                ) : (
                    // Display a message if no products are found
                    <div style={{ padding: '20px', width: '100%', textAlign: 'center' }}>
                        <p>No products found. Try a different search term or check the database connection.</p>
                    </div>
                )}
           </div>
        </div>
    </>
  )
}

export default Home


{/*function Home() {
  return (
    <>
    <div className='home'>
       <img src={home_img} className='home_img' alt='home_image' />
       <div className='home_row_1'>
       <Product />
       <Product />
       <Product />
       </div>
    </div>
    </>
  )
}

export default Home */}
