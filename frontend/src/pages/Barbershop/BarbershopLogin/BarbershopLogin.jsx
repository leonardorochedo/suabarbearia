// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// NOTIFY
import { SuccesNotification } from '../../../utils/SuccessNotification/SuccessNotification';
import { toast } from "react-toastify";

// API
import api from '../../../utils/api';

export function BarbershopLogin() {

    const [barbershop, setBarbershop] = useState({});
    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee, BarbershopLogin } = useContext(Context);

    function handleChangeInput(e) {
        setBarbershop({...barbershop, [e.target.name]: e.target.value});
    };

    function verifyData() {

        // Valid email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(barbershop.email)) {
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

        BarbershopLogin(barbershop);
    };

    async function forgotPassword() {
        if (!barbershop.email) {
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

        api.post(`/barbershops/sendemailpassword/${barbershop.email}`).then(async (response) => {
            await SuccesNotification(response.data.message);
        })
    };

    return (
        <section className="container container-form">
            {authenticatedUser || authenticatedBarbershop || authenticatedEmployee
            ? (
                <>
                    <RedirectAuth title="Você já está autenticado." comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Entrar como Barbearia!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} text="E-mail" placeholder="Digite seu e-mail" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <div className='container-forgot-password'>
                            <p className="forgot-password" onClick={forgotPassword}>Esqueceu sua senha?</p>
                        </div>
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Não tem uma conta? <Link to="/barbershop/register" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}