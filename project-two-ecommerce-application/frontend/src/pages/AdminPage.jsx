import React from 'react'
import add_product from '../assets/images/add-product.png'
import edit_product from '../assets/images/product-edit.png'
import { useState } from 'react'

import '../assets/css/AdminPage.css'
import { useNavigate } from 'react-router-dom'

function AdminPage() {

   const [productFormData, setProductFormData] = useState(
           {
              name: '',
              price: '',
              description: '',
           }
        );

        const [statusMessage, setStatusMessage] = useState('');
        const [isError, setIsError] = useState(false);
  
        const handleChange = (e) => {
        const { name, value } = e.target;
        setProductFormData((prevData) => ({
              ...prevData,
              [name]: value,
        }))
        };
  
        const handleSubmit = async (e) =>{
          e.preventDefault();
  
          setStatusMessage('Sending product data...');
          setIsError(false);

          // Get the token from storage
        const token = localStorage.getItem('token');

        if (!token) {
            console.error('Product submission failed: No token found. Redirecting to login.');
            setStatusMessage('Error: Not authenticated. Redirecting to login.');
            setIsError(true);
            // Optionally redirect to login if no token is found
            // navigate('/login'); 
            return;
        }
  
          // The fetch request logic
          try{
             const url = `http://project-two-ecommerce-application-backend-production.up.railway.app/api/products/create`;
             const response = await fetch(url, {
             method: 'POST',
             headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` 
             },
             body: JSON.stringify(productFormData),
            });
        
           // Check if the server's response was successful
           if(!response.ok){
              // Read the status code to give a better error message
                if (response.status === 403) {
                     throw new Error(`Access Denied (403). User does not have ADMIN privileges.`);
                }
              throw new Error(`Http error! status: ${response.status}`);
           }
  
           // Parse the JSON response from the server
           const result = await response.json();
           console.log('Success', result);
  
           // Update the UI with a success message
           setStatusMessage('Product submitted successfully');
  
          // Reset the form inputs after successful submission
          setProductFormData({
           name: '',
           price: '',
           description: '',
          });
         }
         catch(error){
           console.error('Product submission failed : ', error);
           // Update the UI with an error message
           setStatusMessage('Product submission failed, Please try again');
           setIsError(true);
         }
        };

      const navigate = useNavigate();
  

  return (
    <>
      <div className='admin_page'>
        <div className='container'>
          <div><h2 className='admin_page_title'>Welcome to admin page to create or update or delete products</h2></div>
          <div className='content_box'>
            <form className='left_box' onSubmit={handleSubmit}>
              <div className='subtitle_box'>
                <span className='ad_sub_title'>Create Products</span><img src={add_product} className='sub_img'/>
              </div>
              <div className='ad_p_name_box'>
                <label htmlFor='name' className='ad-n-label'>Product Name</label>
                <input type='text' id='name' name='name' value={productFormData.name} onChange={handleChange} className='ad-n-input' />
              </div>
              <div className='ad_p_price_box'>
                <label htmlFor='price' className='ad-p-label'>Product Price</label>
                <input type='text' id='price' name='price' value={productFormData.price} onChange={handleChange} className='ad-p-input' />
              </div>
              <div className='ad_p_description_box'>
                <label htmlFor='description' className='ad-d-label'>Product Description</label>
                <textarea rows='4' cols='30' id='description' name='description' value={productFormData.description} onChange={handleChange} className='ad-d-input' /
                >
              </div>
              <button type='submit' className='ad-submit-btn'>Submit</button>
            </form>
            <div className='right_box'>
              <img src={edit_product} className='right_side_img'/>
              <button className='edit_btn' onClick={() => navigate('/viewProduct')}>View & Edit Products</button>
            </div>
          </div>
          {statusMessage && (
        <p className='ad_info_msg' style={{ color: isError ? 'red' : 'green' }}>
          {statusMessage}
        </p>
      )}
        </div>
      </div>
    </>
  )
};

export default AdminPage

