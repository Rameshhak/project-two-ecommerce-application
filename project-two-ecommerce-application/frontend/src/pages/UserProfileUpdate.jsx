import React from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import {useState, useEffect} from 'react'
import api from '../assets/js/api.js'
``
import '../assets/css/UserProfileUpdate.css'

function UserProfileUpdate() {

    const { id } = useParams(); // Get 'id' from URL
    const navigate = useNavigate();

    const [userFormData, setUserFormData] = useState({
        name: "",
        email: "",
        password: "",
        role: "ROLE_CUSTOMER"
    });
    const [isUpdating, setIsUpdating] = useState(false);
    const [loading, setLoading] = useState(true);

    // Fetch user details on mount
    useEffect(() => {
        if (!id) return; // safeguard
        const fetchUser = async () => {
            try {
                const res = await api.get(`user/get-user/${id}`);
                
               const data = res.data;
                setUserFormData({
                    name: data.name,
                    email: data.email,
                    password: "", // leave blank
                    role: data.role
                });
                setLoading(false);
            } catch (err) {
                // 403/401 errors here
               const status = err.response ? err.response.status : null;
                  if (status === 403) {
                     console.error("Error fetching user: Access Denied (Check Admin/User privileges)");
                  } else {
                     console.error("Error fetching user:", err);
            }
                setLoading(false);
            }
        };
        fetchUser();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsUpdating(true);
        try {

            const res = await api.put(`/user/update/${id}`, userFormData); 
        
            // Axios success check is implicit
            alert("User updated successfully!");
            navigate("/"); 

        } catch (err) {
            console.error("Error updating user:", err);
            alert('Update failed. Check console for details.');
        } finally {
            setIsUpdating(false);
        }
    };

    if (loading) return <div>Loading user details...</div>;

    return (
        <div className='e-user-container'>
            <h2 className='e-user-sub-title'>Edit User Profile (ID: {id})</h2>
            <form onSubmit={handleSubmit} className='e-user-form-section'>
                <label className='e-user-id-label'>
                    User Id (from URL):
                    <input type="text" value={id} className='e-user-id-input' readOnly />
                </label>
                <label className='e-user-n-label'>
                    User Name:
                    <input type="text" name="name" value={userFormData.name} onChange={handleChange} className='e-user-n-input' required />
                </label>
                <label className='e-user-e-label'>
                    User Email:
                    <input type="email" name="email" value={userFormData.email} onChange={handleChange} className='e-user-e-input' required />
                </label>
                <label className='e-user-p-label'>
                    User Password (Leave blank to keep existing):
                    <input type='password' name="password" value={userFormData.password} onChange={handleChange} className='e-user-p-input' placeholder='New password' />
                </label>
                <label className='role_info'>Select your role</label>
                <select className='select_head' name='role' value={userFormData.role} onChange={handleChange}>
                    <option value='ROLE_CUSTOMER'>Customer</option>
                    <option value='ROLE_ADMIN'>Admin</option>
                </select>
                <button type="submit" disabled={isUpdating} className='e-user-submit-button'>
                    {isUpdating ? 'Saving...' : 'Save Changes'}
                </button>
            </form>
            <button onClick={() => navigate('/viewProduct')} className='cancel-button'>Cancel</button>
        </div>
    );
}

export default UserProfileUpdate
