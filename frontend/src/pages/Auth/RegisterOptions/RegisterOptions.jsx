// CONTEXT
import { useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

import "./RegisterOptions.css";

export function RegisterOptions() {

    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee } = useContext(Context);

    return (
        <section className="container">
            {authenticatedUser || authenticatedBarbershop || authenticatedEmployee ? (
                <>
                    <RedirectAuth title="Você já está autenticado!" comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Selecione uma opção para realizar o cadastro:</h1>
                    <div className="button-options">
                        <Link to="/user/register" className="button-option">Cliente</Link>
                        <Link to="/barbershop/register" className="button-option">Barbearia</Link>
                    </div>
                    <p>Já tem uma conta? <Link to="/login" className='link'><span>Clique aqui.</span></Link></p>
                </>
            )}
        </section>
    )
}