import "./NotFound.css";

import { Link } from "react-router-dom";

export function NotFound() {
    return (
        <section className="container notfound-container">
            <h1>Página não encontrada ❌!</h1>
            <Link to="/" className="link comeback" >Voltar para a página inicial.</Link>
        </section>
    );
}