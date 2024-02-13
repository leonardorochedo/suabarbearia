// USER
import { UserLogin } from "./pages/User/UserLogin/UserLogin";
import { UserRegister } from "./pages/User/UserRegister/UserRegister";
import { UserDelete } from "./pages/User/UserDelete/UserDelete";
import { UserEdit } from "./pages/User/UserEdit/UserEdit";
import { UserChangePassword } from "./pages/User/UserChangePassword/UserChangePassword";

// BARBERSHOP
import { BarbershopLogin } from "./pages/Barbershop/BarbershopLogin/BarbershopLogin";
import { BarbershopRegister } from "./pages/Barbershop/BarbershopRegister/BarbershopRegister";
import { BarbershopDelete } from "./pages/Barbershop/BarbershopDelete/BarbershopDelete";
import { BarbershopEdit } from "./pages/Barbershop/BarbershopEdit/BarbershopEdit";
import { BarbershopChangePassword } from "./pages/Barbershop/BarbershopChangePassword/BarbershopChangePassword";

// CONTEXTS
import { AppProvider } from "./context/AppContext";

// QUERY CLIENT
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

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
        <QueryClientProvider client={queryClient}>
          <AppProvider>
            <Routes>
              {/* USER */}
              <Route path="/user/login" element={<UserLogin />} />
              <Route path="/user/register" element={<UserRegister />} />
              <Route path="/user/delete/:id" element={<UserDelete />} />
              <Route path="/user/edit" element={<UserEdit />} />
              <Route path="/user/changepassword" element={<UserChangePassword />} />
              {/* BARBERSHOP */}
              <Route path="/barbershop/login" element={<BarbershopLogin />} />
              <Route path="/barbershop/register" element={<BarbershopRegister />} />
              <Route path="/barbershop/delete/:id" element={<BarbershopDelete />} />
              <Route path="/barbershop/edit" element={<BarbershopEdit />} />
              <Route path="/barbershop/changepassword" element={<BarbershopChangePassword />} />
            </Routes>
          </AppProvider>
        </QueryClientProvider>
      </BrowserRouter>
  )
}

export default App
