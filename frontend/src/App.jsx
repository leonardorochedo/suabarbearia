// USER
import { UserLogin } from "./pages/User/UserLogin/UserLogin";
import { UserRegister } from "./pages/User/UserRegister/UserRegister";
import { UserEdit } from "./pages/User/UserEdit/UserEdit";
import { UserDelete } from "./pages/User/UserDelete/UserDelete";

// CONTEXTS
import { UserProvider } from "./context/UserContext";

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
          <UserProvider>
            <Routes>
              <Route path="/user/login" element={<UserLogin />} />
              <Route path="/user/register" element={<UserRegister />} />
              <Route path="/user/edit" element={<UserEdit />} />
              <Route path="/user/delete/:id" element={<UserDelete />} />
            </Routes>
          </UserProvider>
        </QueryClientProvider>
      </BrowserRouter>
  )
}

export default App
