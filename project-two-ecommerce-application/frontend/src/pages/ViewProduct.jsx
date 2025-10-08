import React, { useEffect, useState } from 'react'
import {Link} from 'react-router-dom'
import api from '../assets/js/api.js'

import '../assets/css/ViewProduct.css'

function ViewProduct() {

    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchProducts = async() => {
        setProducts([]);
        setIsLoading(true);
        setError(null);

    try{
  
        const response = await api.get('/products/display-all-products');

        const data = await response.data //json();
        const processedData = data.map(product => ({
            ...product,
            // Replace all commas with an empty string, then convert to a float
            price: product.price 
                   ? parseFloat(String(product.price).replace(/,/g, '')) 
                   : null 
        }));

        setProducts(processedData);
    }
    catch(err){
        // Axios errors are typically wrapped, so check for a response status for better logging
        const errorMessage = err.response 
            ? `HTTP error status: ${err.response.status}` 
            : err.message;
            
        setError('Failed to fetch products, Please check the API.')
        console.log('Fetch error:', err)
    }
    finally{
        setIsLoading(false);
    }
    }

    useEffect(() => {
        fetchProducts();
    }, []);

  return (
    <>
      <div className='view_product_container'>
            <h1 className='v-product-title-name' style={{marginTop: '70px'}}>Product List</h1>
            
            <button className='click-to-vist-btn' onClick={fetchProducts} disabled={isLoading}>
                {isLoading ? 'Refreshing...' : 'Refresh Product List'}
            </button>

            {error && <p className='view-error-msg'>🚨 {error}</p>}
            
            {/* Display the table if there are products */}
            {products.length > 0 ? (
                <div className='product-container'>
                    <table className='product-table'>
                        <thead>
                            <tr className='t-header-row'>
                                <th className='t-head-cell'>Product Name</th>
                                <th className='t-head-cell'>Price (₹)</th>
                                <th className='t-head-cell'>Description</th>
                                <th className='t-head-cell'>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {products.map((product) => (
                                <tr key={product.id} className='t-product-row'>
                                    {/* Product Name */}
                                    <td className='t-cell'>{product.name}</td>
                                    
                                    {/* Price (with Rupee symbol and formatting) */}
                                    <td className='t-cell'>
                                        {/* Display Rupee symbol and use the pre-processed numeric price */}
                                        ₹
                                        {product.price !== null 
                                            ? product.price.toFixed(2) // already a number from fetchProducts
                                            : 'N/A'
                                        }
                                    </td>
                                    
                                    {/* Description (Safely handle missing description) */}
                                    <td className='t-cell'>
                                        {product.description || 'No description provided.'}
                                    </td>

                                    {/* Action Buttons */}
                                    <td className='action-cell'>
                                        <Link to={`/edit/${product.id}`} className='btn-link'>
                                            <button className='update-btn'>
                                                Update
                                            </button>
                                        </Link>
                                        <Link to={`/delete/${product.id}`} className='btn-link'>
                                            <button className='delete-btn'>
                                                Delete
                                            </button>
                                        </Link>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                // Display message if no products
                !isLoading && !error && <p className='no-product-msg'>No products found.</p>
            )}
        </div>
    </>
  )
}

export default ViewProduct
