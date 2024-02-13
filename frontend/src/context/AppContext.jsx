import { createContext } from "react";

import { useAuth } from "../hooks/useAuth";

const Context = createContext()

function AppProvider({ children }) {

    const { authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, BarbershopRegister, BarbershopLogin, BarbershopDelete, BarbershopEdit, BarbershopChangePassword, Logout } = useAuth()

    return (
        <Context.Provider value={{ authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, BarbershopRegister, BarbershopLogin, BarbershopDelete, BarbershopEdit, BarbershopChangePassword, Logout }}>
            {children}
        </Context.Provider>
    )
}

export {Context, AppProvider}