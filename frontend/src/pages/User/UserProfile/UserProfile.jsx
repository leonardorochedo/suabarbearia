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

export function UserProfile() {

    const [user, setUser] = useState({
        id: "",
        name: "",
        email: "",
        cpf: "",
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
        }
    });
    const { authenticatedUser } = useContext(Context);

    const { data, isLoading, isError } = useQuery(['user'], () =>
        api.get(`/users/profile`).then((response) => {
            setUser({
                id: response.data.data.id,
                image: response.data.data.image,
                name: response.data.data.name,
                email: response.data.data.email,
                cpf: response.data.data.cpf,
                birth: response.data.data.birth,
                phone: response.data.data.phone,
                cep: response.data.data.address.cep,
                street: response.data.data.address.street,
                number: response.data.data.address.number,
                neighborhood: response.data.data.address.neighborhood,
                city: response.data.data.address.city,
                state: response.data.data.address.state,
                complement: response.data.data.address.complement
            })
        })
    );

    function formatData(data) {
        const partesData = data.split("-");
        
        const dataFormatada = partesData[2] + "/" + partesData[1] + "/" + partesData[0];
        
        return dataFormatada;
    }

    return (
        <section className="container">
            {authenticatedUser
            ? (
                <>
                    {isLoading ? (
                        <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                    ) : (
                        <>
                            <div className="profile-profile">
                                {user.image ? (
                                    <RoundImage src={user.image} alt={user.name} size="rem5" />
                                ) : (
                                    <RoundImage src={userNoImage} alt={user.name} size="rem5" />
                                )}
                                <h1>{user.name}</h1>
                                <LineData>
                                    <IoIosMail size={20} color="#2ab7eb" />
                                    <p>{user.email}</p>
                                </LineData>
                                <LineData>
                                    <FaPhoneAlt size={20} color="#2ab7eb" />
                                    <p>{user.phone}</p>
                                </LineData>
                                <LineData>
                                    <BsFillPersonVcardFill size={20} color="#2ab7eb" />
                                    <p>{user.cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')}</p>
                                </LineData>
                                <LineData>
                                    <BsCalendarDateFill size={20} color="#2ab7eb" />
                                    <p>{formatData(user.birth)}</p>
                                </LineData>
                                <LineData>
                                    <IoMapSharp size={20} color="#2ab7eb" />
                                    <p>{user.street} - {user.number}</p>  
                                </LineData>
                                <LineData>
                                    <SiGooglemaps size={20} color="#2ab7eb" />
                                    <p>{user.cep}</p>
                                </LineData>
                                <LineData>
                                    <FaHouse size={20} color="#2ab7eb" />
                                    <p>{user.neighborhood}</p>
                                </LineData>
                                <LineData>
                                    <BiSolidCity size={20} color="#2ab7eb" />
                                    <p>{user.city} - {user.state}</p> 
                                </LineData>
                                <LineData>
                                    <PiGarageFill size={20} color="#2ab7eb" />
                                    <p>{user.complement || "Sem complemento"}</p>
                                </LineData>
                            </div>
                            <div className="profile-button-options">
                                <Link to="/user/edit" className="button-option button-option-edit">Atualizar Dados</Link>
                                <Link to="/user/changepassword" className="button-option button-option-password">Alterar Senha</Link>
                                <Link to={`/user/delete/${user.id}`} className="button-option button-option-delete">Deletar Conta</Link>
                            </div>
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