// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

export function EmployeeLogin() {

    const [employee, setEmployee] = useState({});
    const { authenticatedemployee, authenticatedBarbershop, authenticatedEmployee, EmployeeLogin } = useContext(Context);

    function handleChangeInput(e) {
        setEmployee({...employee, [e.target.name]: e.target.value});
    };

    function handleSubmit(e) {
        e.preventDefault();

        EmployeeLogin(employee);
    };

    return (
        <section className="container container-form">
            {authenticatedemployee || authenticatedBarbershop || authenticatedEmployee
            ? (
                <>
                    <RedirectAuth title="Você já está autenticado." comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Entrar como Funcionário!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="username" name="username" id="username" handleChangeInput={handleChangeInput} text="Usuário" placeholder="Digite seu usuário" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Não tem uma conta? <Link to="/employee/register" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}