import { FaCalendarAlt } from "react-icons/fa";
import { GrStatusCriticalSmall } from "react-icons/gr";
import { IoPersonCircleSharp } from "react-icons/io5";
import { IoIosPricetags } from "react-icons/io";
import { FaScissors, FaShop } from "react-icons/fa6";

import "./SchedulingCard.css";

export function SchedulingCard({ scheduling }) {
    
    // Price
    const formattedPrice = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(scheduling.price);

    // Status
    const getStatusInPortuguese = (status) => {
        const statusMap = {
            FINISHED: 'Concluído',
            CANCELED: 'Cancelado',
            FOUL: 'Faltou',
            PENDING: 'Pendente',
            WAITING_PAYMENT: 'Aguardando Pagamento',
        };

        return statusMap[status] || status;
    };

    const formattedStatus = getStatusInPortuguese(scheduling.status);

    // Date
    const dateObject = new Date(scheduling.date);
    
    const formatDate = (date) => {
        const options = {
            day: 'numeric',
            month: 'numeric',
            year: '2-digit',
            hour: 'numeric',
            minute: 'numeric',
        };

        try {
            return new Intl.DateTimeFormat('pt-BR', options).format(date);
        } catch (error) {
            return "Data Inválida";
        }
    };

    const formattedDate = formatDate(dateObject);

    return (
        <div className="scheduling-card">
            <div className="scheduling-option scheduling-option-two">
                <div className="scheduling-element">
                    <FaScissors size={20} color="grey" />
                    <p>{scheduling.title}</p>
                </div>
                <div className="scheduling-element">
                    <IoIosPricetags size={20} color="green" />
                    <p>{formattedPrice}</p>
                </div>
            </div>
            <div className="scheduling-option scheduling-option-two">
                <div className="scheduling-element">
                    <FaShop size={20} color="black" />
                    <p>{scheduling.barbershop}</p>
                </div>
                <div className="scheduling-element">
                    <IoPersonCircleSharp size={20} color="black" />
                    <p>{scheduling.employee}</p>
                </div>
            </div>
            <div className="scheduling-option">
                <GrStatusCriticalSmall size={20} color="orange" />
                <p>{formattedStatus}</p>
            </div>
            <div className="scheduling-option">
                <FaCalendarAlt size={20} color="orange" />
                <p>{formattedDate}</p>
            </div>
        </div>
    );
}
