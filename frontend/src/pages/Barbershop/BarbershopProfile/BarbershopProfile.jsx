// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";
import { Link } from 'react-router-dom';

// COMPONENT
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';
import ReactLoading from 'react-loading';
import { LineData } from '../../../components/LineData/LineData';

// IMAGE
import { RoundImage } from "../../../components/RoundImage/RoundImage";
import userNoImage from "../../../assets/images/nopic.png";

// API
import api from '../../../utils/api';

// ICONS
import { IoIosMail } from "react-icons/io";
import { FaPhoneAlt } from "react-icons/fa";
import { BsFillPersonVcardFill } from "react-icons/bs";
import { BsCalendarDateFill } from "react-icons/bs";
import { IoMapSharp } from "react-icons/io5";
import { SiGooglemaps } from "react-icons/si";
import { BiSolidCity } from "react-icons/bi";
import { FaHouse } from "react-icons/fa6";
import { PiGarageFill } from "react-icons/pi";
import { TbClockHour9 } from "react-icons/tb";

export function BarbershopProfile() {

    const [barbershop, setBarbershop] = useState({
        id: "",
        name: "",
        email: "",
        document: "",
        birth: "",
        phone: "",
        address: {
          cep: "",
          street: "",
          number: "",
          neighborhood: "",
          city: "",
          state: "",
          complement: "",
        },
        openTime: "",
        closeTime: ""
    });
    const { authenticatedBarbershop } = useContext(Context);

    const { data, isLoading, isError } = useQuery(['barbershop'], () =>
        api.get(`/barbershops/profile`).then((response) => {
            setBarbershop({
                id: response.data.data.id,
                image: response.data.data.image,
                name: response.data.data.name,
                email: response.data.data.email,
                document: response.data.data.document,
                birth: response.data.data.birth,
                phone: response.data.data.phone,
                cep: response.data.data.address.cep,
                street: response.data.data.address.street,
                number: response.data.data.address.number,
                neighborhood: response.data.data.address.neighborhood,
                city: response.data.data.address.city,
                state: response.data.data.address.state,
                complement: response.data.data.address.complement,
                openTime: response.data.data.openTime,
                closeTime: response.data.data.closeTime
            })
        })
    );

    function formatData(data) {
        const partesData = data.split("-");
        
        const dataFormatada = partesData[2] + "/" + partesData[1] + "/" + partesData[0];
        
        return dataFormatada;
    }

    function formatDocument(document) {
        if (document.length === 11) {
          return document.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
        } else if (document.length === 14) {
          return document.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
        } else {
          return document;
        }
    }

    function formatHour(hour) {
        const parts = hour.split(':');
        const hourMinutes = parts.slice(0, 2).join(':');
        return hourMinutes;
    }

    return (
        <section className="container">
            {authenticatedBarbershop
            ? (
                <>
                    {isLoading ? (
                        <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                    ) : (
                        <>
                            <div className="profile-profile">
                                {barbershop.image ? (
                                    <RoundImage src={barbershop.image} alt={barbershop.name} size="rem5" />
                                ) : (
                                    <RoundImage src={userNoImage} alt={barbershop.name} size="rem5" />
                                )}
                                <h1>{barbershop.name}</h1>
                                <LineData>
                                    <IoIosMail size={20} color="#2ab7eb" />
                                    <p>{barbershop.email}</p>
                                </LineData>
                                <LineData>
                                    <FaPhoneAlt size={20} color="#2ab7eb" />
                                    <p>{barbershop.phone}</p>
                                </LineData>
                                <LineData>
                                    <BsFillPersonVcardFill size={20} color="#2ab7eb" />
                                    <p>{formatDocument(barbershop.document)}</p>
                                </LineData>
                                <LineData>
                                    <BsCalendarDateFill size={20} color="#2ab7eb" />
                                    <p>{formatData(barbershop.birth)}</p>
                                </LineData>
                                <LineData>
                                    <IoMapSharp size={20} color="#2ab7eb" />
                                    <p>{barbershop.street} - {barbershop.number}</p>  
                                </LineData>
                                <LineData>
                                    <SiGooglemaps size={20} color="#2ab7eb" />
                                    <p>{barbershop.cep}</p>
                                </LineData>
                                <LineData>
                                    <FaHouse size={20} color="#2ab7eb" />
                                    <p>{barbershop.neighborhood}</p>
                                </LineData>
                                <LineData>
                                    <BiSolidCity size={20} color="#2ab7eb" />
                                    <p>{barbershop.city} - {barbershop.state}</p> 
                                </LineData>
                                <LineData>
                                    <PiGarageFill size={20} color="#2ab7eb" />
                                    <p>{barbershop.complement || "Sem complemento"}</p>
                                </LineData>
                                <LineData>
                                    <TbClockHour9 size={20} color="#2ab7eb" />
                                    <p>{formatHour(barbershop.openTime)} até {formatHour(barbershop.closeTime)}</p>
                                </LineData>
                            </div>
                            <div className="profile-button-options">
                                <Link to="/barbershop/edit" className="button-option button-option-edit">Atualizar Dados</Link>
                                <Link to="/barbershop/changepassword" className="button-option button-option-password">Alterar Senha</Link>
                                <Link to={`/barbershop/delete/${barbershop.id}`} className="button-option button-option-delete">Deletar Conta</Link>
                            </div>
                        </>
                    )}
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/barbershop/login" />
                </>
            )}
        </section>
    )
}