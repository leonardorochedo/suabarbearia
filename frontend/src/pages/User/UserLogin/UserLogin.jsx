// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/UserContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";

// NOTIFY
import { SuccesNotification } from '../../../utils/SuccessNotification/SuccessNotification';
import { toast } from "react-toastify";

// API
import api from '../../../utils/api';

export function UserLogin() {

    const [user, setUser] = useState({});
    const { authenticated, UserLogin } = useContext(Context);

    function handleChangeInput(e) {
        setUser({...user, [e.target.name]: e.target.value});
    };

    function verifyData() {

        // Valid email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(user.email)) {
            toast.error("E-mail inválido!", {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })

            return false;
        };

        return true;
    };

    function handleSubmit(e) {
        e.preventDefault();

        verifyData();

        UserLogin(user);
    };

    async function forgotPassword() {
        if (!user.email) {
            toast.error("E-mail inválido", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });

            return;
        };

        toast.info("Enviando...", {
            position: "top-right",
            autoClose: 3500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });

        api.post(`/users/sendemailpassword/${user.email}`).then(async (response) => {
            await SuccesNotification(response.data.message);
        })
    };

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
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <div className='container-forgot-password'>
                            <p className="forgot-password" onClick={forgotPassword}>Esqueceu sua senha?</p>
                        </div>
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