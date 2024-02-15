import { createContext } from "react";

import { useAuth } from "../hooks/useAuth";

const Context = createContext()

function AppProvider({ children }) {

    const { 
        authenticatedUser,
        authenticatedBarbershop,
        authenticatedEmployee,
        UserRegister,
        UserLogin,
        UserDelete,
        UserEdit,
        UserChangePassword,
        BarbershopRegister,
        BarbershopLogin,
        BarbershopDelete,
        BarbershopEdit,
        BarbershopChangePassword,
        EmployeeCreate,
        EmployeeLogin,
        EmployeeDelete,
        EmployeeEdit,
        EmployeeBarbershopDelete,
        EmployeeBarbershopEdit,
        ServiceCreate,
        Logout
    } = useAuth()

    return (
        <Context.Provider value={{ 
            authenticatedUser,
            authenticatedBarbershop,
            authenticatedEmployee,
            UserRegister,
            UserLogin,
            UserDelete,
            UserEdit,
            UserChangePassword,
            BarbershopRegister,
            BarbershopLogin,
            BarbershopDelete,
            BarbershopEdit,
            BarbershopChangePassword,
            EmployeeCreate,
            EmployeeLogin,
            EmployeeDelete,
            EmployeeEdit,
            EmployeeBarbershopDelete,
            EmployeeBarbershopEdit,
            ServiceCreate,
            Logout
        }}>
            {children}
        </Context.Provider>
    )
}

export {Context, AppProvider}