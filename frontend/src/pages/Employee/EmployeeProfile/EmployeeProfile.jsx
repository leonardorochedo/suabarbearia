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
import { MdPerson } from "react-icons/md";
import { FaPhoneAlt } from "react-icons/fa";
import { MdAttachMoney } from "react-icons/md";

export function EmployeeProfile() {

    const [employee, setEmployee] = useState({
        id: "",
        name: "",
        username: "",
        phone: "",
        commission: "",
        image: "",
    });
    const { authenticatedEmployee } = useContext(Context);

    const { data, isLoading, isError } = useQuery(['employee'], () =>
        api.get(`/employees/profile`).then((response) => {
            setEmployee({
                id: response.data.data.id,
                name: response.data.data.name,
                username: response.data.data.username,
                phone: response.data.data.phone,
                commission: response.data.data.commission,
                image: response.data.data.image
            })
        })
    );

    return (
        <section className="container">
            {authenticatedEmployee
            ? (
                <>
                    {isLoading ? (
                        <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                    ) : (
                        <>
                            <div className="profile-profile">
                                {employee.image ? (
                                    <RoundImage src={employee.image} alt={employee.name} size="rem5" />
                                ) : (
                                    <RoundImage src={userNoImage} alt={employee.name} size="rem5" />
                                )}
                                <h1>{employee.name}</h1>
                                <LineData>
                                    <MdPerson size={20} color="#2ab7eb" />
                                    <p>{employee.username}</p>
                                </LineData>
                                <LineData>
                                    <FaPhoneAlt size={20} color="#2ab7eb" />
                                    <p>{employee.phone}</p>
                                </LineData>
                                <LineData>
                                    <MdAttachMoney size={20} color="#2ab7eb" />
                                    <p>% {employee.commission}</p>
                                </LineData>
                            </div>
                            <div className="profile-button-options">
                                <Link to="/employee/edit" className="button-option button-option-edit">Atualizar Dados</Link>
                                <Link to={`/employee/delete`} className="button-option button-option-delete">Deletar Conta</Link>
                            </div>
                        </>
                    )}
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/employee/login" />
                </>
            )}
        </section>
    )
}