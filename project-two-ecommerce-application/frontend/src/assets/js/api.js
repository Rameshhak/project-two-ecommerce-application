import axios from 'axios';

const token = localStorage.getItem("token");

// Create axios instance
const api = axios.create({
  baseURL: "http://project-two-ecommerce-application-backend-production.up.railway.app/api",
  //baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json"
  },
  // only set withCredentials=true if you rely on cookies / sessions
  withCredentials: false
});

// Attach fresh token before every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const login = async (username, password) => {
  try {
    const response = await axios.post("http://project-two-ecommerce-application-backend-production.up.railway.app/api/auth/login", {
      username,
      password,
    });

    // Save token from backend response
    localStorage.setItem("token", response.data.token);

    return response.data;
  } catch (error) {
    console.error("Login failed:", error);
    throw error;
  }
};

// --- Product Endpoints ---
export const fetchProducts = (keyword = '') => 
  api.get(`/products`, { params: { q: keyword } });

// --- Cart Endpoints ---
export const fetchCart = () => 
  api.get('/cart');

// --- Order Endpoints ---
export const placeOrder = () => 
  api.post('/orders/place');

export const fetchMyOrders = () => 
  api.get('/orders/my');

// --- Cart Endpoints ---

export const addToCart = async (productId, quantity) => {
    try {
        const response = await api.post('/cart/add', {
            productId, 
            quantity 
        });
        // Return the new cart count from the server response
        return response.data; 
    } catch (error) {
        console.error("Error adding item to cart:", error);
        throw error;
    }
};

export const fetchCartItems = async () => {

  const token = localStorage.getItem('token');
  console.log('Cart fetch token:', token);

  try {
    const response = await api.get("/cart", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log("Response from server:", response.data);
    return response.data;

  } catch (error) {
    console.error("Could not load cart:", error);
    throw error
  }
};

// Fetch the cart item count
export const fetchCartCount = async () => {
    try {
        // Assuming you have a specific backend endpoint for just the count:
        // If your backend /api/cart returns a List, you might call it and return .data.length
        // OR you can create a new lightweight endpoint: GET /api/cart/count
        const token = localStorage.getItem("token"); // Get saved JWT
        const response = await api.get('/cart'); // Use the existing endpoint for now
        
        // Assuming /api/cart returns an array of cart items:
        return response.data.length; 
    } catch (error) {
        console.error("Error fetching cart count:", error);
        // Return 0 on error so the app doesn't crash
        return 0; 
    }
};

export const deleteCartItem = async (cartItemId) => {
    try {
        const response = await api.delete(`/cart/delete/${cartItemId}`); 
        
        // The backend should ideally return the updated cart list or the new count.
        // If it returns the new list, return response.data.
        return response.data; 
    } catch (error) {
        console.error("Error deleting item from cart:", error);
        throw error;
    }
};

export const getCartCount = async () => {
  try {
    const response = await api.get("/cart/count");
    return response.data;
  } catch (error) {
    console.error("Error fetching cart count:", error);
    throw error;
  }
};

export default api;



