import "./Footer.css";

import logo from "../../assets/images/logo.png";

import { Link } from "react-router-dom";

export function Footer() {
    return (
        <footer>
            <Link to="/" className="link">
                <img src={logo} alt="Logo" />
            </Link>
            <div className="social-medias">
                <p>A melhor maneira de realizar agendamentos em sua barbearia!</p>
                <cite>@2024 Sua Barbearia</cite>
            </div>
        </footer>
    )
}