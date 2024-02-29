import "./Input.css";

export function Input({ 
    type,
    text,
    name,
    minLength,
    min,
    max,
    placeholder,
    handleChangeInput,
    id,
    value,
    multiple 
}) {

    return (
        <div className="form-input">
            <label htmlFor={name}>{text}:</label>
            <input 
                type={type} 
                name={name} 
                minLength={minLength !== undefined ? minLength : null}
                min={min !== undefined ? min : null}
                max={max !== undefined ? max : null}
                onChange={handleChangeInput} 
                id={id} 
                placeholder={placeholder} 
                value={value}
                {...(multiple ? {multiple} : '')}
            />
        </div>
    );
}