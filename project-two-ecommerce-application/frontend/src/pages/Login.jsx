import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import amazon_logo from '../assets/images/amazon_logo_white.jpg'

import '../assets/css/Login.css'

function Login() {

   const navigate = useNavigate();

   const [name, setName] = useState("");
   const [password, setPassword] = useState("");
   const [error, setError] = useState("");

   let handleSubmit = async (e) => {
   e.preventDefault();

   try {
    const response = await fetch(`http://localhost:8080/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, password }),
    });

    const data = await response.json();

    if (response.ok && data.token) {
      localStorage.setItem("token", data.token);
      localStorage.setItem("userId", data.userId); // store userId for NavLink
      alert("Login Successful!");
      navigate("/"); // redirect after successful login
    } else {
      setError(data.error || "Invalid username or password");
    }

    // reset form
    setName("");
    setPassword("");

  } catch (err) {
    setError("Server error, try again later");
  }
};


  return (
      <>
        <div className='login_page'>
           <div className='login_conatiner'>
              <img src={amazon_logo} className='log-page_logo' />
              <form className='container_inside_box' onSubmit={handleSubmit}>
                 <h3 className='log-title_name'>Login Page</h3>
                 <div className='name_box'>
                    <label htmlFor='name' className='log-n-label'>Username</label>
                    <input type='text' id='name'name='name' className='log-n-input' placeholder='Enter username' 
                           value={name} onChange={(e) => setName(e.target.value)} required />
                 </div>
                 <div className='passowrd_box'>
                    <label htmlFor='password' className='log-p-label'>Password</label>
                    <input type='password' id='password' name='password' className='log-p-input' placeholder='Enter password' 
                           value={password} onChange={(e) => setPassword(e.target.value)} required />
                 </div>
                 <div className='log-submit_box'>
                    <button className='log-submit-btn' type='submit' >Submit</button>
                 </div>
              </form>
              {error && <p style={{ color: "red" }} className='log-info-msg'>{error}</p>}
           </div>
        </div>
      </>
  )
}

export default Login
