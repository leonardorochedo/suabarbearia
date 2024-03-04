import api from "../../../utils/api";

// COMPONENT
import { BarbershopCard } from "../../../components/BarbershopCard/BarbershopCard";

// CONTEXT
import { useState } from 'react';
import { useQuery } from 'react-query';

export function UserBarbershops() {

    const [barbershops, setBarbershops] = useState([]);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['barbershop'], () =>
        api.get(`/barbershops`).then((response) => {
            setBarbershops(response.data)
        })
    );

    return (
        <section className="container">
            <h1 className="title">Barbearias Disponíveis</h1>
            {(barbershops.length > 0) ? (
                barbershops.map((item, index) => (
                    <BarbershopCard key={index} barbershop={item} />
                ))
            ) : (
                <>
                    <p style={{ marginTop: "2rem" }}>Sem barbearias até o momento...</p>
                </>
            )}
        </section>
    )
}