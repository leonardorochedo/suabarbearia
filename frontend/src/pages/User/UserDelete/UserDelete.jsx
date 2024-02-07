// CONTEXT
import { useContext } from "react";
import { Context } from "../../../context/UserContext";

// RRD
import { Link, useParams } from "react-router-dom";

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
                    <h1 className="title">Você não está autenticado!</h1>
                    <Link to="/user/login" className="link comeback" >Entre com sua conta.</Link>
                </>
            )}
        </section>
    );
}