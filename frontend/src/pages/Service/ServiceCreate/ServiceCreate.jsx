// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// NOTIFY
import { toast } from "react-toastify";

export function ServiceCreate() {

    const [service, setService] = useState({});
    const { authenticatedBarbershop, ServiceCreate } = useContext(Context);

    function handleChangeInput(e) {
        setService({...service, [e.target.name]: e.target.value});
    };

    function handleSubmit(e) {
        e.preventDefault();

        if (service.price <= 0) {
            toast.error("Preço inválido!", {
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
        }

        ServiceCreate(service);
    };

    return (
        <section className="container container-form">
            {authenticatedBarbershop
            ? (
                <>
                    <h1 className="title">Crie um serviço!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="title" name="title" id="title" minLength={5} handleChangeInput={handleChangeInput} text="Serviço" placeholder="Digite o serviço" />
                        <Input type="number" step="0.01" min="0.01" name="price" id="price" handleChangeInput={handleChangeInput} text="Preço" placeholder="Exemplos: 50,00 109,90..." />
                        <div className="form-buttons">
                            <input type="submit" value="Criar" />
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