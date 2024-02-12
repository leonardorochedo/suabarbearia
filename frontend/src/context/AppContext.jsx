import { createContext } from "react";

import { useAuth } from "../hooks/useAuth";

const Context = createContext()

function AppProvider({ children }) {

    const { authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, BarbershopRegister, BarbershopLogin, BarbershopDelete, BarbershopEdit, Logout } = useAuth()

    return (
        <Context.Provider value={{ authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, BarbershopRegister, BarbershopLogin, BarbershopDelete, BarbershopEdit, Logout }}>
            {children}
        </Context.Provider>
    )
}

export {Context, AppProvider}