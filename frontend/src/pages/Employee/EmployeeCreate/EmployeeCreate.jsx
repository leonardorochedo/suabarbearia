// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// MASK
import { IMaskInput } from 'react-imask';

// NOTIFY
import { toast } from "react-toastify";

export function EmployeeCreate() {

    const [employee, setEmployee] = useState({});
    const { authenticatedBarbershop, EmployeeCreate } = useContext(Context);

    function handleChangeInput(e) {
        setEmployee({...employee, [e.target.name]: e.target.value});
    };

    function handleSubmit(e) {
        e.preventDefault();

        toast.info("Carregando...", {
            position: "top-right",
            autoClose: 3500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });

        EmployeeCreate(employee);
    };

    return (
        <section className="container container-form">
            {authenticatedBarbershop
            ? (
                <>
                    <h1 className="title">Crie um funcionário!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="name" name="name" id="name" minLength={5} handleChangeInput={handleChangeInput} text="Nome" placeholder="Digite o nome" />
                        <Input type="username" name="username" id="username" handleChangeInput={handleChangeInput} text="Usuário" placeholder="Digite o usuário" />
                        <div className="form-input">
                            <label htmlFor="phone">Celular:</label>
                            <IMaskInput mask={"(00) 000000000"} name="phone" id="phone" onChange={handleChangeInput} placeholder="Digite o celular" className="imask" />
                        </div>
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite a senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme a senha" placeholder="Confirme a senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Já possui uma conta? <Link to="/employee/login" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado." comebackText="Entre com sua conta." toURL="/barbershop/login" />
                </>
            )}
        </section>
    )
}