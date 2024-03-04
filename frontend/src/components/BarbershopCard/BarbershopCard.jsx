// IMAGES
import { RoundImage } from "../RoundImage/RoundImage";
import userNoImage from "../../assets/images/nopic.png";

// ICONS
import { FaPhoneAlt } from "react-icons/fa";
import { BiSolidCity } from "react-icons/bi";
import { TbClockHour9 } from "react-icons/tb";
import { FaMapPin } from "react-icons/fa";


import "./BarbershopCard.css";

export function BarbershopCard({ barbershop }) {

    const address = barbershop.address;

    function formatHour(hour) {
        const parts = hour.split(':');
        const hourMinutes = parts.slice(0, 2).join(':');
        return hourMinutes;
    }

    return (
        <>
            {barbershop.name != "Barbearia excluída!" && (
                <div className="barbershop-card">
                    <div className="barbershop-image-text">
                        {barbershop.image ? (
                            <RoundImage src={barbershop.image} alt={barbershop.name} size="rem3" />
                        ) : (
                            <RoundImage src={userNoImage} alt={barbershop.name} size="rem3" />
                        )}
                        <p className="title-barbershop">{barbershop.name}</p>
                    </div>
                    <div className="barbershop-infos">
                        <div className="barbershop-info">
                            <FaPhoneAlt size={15} color="#2ab7eb" />
                            <p>{barbershop.phone}</p>
                        </div>
                        <div className="barbershop-info">
                            <TbClockHour9 size={15} color="#2ab7eb" />
                            <p>{formatHour(barbershop.openTime)} até {formatHour(barbershop.closeTime)}</p>
                        </div>
                        <div className="barbershop-info">
                            <FaMapPin size={15} color="#2ab7eb" />
                            <p>{address.street}, {address.number}</p>                            
                        </div>
                        <div className="barbershop-info">
                            <BiSolidCity size={15} color="#2ab7eb" />
                            <p>{address.city} - {address.state}</p>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}
