// CONTEXT
import { useContext } from "react";
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// RRD
import { useParams } from "react-router-dom";

import { toast } from "react-toastify";

export function EmployeeBarbershopDelete() {

    const { authenticatedBarbershop, EmployeeBarbershopDelete } = useContext(Context);
    const { id } = useParams();

    function buttonSubmit(e) {
        e.preventDefault()

        toast.info("Carregando...", {
            position: "top-right",
            autoClose: 3500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });

        EmployeeBarbershopDelete(id);
    }

    return (
        <section className="container">
            {authenticatedBarbershop ? (
                <>
                    <h1 className="title">Tem certeza que deseja excluir este funcionário?</h1>
                    <button className="delete-button" onClick={buttonSubmit}>Sim! Deletar</button>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/barbershop/login" />
                </>
            )}
        </section>
    );
}