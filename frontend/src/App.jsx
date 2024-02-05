// USER
import { UserLogin } from "./pages/User/UserLogin/UserLogin";
import { UserRegister } from "./pages/User/UserRegister/UserRegister";

// CONTEXTS
import { UserProvider } from "./context/UserContext";

// LIB'S
import { 
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

function App() {
  

  return (
    <BrowserRouter>
      <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
        <UserProvider>
          <Routes>
            <Route path="/user/login" element={<UserLogin />} />
            <Route path="/user/register" element={<UserRegister />} />
          </Routes>
        </UserProvider>
      </BrowserRouter>
  )
}

export default App
