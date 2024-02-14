// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RoundImage } from "../../../components/RoundImage/RoundImage";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// MASK
import { IMaskInput } from 'react-imask';

// API
import api from '../../../utils/api';

export function EmployeeEdit() {

    const [employee, setEmployee] = useState({
        id: "",
        name: "",
        username: "",
        phone: "",
        password: "",
        confirmpassword: "",
    });
    const [preview, setPreview] = useState()
    const { authenticatedEmployee, EmployeeEdit } = useContext(Context);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['employee'], () =>
        api.get(`/employees/profile`).then((response) => {
            if (!employee.name) {
                setEmployee({
                    id: response.data.data.id,
                    name: response.data.data.name,
                    username: response.data.data.username,
                    phone: response.data.data.phone,
                    password: "",
                    confirmpassword: "",
                })
            }
        })
    );

    function handleChangeInput(e) {
        setEmployee({...employee, [e.target.name]: e.target.value});
    };

    function onFileChange(e) {
        setPreview(e.target.files[0]) // preview da image
        setEmployee({...employee, [e.target.name]: e.target.files[0]}) // setando a image no perfil
    }

    function handleSubmit(e) {
        e.preventDefault();

        EmployeeEdit(employee, employee.id);
    };

    return (
        <section className="container container-form">
            {authenticatedEmployee
            ? (
                <>
                    <h1 className="title">Atualize seus dados!</h1>
                    {(employee.image || preview) && (
                        <RoundImage src={
                            preview
                            ? URL.createObjectURL(preview)
                            : employee.image}
                            alt={employee.name}
                            size="rem12" />
                    )}
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="file" name="image" handleChangeInput={onFileChange} text="Imagem" />
                        <Input type="name" name="name" id="name" minLength={5} handleChangeInput={handleChangeInput} value={employee.name || ""} text="Nome" placeholder="Digite seu nome" />
                        <Input type="username" name="username" id="username" handleChangeInput={handleChangeInput} value={employee.username || ""} text="Usuário" placeholder="Digite seu usuário" />
                        <div className="form-input">
                            <label htmlFor="phone">Celular:</label>
                            <IMaskInput mask={"(00) 000000000"} name="phone" id="phone" onChange={handleChangeInput} value={employee.phone || ""} placeholder="Digite seu celular" className="imask" />
                        </div>
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                        </div>
                    </form>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/employee/login" />
                </>
            )}
        </section>
    )
}