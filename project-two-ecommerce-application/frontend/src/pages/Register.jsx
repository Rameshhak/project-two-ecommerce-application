import React from 'react'
import { useState } from 'react'
import amazon_logo from '../assets/images/amazon_logo_white.jpg'

import '../assets/css/Register.css'

function Register() {

      // Use a single state object for all form inputs
      const [formData, setFormData] = useState(
         {
            name: '',
            email: '',
            password: '',
            role: 'ROLE_CUSTOMER',
         }
      );

      // State for displaying messages to the user
      const [statusMessage, setStatusMessage] = useState('');
      const [isError, setIsError] = useState(false);

      // This function updates the state whenever any input changes
      const handleChange = (e) => {
      const { name, value } = e.target;
      setFormData((prevData) => ({
            ...prevData,
            [name]: value,
      }))
      };

      // The function to handle form submission
      const handleSubmit = async (e) =>{
        e.preventDefault();

        // Update the UI to show a loading/sending message
        setStatusMessage('Sending registration data...');
        setIsError(false);

        // The fetch request logic
        try{
           const response = await fetch(`http://localhost:8080/api/user/register`, {
           method: 'POST',
           headers: {
              'Content-Type': 'application/json'
           },
           body: JSON.stringify(formData),
          });
      
         // Check if the server's response was successful
         if(!response.ok){
            throw new Error(`Http error! status: ${response.status}`);
         }

         // Parse the JSON response from the server
         const result = await response.json();
         console.log('Success', result);

         // Update the UI with a success message
         setStatusMessage('User registered successfully');

        // Reset the form inputs after successful submission
        setFormData({
         name: '',
         email: '',
         password: '',
         role: 'ROLE_CUSTOMER',
        });
       }
       catch(error){
         console.error('Registration failed : ', error);
         // Update the UI with an error message
         setStatusMessage('Registration failed, Please try again');
         setIsError(true);
       }
      };

  return (
   <>
    <div className='register_page'>
       <div className='reg_container'>
          <img src={amazon_logo} className='reg-page_logo' />
          <form className='container_inside_box' onSubmit={handleSubmit}>
             <h3 className='reg-title_name'>Register Page</h3>
             <div className='name_box'>
                <label htmlFor='name' className='reg-n-label'>Username</label>
                <input type='text' id='name' name='name' className='reg-n-input' value={formData.name} onChange={handleChange} required />
             </div>
             <div className='email_box'>
                <label htmlFor='email' className='reg-e-label'>Email ID</label>
                <input type='email' id='email' name='email' className='reg-e-input' value={formData.email} onChange={handleChange} required />
             </div>
             <div className='passowrd_box'>
                <label htmlFor='password' className='reg-p-label'>Password</label>
                <input type='password' id='password' name='password' className='reg-p-input' value={formData.password} onChange={handleChange} required />
             </div>
             <div className='role_box'>
                <p className='role_info'>Select your role</p>
                <select className='select_head' name='role' value={formData.role} onChange={handleChange} >
                   <option className='option-1' value='ROLE_CUSTOMER'>Customer</option>
                   <option className='option-2' value='ROLE_ADMIN'>Admin</option>
                </select>
             </div>
             <div className='reg-submit_box'>
                <button className='reg-submit-btn' type='submit' >Register</button>
             </div>
          </form>
          {statusMessage && (
        <p className='reg-info_msg' style={{ color: isError ? 'red' : 'green' }}>
          {statusMessage}
        </p>
      )}
       </div>
    </div>
   </>
  )
}

export default Register
