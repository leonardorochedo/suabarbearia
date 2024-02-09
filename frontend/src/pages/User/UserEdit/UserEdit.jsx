// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/UserContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RoundImage } from "../../../components/RoundImage/RoundImage";

// MASK
import { cpf } from 'cpf-cnpj-validator'; 
import { IMaskInput } from 'react-imask';

// NOTIFY
import { toast } from "react-toastify";

// API
import api from '../../../utils/api';
import axios from 'axios';

export function UserEdit() {

    const [user, setUser] = useState({
        id: "",
        name: "",
        email: "",
        cpf: "",
        birth: "",
        phone: "",
        address: {
          cep: "",
          street: "",
          number: "",
          neighborhood: "",
          city: "",
          state: "",
          complement: "",
        },
        password: "",
        confirmpassword: "",
    });
    const [preview, setPreview] = useState()
    const { authenticated, UserEdit } = useContext(Context);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['user'], () =>
        api.get(`/users/profile`).then((response) => {
            if (!user.name) {
                setUser({
                    id: response.data.data.id,
                    image: response.data.data.image,
                    name: response.data.data.name,
                    email: response.data.data.email,
                    cpf: response.data.data.cpf,
                    birth: response.data.data.birth,
                    phone: response.data.data.phone,
                    cep: response.data.data.address.cep,
                    street: response.data.data.address.street,
                    number: response.data.data.address.number,
                    neighborhood: response.data.data.address.neighborhood,
                    city: response.data.data.address.city,
                    state: response.data.data.address.state,
                    complement: response.data.data.address.complement,
                    password: "",
                    confirmpassword: "",
                })
            }
        })
    );

    function handleChangeInput(e) {
        if (e.target.name == "cpf") {
            setUser({...user, cpf: e.target.value.replace(/[^\d]/g, '')});
            return
        }

        setUser({...user, [e.target.name]: e.target.value});
    };

    function onFileChange(e) {
        setPreview(e.target.files[0]) // preview da image
        setUser({...user, [e.target.name]: e.target.files[0]}) // setando a image no perfil
    }

    function verifyData() {

        // Valid email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(user.email)) {
            toast.error("E-mail inválido!", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })

            return false;
        };

        // Valid CPF
        if (!cpf.isValid(user.cpf)) {
            toast.error("CPF inválido!", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })

            return false;
        };

        return true;
    };

    async function validCEP(e) {
        try {
            if(e.target.value.length == 9) {
                const response = await axios.get(`https://viacep.com.br/ws/${e.target.value}/json/`);
                const data = response.data;
                
                setUser({
                    ...user,
                    cep: data.cep || "",
                    street: data.logradouro || "",
                    neighborhood: data.bairro || "",
                    city: data.localidade || "",
                    state: data.uf || ""
                });
            }
            
            return
        } catch (error) {
            return toast.error("CEP inválido!", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    };

    function handleSubmit(e) {
        e.preventDefault();

        if (!verifyData()) {
            return
        }

        const formattedUser = {
            image: preview ? user.image : null,
            name: user.name,
            email: user.email,
            cpf: user.cpf,
            birth: user.birth,
            phone: user.phone,
            address: {
                cep: user.cep,
                street: user.street,
                number: user.number,
                neighborhood: user.neighborhood,
                city: user.city,
                state: user.state,
                complement: user.complement
            },
            password: user.password,
            confirmpassword: user.confirmpassword
        };

        UserEdit(formattedUser, user.id);
    };

    return (
        <section className="container container-form">
            {authenticated
            ? (
                <>
                    <h1 className="title">Atualize seus dados!</h1>
                    {(user.image || preview) && (
                        <RoundImage src={
                            preview
                            ? URL.createObjectURL(preview)
                            : user.image}
                            alt={user.name}
                            size="rem12" />
                    )}
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="file" name="image" handleChangeInput={onFileChange} text="Imagem" />
                        <Input type="name" name="name" id="name" minLength={10} handleChangeInput={handleChangeInput} value={user.name || ""} text="Nome" placeholder="Digite seu nome" />
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} value={user.email || ""} text="E-mail" placeholder="Digite seu e-mail" />
                        <div className="form-input">
                            <label htmlFor="phone">Celular:</label>
                            <IMaskInput mask={"(00) 000000000"} name="phone" id="phone" onChange={handleChangeInput} value={user.phone || ""} placeholder="Digite seu celular" className="imask" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="cep">CEP:</label>
                            <IMaskInput mask={"00000-000"} name="cep" id="cep" onChange={validCEP} value={user.cep || ""} placeholder="Digite seu CEP" className="imask" />
                        </div>
                        <Input type="street" name="street" id="street" minLength={6} handleChangeInput={handleChangeInput} value={user.street || ""} text="Rua" placeholder="Digite sua rua" />
                        <Input type="text" name="number" id="number" handleChangeInput={handleChangeInput} value={user.number || ""} text="Número" placeholder="Digite o número da rua" />
                        <Input type="neighborhood" name="neighborhood" id="neighborhood" handleChangeInput={handleChangeInput} value={user.neighborhood || ""} text="Bairro" placeholder="Digite seu bairro" />
                        <Input type="city" name="city" id="city" handleChangeInput={handleChangeInput} value={user.city || ""} text="Cidade" placeholder="Digite sua cidade" />
                        <Input type="state" name="state" id="state" handleChangeInput={handleChangeInput} value={user.state || ""} text="Estado" placeholder="Digite sua estado" />
                        <Input type="complement" name="complement" id="complement" handleChangeInput={handleChangeInput} value={user.complement || ""} text="Complemento" placeholder="Complemento se houver" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                        </div>
                    </form>
                </>
            ) : (
                <>
                    <h1 className="title">Você não está autenticado!</h1>
                    <Link to="/user/login" className="link comeback">Voltar para a página inicial.</Link>
                </>
            )}
        </section>
    )
}