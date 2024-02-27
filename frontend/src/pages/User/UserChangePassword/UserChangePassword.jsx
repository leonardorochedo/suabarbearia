// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";

export function UserChangePassword() {

    const [user, setUser] = useState({});
    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee, UserChangePassword, UserChangePasswordWithToken } = useContext(Context);
    const token = new URLSearchParams(location.search).get('token');

    function handleChangeInput(e) {
        setUser({...user, [e.target.name]: e.target.value});
    };

    async function handleSubmit(e) {
        e.preventDefault();

        if (authenticatedUser) {
            UserChangePasswordWithToken(user);
        } else {
            UserChangePassword(user, token);
        }
    };

    return (
        <section className="container container-form">
            {authenticatedUser && (
                <>
                    <h1 className="title">Atualize sua senha!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua nova senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua nova senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                        </div>
                    </form>
                </>
            )}
            {authenticatedBarbershop || authenticatedEmployee && (
                <>
                    <RedirectAuth title="Você já está autenticado." comebackText="Voltar para a página inicial." toURL="/" />
                </>
            )}
            {!authenticatedUser && (
                <>
                    <h1 className="title">Crie sua senha!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua nova senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua nova senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                            <p>Lembrou de sua senha? <Link to="/user/login" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}