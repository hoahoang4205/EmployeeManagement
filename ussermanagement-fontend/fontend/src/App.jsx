import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import Navbar from './components/common/Navbar';
import LoginPage from './components/auth/LoginPage';
import RegistrationPage from './components/auth/RegistrationPage';
import FooterComponent from './components/common/Footer';
import UserService from './components/service/UserService';
import UpdateUser from './components/userspage/UpdateUser';
import UserManagementPage from './components/userspage/UserManagementPage';
import ProfilePage from './components/userspage/ProfilePage';

const isAdmin = UserService.adminOnly(); // Kiểm tra quyền admin

// Cấu hình router với các future flags
const router = createBrowserRouter([
    { path: "/", element: <LoginPage /> },
    { path: "/login", element: <LoginPage /> },
    { path: "/profile", element: <ProfilePage /> },
    ...(isAdmin ? [
        { path: "/register", element: <RegistrationPage /> },
        { path: "/admin/user-management", element: <UserManagementPage /> },
        { path: "/update-user/:userId", element: <UpdateUser /> }
    ] : []),
    { path: "*", element: <Navigate to="/login" /> }
], {
    future: {
        v7_startTransition: true,
        v7_relativeSplatPath: true,
    },
});

function App() {
    return (
        <RouterProvider router={router}>
            <div className="App">
                <Navbar />
                <div className="content">
                    {/* Các route đã khai báo trong cấu hình router */}
                </div>
                <FooterComponent />
            </div>
        </RouterProvider>
    );
}

export default App;
