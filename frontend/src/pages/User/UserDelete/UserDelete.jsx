// CONTEXT
import { useContext } from "react";
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// RRD
import { useParams } from "react-router-dom";

import { toast } from "react-toastify";

export function UserDelete() {

    const { authenticatedUser, UserDelete } = useContext(Context);
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

        UserDelete(id);
    }

    return (
        <section className="container">
            {authenticatedUser ? (
                <>
                    <h1 className="title">Tem certeza que deseja excluir sua conta?</h1>
                    <button className="delete-button" onClick={buttonSubmit}>Sim! Deletar</button>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/user/login" />
                </>
            )}
        </section>
    );
}