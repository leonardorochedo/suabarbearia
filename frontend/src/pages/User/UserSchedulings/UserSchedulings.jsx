import api from "../../../utils/api";

// COMPONENT
import { SchedulingCard } from "../../../components/SchedulingCard/SchedulingCard";

// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';
import ReactLoading from 'react-loading';

// RRD
import { Link } from "react-router-dom";

// NOTIFY
import { toast } from "react-toastify";

// ICONS
import { FaRegPlusSquare } from "react-icons/fa";

// DATE
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format } from 'date-fns';

import "./UserSchedulings.css";

export function UserSchedulings() {

    const [schedulings, setSchedulings] = useState([]);
    const [initialDate, setInitialDate] = useState();
    const [finalDate, setFinalDate] = useState();
    const [initialDateFormatted, setInitialDateFormatted] = useState();
    const [finalDateFormatted, setFinalDateFormatted] = useState();

    const { authenticatedUser } = useContext(Context);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['scheduling'], () =>
        api.get(`/users/schedulings`).then((response) => {
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
        })
    );

    function handleInitialDateChange(date) {
        setInitialDate(date);
        console.log(format(date, 'yyyy-MM-dd'));
        setInitialDateFormatted(format(date, 'yyyy-MM-dd'));
    };

    function handleFinalDateChange(date) {
        setFinalDate(date);
        console.log(format(date, 'yyyy-MM-dd'));
        setFinalDateFormatted(format(date, 'yyyy-MM-dd'));
    };

    function applyFilterSchedulings() {
        if (!initialDateFormatted || !finalDateFormatted) {
            return toast.error("Filtro inválido!", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }

        api.get(`/users/schedulings/${initialDateFormatted}/${finalDateFormatted}`).then((response) => {
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
        })

        return toast.success("Filtro aplicado!", {
            position: "top-right",
            autoClose: 3500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        })
    }

    return (
        <section className="container container-home">
            {authenticatedUser ? (
                <>
                    <h1>Realizar agendamento</h1>
                    <Link to="/" className="button-scheduling">
                        <FaRegPlusSquare size={20} color="#FFF" />
                        <p>Novo Agendamento</p>
                    </Link>
                    <h1>Seus agendamentos</h1>
                    <div className="date-schedulings">
                        <div className="date-scheduling">
                            <DatePicker selected={initialDate} onChange={handleInitialDateChange} dateFormat="dd/MM/yyyy" placeholderText="Selecione a data" className="date-picker date-scheduling-input" />
                            <p>até</p>
                            <DatePicker selected={finalDate} onChange={handleFinalDateChange} dateFormat="dd/MM/yyyy" placeholderText="Selecione a data" className="date-picker date-scheduling-input" />
                        </div>
                        <button onClick={applyFilterSchedulings} className="date-scheduling-button">Aplicar Filtro</button>
                    </div>
                    {isLoading ? (
                        <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                    ) : (
                        <>
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
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/user/login" />
                </>
            )}
        </section>
    )
}