import { createContext } from "react";

import { useAuth } from "../hooks/useAuth";

const Context = createContext()

function UserProvider({ children }) {

    const { authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, UserLogout } = useAuth()

    return (
        <Context.Provider value={{ authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, UserLogout }}>
            {children}
        </Context.Provider>
    )
}

export {Context, UserProvider}