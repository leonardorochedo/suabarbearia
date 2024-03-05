import api from "../../../utils/api";

// COMPONENT
import { BarbershopCard } from "../../../components/BarbershopCard/BarbershopCard";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';
import ReactLoading from 'react-loading';

// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";

export function UserBarbershops() {

    const [barbershops, setBarbershops] = useState([]);

    const { authenticatedUser } = useContext(Context);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['barbershop'], () =>
        api.get(`/barbershops`).then((response) => {
            setBarbershops(response.data)
        })
    );

    return (
        <section className="container">
            {authenticatedUser ? (
                <>
                    <h1 className="title">Barbearias Disponíveis</h1>
                    {isLoading ? (
                        <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                    ) : (
                        <>
                            {(barbershops.length > 0) ? (
                                barbershops.map((item, index) => (
                                    <BarbershopCard key={index} barbershop={item} />
                                ))
                            ) : (
                                <>
                                    <p style={{ marginTop: "2rem" }}>Sem barbearias até o momento...</p>
                                </>
                            )}
                        </>
                    )}
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/user/login" />
                </>
            )}
        </section>
    )
}