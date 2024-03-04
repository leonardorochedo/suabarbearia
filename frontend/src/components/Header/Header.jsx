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
import { FiLogOut } from "react-icons/fi";
import { IoMdMenu } from "react-icons/io";
import { IoMdClose } from "react-icons/io";
import { AiOutlineSchedule } from "react-icons/ai";
import { SlMustache } from "react-icons/sl";
import { CiCreditCard1 } from "react-icons/ci";
import { GoPerson } from "react-icons/go";
import { IoPeopleOutline, IoStatsChartOutline } from "react-icons/io5";
import { MdOutlineDesignServices } from "react-icons/md";
import { GiMoneyStack } from "react-icons/gi";

import "./Header.css";

export function Header() {

    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee, Logout } = useContext(Context);
    const [user, setUser] = useState({ id: "", name: "", image: "" });
    const [sidebarVisible, setSidebarVisible] = useState(false);

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

    const toggleSidebar = () => {
        setSidebarVisible(!sidebarVisible);
    };

    const toggleLogout = () => {
        Logout();
    };

    return (
        <header>
            <div className="button-header">
                <button onClick={toggleSidebar}>
                    {sidebarVisible ? (
                        <IoMdClose size={24} color="#FFF" />
                    ) : (
                        <IoMdMenu size={24} color="#FFF" />
                    )}
                </button>
            </div>
            <Link to="/" className="logo-header">
                <img src={logo} alt="Logomarca" />
            </Link>
            <div className="button-header">
                {authenticatedUser && (
                    <Link to={`/user/profile`} className="link">
                        <div className="user-header">
                            {user.image ? (
                                <RoundImage src={user.image} alt={user.name} size="rem3" />
                            ) : (
                                <RoundImage src={userNoImage} alt={user.name} size="rem3" />
                            )}
                            <p>{user.name.split(" ")[0].toUpperCase()}</p>
                        </div>
                    </Link>
                )}
                {authenticatedBarbershop && (
                    <Link to={`/barbershop/profile`} className="link">
                        <div className="user-header">
                            {user.image ? (
                                <RoundImage src={user.image} alt={user.name} size="rem3" />
                            ) : (
                                <RoundImage src={userNoImage} alt={user.name} size="rem3" />
                            )}
                            <p>{user.name.split(" ")[0].toUpperCase()}</p>
                        </div>
                    </Link>
                )}
                {authenticatedEmployee && (
                    <Link to={`/employee/profile`} className="link">
                        <div className="user-header">
                            {user.image ? (
                                <RoundImage src={user.image} alt={user.name} size="rem3" />
                            ) : (
                                <RoundImage src={userNoImage} alt={user.name} size="rem3" />
                            )}
                            <p>{user.name.split(" ")[0].toUpperCase()}</p>
                        </div>
                    </Link>
                )}
                {(!authenticatedUser && !authenticatedBarbershop && !authenticatedEmployee) && (
                    <Link to="/login" className="link">
                        <span className="link">
                            <FiLogIn size={24} color="#FFF" />
                            <p>Entrar</p>
                        </span>
                    </Link>
                )}
            </div>
            <div className={`sidebar ${sidebarVisible ? 'visible' : ''}`}>
                <div className="buttons-sidebar">
                    {authenticatedUser && (
                        <>
                            <Link to="/" className="button-sidebar">
                                <AiOutlineSchedule size={24} color="#FFF" />
                                <p>Agendamentos</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <CiCreditCard1 size={24} color="#FFF" />
                                <p>Planos</p>
                            </Link>
                            <Link to="/user/barbershops" className="button-sidebar">
                                <SlMustache size={24} color="#FFF" />
                                <p>Barbearias</p>
                            </Link>
                            <Link to="/user/profile" className="button-sidebar">
                                <GoPerson size={24} color="#FFF" />
                                <p>Perfil</p>
                            </Link>
                            <div className="button-sidebar logout" onClick={toggleLogout}>
                                <FiLogOut size={24} color="#FFF" />
                                <p>Sair</p>
                            </div>
                        </>
                    )}
                    {authenticatedBarbershop && (
                        <>
                            <Link to="/" className="button-sidebar">
                                <AiOutlineSchedule size={24} color="#FFF" />
                                <p>Agendamentos</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <IoPeopleOutline size={24} color="#FFF" />
                                <p>Funcionários</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <MdOutlineDesignServices size={24} color="#FFF" />
                                <p>Serviços</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <IoStatsChartOutline size={24} color="#FFF" />
                                <p>Estatísticas</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <GiMoneyStack size={24} color="#FFF" />
                                <p>Saque</p>
                            </Link>
                            <Link to="/barbershop/profile" className="button-sidebar">
                                <GoPerson size={24} color="#FFF" />
                                <p>Perfil</p>
                            </Link>
                            <div className="button-sidebar logout" onClick={toggleLogout}>
                                <FiLogOut size={24} color="#FFF" />
                                <p>Sair</p>
                            </div>
                        </>
                    )}
                    {authenticatedEmployee && (
                        <>
                            <Link to="/" className="button-sidebar">
                                <AiOutlineSchedule size={24} color="#FFF" />
                                <p>Agendamentos</p>
                            </Link>
                            <Link to="/" className="button-sidebar">
                                <IoStatsChartOutline size={24} color="#FFF" />
                                <p>Estatísticas</p>
                            </Link>
                            <Link to="/employee/profile" className="button-sidebar">
                                <GoPerson size={24} color="#FFF" />
                                <p>Perfil</p>
                            </Link>
                            <div className="button-sidebar logout" onClick={toggleLogout}>
                                <FiLogOut size={24} color="#FFF" />
                                <p>Sair</p>
                            </div>
                        </>
                    )}
                    {(!authenticatedUser && !authenticatedBarbershop && !authenticatedEmployee) && (
                        <>
                            <Link to="/login" className="button-sidebar">
                                <FiLogIn size={24} color="#FFF" />
                                <p>Entrar</p>
                            </Link>
                            <Link to="/register" className="button-sidebar">
                                <GoPerson size={24} color="#FFF" />
                                <p>Registrar</p>
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </header>
    )
}