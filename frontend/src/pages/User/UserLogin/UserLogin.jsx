// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/UserContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";

export function UserLogin() {

    const [user, setUser] = useState({});
    const { authenticated, UserLogin } = useContext(Context);

    function handleChangeInput(e) {
        setUser({...user, [e.target.name]: e.target.value});
    }

    function handleSubmit(e) {
        e.preventDefault();

        UserLogin(user);
    }

    return (
        <section className="container container-form">
            {authenticated
            ? (
                <>
                    <h1 className="title">Você já está autenticado!</h1>
                    <Link to="/" className="link comeback">Voltar para a página inicial.</Link>
                </>
            ) : (
                <>
                    <h1 className="title">Entre com sua conta!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} text="E-mail" placeholder="Digite seu e-mail" />
                        <Input type="password" name="password" id="password" handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Não tem uma conta? <Link to="/user/register" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}