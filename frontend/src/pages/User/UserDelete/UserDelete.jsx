// CONTEXT
import { useContext } from "react";
import { Context } from "../../../context/AppContext";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// RRD
import { useParams } from "react-router-dom";

export function UserDelete() {

    const { authenticated, UserDelete } = useContext(Context)
    const { id } = useParams()

    function buttonSubmit(e) {
        e.preventDefault()

        UserDelete(id)
    }

    return (
        <section className="container">
            {authenticated ? (
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