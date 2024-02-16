// CONTEXT
import { useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

import "./LoginOptions.css";

export function LoginOptions() {

    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee } = useContext(Context);

    return (
        <section className="container">
            {authenticatedUser || authenticatedBarbershop || authenticatedEmployee ? (
                <>
                    <RedirectAuth title="Você já está autenticado!" comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Selecione uma opção para realizar login:</h1>
                    <div className="button-options">
                        <Link to="/user/login" className="button-option">Cliente</Link>
                        <Link to="/barbershop/login" className="button-option">Barbearia</Link>
                        <Link to="/employee/login" className="button-option">Funcionário</Link>
                    </div>
                    <p>Não tem uma conta? <Link to="/register" className='link'><span>Clique aqui.</span></Link></p>
                </>
            )}
        </section>
    )
}