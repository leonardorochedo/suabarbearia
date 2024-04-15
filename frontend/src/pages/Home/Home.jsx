import api from "../../utils/api";

// COMPONENT
import { SchedulingCard } from "../../components/SchedulingCard/SchedulingCard";

// CONTEXT
import { useState, useEffect, useContext } from 'react';
import { Context } from "../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// ICONS
import { FaRegPlusSquare } from "react-icons/fa";
import { SlMustache } from "react-icons/sl";

import "./Home.css";

export function Home() {

    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee } = useContext(Context);
    const [scheduling, setScheduling] = useState({});
    const [schedulings, setSchedulings] = useState([]);

    // Consulting API
    useEffect(() => {
        const today = new Date().toISOString().split('T')[0];

        if (authenticatedUser) {
            api.get(`/users/schedulings`).then((response) => {
                setScheduling({
                    title: response.data[0].service.title,
                    price: response.data[0].service.price,
                    employee: response.data[0].employee.name,
                    barbershop: response.data[0].barbershop.name,
                    date: response.data[0].date,
                    status: response.data[0].status
                })
            })

            return
        }

        if (authenticatedBarbershop) {
            api.get(`/barbershops/schedulings/${today}/${today}`).then((response) => {
                const mappedSchedulings = response.data.map((item) => ({
                    title: item.service.title,
                    price: item.service.price,
                    employee: item.employee.name,
                    barbershop: item.barbershop.name,
                    date: item.date,
                    status: item.status,
                    id: item.id
                }));
        
                setSchedulings(mappedSchedulings);
            });

            return
        }

        if (authenticatedEmployee) {
            api.get(`/employees/schedulings/${today}/${today}`).then((response) => {
                const mappedSchedulings = response.data.map((item) => ({
                    title: item.service.title,
                    price: item.service.price,
                    employee: item.employee.name,
                    barbershop: item.barbershop.name,
                    date: item.date,
                    status: item.status,
                    id: item.id
                }));
        
                setSchedulings(mappedSchedulings);
            });

            return
        }
    }, [authenticatedUser, authenticatedBarbershop, authenticatedEmployee])

    return (
        <section className="container container-home">
            {authenticatedUser && (
                <>
                    <h1>Realizar agendamento</h1>
                    <Link to="/scheduling/create" className="button-scheduling">
                        <FaRegPlusSquare size={20} color="#FFF" />
                        <p>Novo Agendamento</p>
                    </Link>
                    <h1>Próximos agendamentos</h1>
                    {scheduling.title ? (
                        <>
                            <SchedulingCard scheduling={scheduling} />
                        </>
                    ) : (
                        <>
                            <p style={{ marginTop: "2rem" }}>Sem agendamentos até o momento...</p>
                        </>
                    )}
                </>
            )}
            {authenticatedBarbershop && (
                <>
                    <h1>Agendamentos para hoje:</h1>
                    {(schedulings.length > 0) ? (
                        schedulings.map((item, index) => (
                            <SchedulingCard key={index} scheduling={item} />
                        ))
                    ) : (
                        <>
                            <p style={{ marginTop: "2rem" }}>Sem agendamentos até o momento...</p>
                        </>
                    )}
                </>
            )}
            {authenticatedEmployee && (
                <>
                    <h1>Agendamentos para hoje:</h1>
                    {(schedulings.length > 0) ? (
                        schedulings.map((item, index) => (
                            <SchedulingCard key={index} scheduling={item} />
                        ))
                    ) : (
                        <>
                            <p style={{ marginTop: "2rem" }}>Sem agendamentos até o momento...</p>
                        </>
                    )}
                </>
            )}
            {(!authenticatedUser && !authenticatedBarbershop && !authenticatedEmployee) && (
                <div className="welcome-text">
                    <h1>Bem-vindo à Sua Barbearia!</h1>
                    <p className="welcome-p">Somos um sistema de agendamentos para barbearias, dedicado a proporcionar a melhor experiência para você. Agende seu próximo corte de cabelo ou barba com facilidade, conveniência e estilo.</p>
                    <p className="welcome-p">Descubra a excelência no atendimento, profissionais qualificados e um ambiente acolhedor. Na Sua Barbearia, a sua satisfação é a nossa prioridade.</p>
                    <Link to="/register" className="button-scheduling">
                        <SlMustache size={24} color="#FFF" />
                        <p>QUERO UTILIZAR</p>
                    </Link>
                </div>            
            )}
        </section>
    )
}