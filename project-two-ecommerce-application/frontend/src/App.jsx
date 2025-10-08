import Register from './pages/Register.jsx'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Admin from './pages/AdminPage.jsx'
import ViewProduct from './pages/ViewProduct.jsx'
import EditProductPage from './pages/EditProductPage.jsx'
import DeleteProductPage from './pages/DeleteProductPage.jsx'
import ProductCard from './pages/ProductCard.jsx'
import CartPage from './pages/CartPage.jsx'
import CheckoutPage from './pages/CheckoutPage.jsx'
import EditUserPage from './pages/UserProfileUpdate.jsx'

import './App.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import RootLayout from './components/layout/RootLayout.jsx'

function App() {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path='/' element={<RootLayout />}>
        <Route index element={<Home />}/>
        <Route path='admin' element={<Admin />}/>
        <Route path='register' element={<Register />}/>
        <Route path='login' element={<Login />}/>
        <Route path='viewProduct' element={<ViewProduct />}/>
        <Route path="edit/:id" element={<EditProductPage />} />
        <Route path="delete/:id" element={<DeleteProductPage />} />
        <Route path='product-card' element={<ProductCard />}/>
        <Route path='cart' element={<CartPage />}/>
        <Route path='checkout' element={<CheckoutPage />}/>
        <Route path='user-edit/:id' element={<EditUserPage />}/>
      </Route>
    )
  )

  return (
    <>
      <RouterProvider router={router}/>
    </>
  )
}

export default App
