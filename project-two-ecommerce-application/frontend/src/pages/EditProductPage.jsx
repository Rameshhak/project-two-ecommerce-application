import React from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import {useState, useEffect} from 'react'
import api from '../assets/js/api.js'

import '../assets/css/EditProductPage.css'

function EditProductPage() {

  const { id } = useParams(); 
  const navigate = useNavigate();

  const [formData, setFormData] = useState({ 
    name: '', 
    price: '',
    description: ''
  });

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isUpdating, setIsUpdating] = useState(false);

  // Fetch current product data to pre-fill the form
  useEffect(() => {
    const fetchProductDetails = async () => {
      try {
        const response = await api.get(`/products/get-product/${id}`); 
       
        const data = await response.data;

        // Pre-process the price string to a number for form display
        const numericPrice = data.price 
                             ? parseFloat(String(data.price).replace(/,/g, '')) 
                             : '';

         // Set the formData with the fetched values
        setFormData({ 
            name: data.name || '', 
            price: numericPrice, // Use the numeric price
            description: data.description || '' // Set description
        });

      } 
      catch (error) {
            // Handle specific errors like 403 access denied
            const status = error.response ? error.response.status : null;
        console.error('Error fetching product details:', error);
      } 
      finally {
        setIsLoading(false);
      }
    };
    fetchProductDetails();
  }, [id]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Handle Form Submission (POST)
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsUpdating(true);

    // Convert the price back to a standard string format for the backend if necessary, 
    // or send it as a number if your backend expects that.
    const payload = {
        ...formData,
        // Ensure price is sent as a number if backend requires it
        price: parseFloat(formData.price) 
    };

    try {
        // the 'api' (Axios) instance for the update call as well
        const response = await api.post(`/products/update/${id}`, payload);

      alert(`Product ${formData.name} (ID: ${id}) updated successfully!`);
        navigate('/viewProduct'); 

    } catch (err) {
      console.error('Error updating product:', err);
      alert('An error occurred during update.');
    }
    finally {
      setIsUpdating(false);
    }
  };

  if (isLoading) return <p>Loading product details...</p>;
  if (error) return <p style={{color: 'red'}}>{error}</p>;

  return (
  <>
    <div className='edit-product-container'>
      <h2 className='eproduct-sub-title' >Edit Product (ID: {id})</h2>
      <form onSubmit={handleSubmit} className='eproduct-form-section'>
        <label>
          Product Name:
          <input type="text" name="name" value={formData.name} onChange={handleChange} className='eproduct-input-element' required />
        </label>
        <br/><br/>
        <label>
          Price (₹):
          <input type="number" name="price" value={formData.price} onChange={handleChange} className='eproduct-input-element' required />
        </label>
        <br/><br/>
         <label>
          Description:
          <textarea name="description" value={formData.description} onChange={handleChange} rows="4" className='eproduct-input-element' ></textarea>
        </label>
        <br/><br/>
        <button type="submit" disabled={isUpdating} className='eproduct-submit-button'> {isUpdating ? 'Saving...' : 'Save Changes'}
        </button>
      </form>
      <button onClick={() => navigate('/viewProduct')} className='cancel-button'>
          Cancel
      </button>
    </div>
  </>
  );
}

export default EditProductPage
