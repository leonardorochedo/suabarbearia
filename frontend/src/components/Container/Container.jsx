import "./Container.css";

export function Container({ children }) {
    return (
        <section className="container-component">
            {children}
        </section>
    );
}