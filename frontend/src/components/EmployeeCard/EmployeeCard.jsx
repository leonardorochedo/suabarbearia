// IMAGES
import { RoundImage } from "../RoundImage/RoundImage";
import userNoImage from "../../assets/images/nopic.png";

// ICONS
import { MdPerson } from "react-icons/md";
import { FaPhoneAlt } from "react-icons/fa";

import "./EmployeeCard.css";

export function EmployeeCard({ employee }) {

    return (
        <>
            {employee.name != "Colaborador excluído!" && (
                <div className="employee-card">
                    {employee.image ? (
                        <RoundImage src={employee.image} alt={employee.name} size="rem5" />
                    ) : (
                        <RoundImage src={userNoImage} alt={employee.name} size="rem5" />
                    )}
                    <div className="employee-text">
                        <MdPerson size={20} color="#2ab7eb" />
                        <p className="title-employee">{employee.name}</p>
                    </div>
                    <div className="employee-text">
                        <FaPhoneAlt size={15} color="#2ab7eb" />
                        <p>{employee.phone}</p>
                    </div>
                </div>
            )}
        </>
    );
}
