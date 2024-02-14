// CONTEXT
import { useContext } from "react";
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

import { toast } from "react-toastify";

export function EmployeeDelete() {

    const { authenticatedEmployee, EmployeeDelete } = useContext(Context)

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

        EmployeeDelete();
    }

    return (
        <section className="container">
            {authenticatedEmployee ? (
                <>
                    <h1 className="title">Tem certeza que deseja excluir sua conta?</h1>
                    <button className="delete-button" onClick={buttonSubmit}>Sim! Deletar</button>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/employee/login" />
                </>
            )}
        </section>
    );
}