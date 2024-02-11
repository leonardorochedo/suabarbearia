import { useContext } from "react";
import { Context } from "../../context/AppContext";
import { Link } from "react-router-dom";

import "./RedirectAuth.css";

export function RedirectAuth({ title, comebackText, toURL }) {

    const { UserLogout } = useContext(Context);

    function handleLogout() {
        UserLogout();
    };

    return (
        <>
            <h1 className="title">{title}</h1>
            <Link to={toURL} className="link comeback">{comebackText}</Link>
            {title === "Você já está autenticado." && (
                <>
                    <button className="logout-button" onClick={handleLogout}>
                        Sair da conta
                    </button>
                </>
            )}
        </>
    )
}