// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// RRD
import { useParams } from "react-router-dom";

// API
import api from '../../../utils/api';

export function ServiceEdit() {

    const [service, setService] = useState({
        id: "",
        title: "",
        price: "",
    });
    const { authenticatedBarbershop, ServiceEdit } = useContext(Context);
    const { id } = useParams();

    // Consulting API
    const { data, isLoading, isError } = useQuery(['service'], () =>
        api.get(`/services/${id}`).then((response) => {
            if (!service.title) {
                setService({
                    id: response.data.id,
                    title: response.data.title,
                    price: response.data.price,
                })
            }
        })
    );

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

        ServiceEdit(service, service.id);
    };

    return (
        <section className="container container-form">
            {authenticatedBarbershop
            ? (
                <>
                    <h1 className="title">Atualize os dados do seviço!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="title" name="title" id="title" minLength={5} handleChangeInput={handleChangeInput} value={service.title || ""} text="Serviço" placeholder="Digite o serviço" />
                        <Input type="number" step="0.01" min="0.01" name="price" id="price" handleChangeInput={handleChangeInput} value={service.price || ""} text="Preço" placeholder="Exemplos: 50,00 109,90..." />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                        </div>
                    </form>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/barbershop/login" />
                </>
            )}
        </section>
    )
}