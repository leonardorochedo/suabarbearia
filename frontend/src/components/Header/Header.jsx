import api from "../../utils/api";

// CONTEXT
import { useState, useEffect, useContext } from 'react';
import { Context } from "../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// IMAGES
import { RoundImage } from "../RoundImage/RoundImage";
import logo from "../../assets/images/logo.png";
import userNoImage from "../../assets/images/nopic.png";

// ICONS
import { FiLogIn } from "react-icons/fi";
import { IoMdMenu } from "react-icons/io";

import "./Header.css";

export function Header() {

    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee } = useContext(Context);
    const [user, setUser] = useState({ id: "", name: "", image: "" });

    // Consulting API
    useEffect(() => {
        if (authenticatedUser) {
            api.get("/users/profile").then((response) => {
              setUser({
                id: response.data.data.id,
                name: response.data.data.name,
                image: response.data.data.image
              })
            });

            return
        }

        if (authenticatedBarbershop) {
            api.get("/barbershops/profile").then((response) => {
              setUser({
                id: response.data.data.id,
                name: response.data.data.name,
                image: response.data.data.image
              })
            });

            return
        }

        if (authenticatedEmployee) {
            api.get("/employees/profile").then((response) => {
              setUser({
                id: response.data.data.id,
                name: response.data.data.name,
                image: response.data.data.image
              })
            });

            return
        }
    }, [authenticatedUser, authenticatedBarbershop, authenticatedEmployee])

    return (
        <header>
            <div className="button-header">
                <button>
                    <IoMdMenu size={24} color="#FFF" />
                </button>
            </div>
            <Link to="/" className="logo-header">
                <img src={logo} alt="Logomarca" />
            </Link>
            <div className="button-header">
            {authenticatedUser || authenticatedBarbershop || authenticatedEmployee ? (
                <Link to={`/users/${user.id}`} className="link">
                    <div className="user-header">
                        {user.image ? (
                            <RoundImage src={user.image} alt={user.name} size="rem3" />
                        ) : (
                            <RoundImage src={userNoImage} alt={user.name} size="rem3" />
                        )}
                        <p>{user.name.split(" ")[0].toUpperCase()}</p>
                    </div>
                </Link>
            ) : ( 
                <Link to="/login" className="link">
                    <span className="link">
                        <FiLogIn size={24} color="#FFF" />
                        <p>Entrar</p>
                    </span>
                </Link>
            )}
            </div>
        </header>
    )
}