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
        UserChangePasswordWithToken,
        BarbershopRegister,
        BarbershopLogin,
        BarbershopDelete,
        BarbershopEdit,
        BarbershopChangePassword,
        BarbershopChangePasswordWithToken,
        EmployeeCreate,
        EmployeeLogin,
        EmployeeDelete,
        EmployeeEdit,
        EmployeeBarbershopDelete,
        EmployeeBarbershopEdit,
        ServiceCreate,
        ServiceEdit,
        SchedulingCreate,
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
            UserChangePasswordWithToken,
            BarbershopRegister,
            BarbershopLogin,
            BarbershopDelete,
            BarbershopEdit,
            BarbershopChangePassword,
            BarbershopChangePasswordWithToken,
            EmployeeCreate,
            EmployeeLogin,
            EmployeeDelete,
            EmployeeEdit,
            EmployeeBarbershopDelete,
            EmployeeBarbershopEdit,
            ServiceCreate,
            ServiceEdit,
            SchedulingCreate,
            Logout
        }}>
            {children}
        </Context.Provider>
    )
}

export {Context, AppProvider}