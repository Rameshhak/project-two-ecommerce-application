import React from 'react'
import logo from "../../assets/images/amazonLogo.png"
import search_img from "../../assets/images/search_img.png"
import cart_img from "../../assets/images/cart.png"
import user_img from "../../assets/images/user.png"
import {NavLink} from 'react-router-dom'
import { useEffect, useState } from "react";

import '../../assets/css/header.css'

// Accept search state, search handler, and cart count as props
function Header({ searchQuery, setSearchQuery, handleSearch}) {

    const [userId, setUserId] = useState(null);

  useEffect(() => {
    setUserId(localStorage.getItem("userId")); // reads after component mounts
    if(userId){
     setUserId(userId);
    }
  }, []); // empty dependency = runs once

  console.log("Header userId:", userId);

  return (
   <>
    <div className='header'>
      <div className='header_logo'>
       <NavLink to='/' ><img src={logo} className='h-amazon-logo' /></NavLink>
      </div>
      {/* Wrap search bar in a form for submission */}
      <form className='search_bar' onSubmit={handleSearch}>
         <input type="search" className='search_input' name='keyword' placeholder='Search products by name' value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)} /> 
      <button type="search-submit" className='search_button'>
         <img className='search_img' src={search_img}  alt="Search Icon"/>
      </button>
      </form>
      <div className='nav_bar'>
        <div className='nav-option'>
           <span className='nav_optionLineOne admin_page'>Page of</span>
           <NavLink to='/admin' style={{textDecoration: 'none'}}><span className='nav_optionLineTwo admin-page'>Admin</span></NavLink>
        </div>
        <div className='nav-option'>
           <span className='nav_optionLineOne'>Hello, User</span>
           <NavLink to='/register' style={{textDecoration: 'none'}}><span className='nav_optionLineTwo register_page'>Register</span></NavLink>
        </div>
        <div className='nav-option'>
           <span className='nav_optionLineOne'>Page of</span>
           <NavLink to='/login' style={{textDecoration: 'none'}}><span className='nav_optionLineTwo login_page'>Login</span></NavLink>
        </div>
        <div className='nav-option'>
           <span className='nav_optionLineOne'>Return</span>
           <NavLink to='/checkout' style={{textDecoration: 'none'}}><span className='nav_optionLineTwo'>& Order</span></NavLink>
        </div>
        <div className='nav-cart'>
           <NavLink to='/cart'><span className='nav_cart_img'><img src={cart_img}  className='cart-img'/></span></NavLink>
        </div>
        <div className='nav-option'>
           <NavLink to={userId ? `/user-edit/${userId}` : "/login"}><span className='nav_optionLineOne'><img src={user_img} className='user_img'  alt="User Profile" /></span></NavLink>
        </div>
      </div>
   </div>
    </>
  )
}

export default Header