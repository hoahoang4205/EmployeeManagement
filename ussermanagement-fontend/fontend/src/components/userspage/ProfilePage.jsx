import { useState, useEffect } from 'react';
import UserService from '../service/UserService';
import { Link } from 'react-router-dom';

function ProfilePage() {
    const [profileInfo, setProfileInfo] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchProfileInfo();
    }, []);

    const fetchProfileInfo = async () => {
        try {
            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            const response = await UserService.getYourProfile(token);
            setProfileInfo(response.ourUsers); // Ensure the structure is correct here
            setIsLoading(false);
        } catch (error) {
            console.error("Error fetching profile information:", error); // Log the error for debugging
            setError(error.message || "Error fetching profile information"); // Use err.message to capture the error message
            setIsLoading(false);
        }
    };

    if (isLoading) {
        return <div>Loading profile...</div>; // Or a loading spinner
    }

    if (error) {
        return <div>{error}</div>; // Display error message if there is one
    }

    if (!profileInfo) {
        return <div>No profile data available.</div>; // In case profileInfo is null or undefined
    }

    return (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>Name: {profileInfo.name}</p>
            <p>Email: {profileInfo.email}</p>
            <p>Position: {profileInfo.position}</p>
            {profileInfo.role === "ADMIN" && (
                <button><Link to={`/update-user/${profileInfo.id}`}>Update This Profile</Link></button>
            )}
        </div>
    );
}

export default ProfilePage;
