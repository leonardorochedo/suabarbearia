import "./RoundImage.css";

export function RoundImage( props ) {
    return (
        <img src={props.src} alt={props.alt} className={`round-image ${props.size}`} />
    );
}