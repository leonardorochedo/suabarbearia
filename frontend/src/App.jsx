// USER
import { UserLogin } from "./pages/User/UserLogin/UserLogin";
import { UserRegister } from "./pages/User/UserRegister/UserRegister";
import { UserDelete } from "./pages/User/UserDelete/UserDelete";
import { UserEdit } from "./pages/User/UserEdit/UserEdit";
import { UserChangePassword } from "./pages/User/UserChangePassword/UserChangePassword";
import { UserProfile } from "./pages/User/UserProfile/UserProfile";

// BARBERSHOP
import { BarbershopLogin } from "./pages/Barbershop/BarbershopLogin/BarbershopLogin";
import { BarbershopRegister } from "./pages/Barbershop/BarbershopRegister/BarbershopRegister";
import { BarbershopDelete } from "./pages/Barbershop/BarbershopDelete/BarbershopDelete";
import { BarbershopEdit } from "./pages/Barbershop/BarbershopEdit/BarbershopEdit";
import { BarbershopChangePassword } from "./pages/Barbershop/BarbershopChangePassword/BarbershopChangePassword";
import { BarbershopProfile } from "./pages/Barbershop/BarbershopProfile/BarbershopProfile";

// EMPLOYEE
import { EmployeeLogin } from "./pages/Employee/EmployeeLogin/EmployeeLogin";
import { EmployeeCreate } from "./pages/Employee/EmployeeCreate/EmployeeCreate";
import { EmployeeDelete } from "./pages/Employee/EmployeeDelete/EmployeeDelete";
import { EmployeeEdit } from "./pages/Employee/EmployeeEdit/EmployeeEdit";
import { EmployeeBarbershopDelete } from "./pages/Employee/EmployeeBarbershopDelete/EmployeeBarbershopDelete";
import { EmployeeBarbershopEdit } from "./pages/Employee/EmployeeBarbershopEdit/EmployeeBarbershopEdit";
import { EmployeeProfile } from "./pages/Employee/EmployeeProfile/EmployeeProfile";

// SERVICE
import { ServiceCreate } from "./pages/Service/ServiceCreate/ServiceCreate";
import { ServiceEdit } from "./pages/Service/ServiceEdit/ServiceEdit";

// OTHERS
import { Home } from "./pages/Home/Home";
import { RegisterOptions } from "./pages/Auth/RegisterOptions/RegisterOptions";
import { LoginOptions } from "./pages/Auth/LoginOptions/LoginOptions";

// HEADER & FOOTER
import { Header } from "./components/Header/Header";
import { Footer } from "./components/Footer/Footer";
import { NotFound } from "./pages/Auth/NotFound/NotFound";

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
import { Container } from "./components/Container/Container";

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
            <Container>
              <Header />
                <Routes>
                  {/* USER */}
                  <Route path="/user/login" element={<UserLogin />} />
                  <Route path="/user/register" element={<UserRegister />} />
                  <Route path="/user/delete/:id" element={<UserDelete />} />
                  <Route path="/user/edit" element={<UserEdit />} />
                  <Route path="/user/changepassword" element={<UserChangePassword />} />
                  <Route path="/user/profile" element={<UserProfile />} />
                  {/* BARBERSHOP */}
                  <Route path="/barbershop/login" element={<BarbershopLogin />} />
                  <Route path="/barbershop/register" element={<BarbershopRegister />} />
                  <Route path="/barbershop/delete/:id" element={<BarbershopDelete />} />
                  <Route path="/barbershop/edit" element={<BarbershopEdit />} />
                  <Route path="/barbershop/changepassword" element={<BarbershopChangePassword />} />
                  <Route path="/barbershop/profile" element={<BarbershopProfile />} />
                  {/* EMPLOYEE */}
                  <Route path="/employee/login" element={<EmployeeLogin />} />
                  <Route path="/employee/register" element={<EmployeeCreate />} />
                  <Route path="/employee/delete" element={<EmployeeDelete />} />
                  <Route path="/employee/edit" element={<EmployeeEdit />} />
                  <Route path="/employee/barbershop/edit/:id" element={<EmployeeBarbershopEdit />} />
                  <Route path="/employee/barbershop/delete/:id" element={<EmployeeBarbershopDelete />} />
                  <Route path="/employee/profile" element={<EmployeeProfile />} />
                  {/* SERVICE */}
                  <Route path="/service/create" element={<ServiceCreate />} />
                  <Route path="/service/edit/:id" element={<ServiceEdit />} />
                  {/* OTHER */}
                  <Route path="/" element={<Home />} />
                  <Route path="/register" element={<RegisterOptions />} />
                  <Route path="/login" element={<LoginOptions />} />
                  <Route path="/*" element={<NotFound />} />
                </Routes>
              <Footer />
            </Container>
          </AppProvider>
        </QueryClientProvider>
      </BrowserRouter>
  )
}

export default App
