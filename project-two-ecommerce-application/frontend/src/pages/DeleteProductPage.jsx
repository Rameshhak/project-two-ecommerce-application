import React from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import {useState, useEffect} from 'react'
import api from "../assets/js/api.js"

import '../assets/css/DeleteProductPage.css'

function DeleteProductPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [productName, setProductName] = useState(`Product ${id}`);

  // Fetch product name for better confirmation
  useEffect(() => {
    const fetchProductName = async () => {
      try {
         // 'api' (Axios) instance for automatic token injection
        const response = await api.get(`/products/get-product/${id}`);
        const data = await response.data;
        setProductName(data.name);
      } catch (error) {
        console.warn('Could not fetch product name, using ID.', error);
      }
    };
    fetchProductName();
  }, [id]);

  const handleDelete = async () => {
    // 1. Send DELETE request
    try {
      const response = await api.delete(`/products/delete/${id}`);
  
        alert(`${productName} deleted successfully!`);
        navigate('/viewProduct');
    } catch (error) {
      console.error('Error deleting product:', error);
      alert('An error occurred during deletion.');
    }
  };

  return (
    <>
    <div className='delete-confirm-container'>
      <h2>Confirm Deletion</h2>
      <p className='d-c-text'>
        Are you sure you want to permanently delete the product: 
        <strong> {productName} (ID: {id})</strong>?
      </p>
      <button 
        onClick={handleDelete} className='d-c-delete-btn'>
        Yes, Delete Product
      </button>
      <button onClick={() => navigate('/viewProduct')} className='d-c-cancel-btn'>
        Cancel
      </button>
    </div>
    </>
  );
}

export default DeleteProductPage
