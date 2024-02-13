// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RoundImage } from "../../../components/RoundImage/RoundImage";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// MASK
import { cpf } from 'cpf-cnpj-validator'; 
import { IMaskInput } from 'react-imask';

// NOTIFY
import { toast } from "react-toastify";

// API
import api from '../../../utils/api';
import axios from 'axios';

export function BarbershopEdit() {

    const [barbershop, setBarbershop] = useState({
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
        openTime: "",
        closeTime: "",
        password: "",
        confirmpassword: "",
    });
    const [preview, setPreview] = useState();
    const { authenticatedBarbershop, BarbershopEdit } = useContext(Context);

    // Consulting API
    const { data, isLoading, isError } = useQuery(['barbershop'], () =>
        api.get(`/barbershops/profile`).then((response) => {
            if (!barbershop.name) {
                setBarbershop({
                    id: response.data.data.id,
                    image: response.data.data.image,
                    name: response.data.data.name,
                    email: response.data.data.email,
                    document: response.data.data.document,
                    birth: response.data.data.birth,
                    phone: response.data.data.phone,
                    cep: response.data.data.address.cep,
                    street: response.data.data.address.street,
                    number: response.data.data.address.number,
                    neighborhood: response.data.data.address.neighborhood,
                    city: response.data.data.address.city,
                    state: response.data.data.address.state,
                    complement: response.data.data.address.complement,
                    openTime: response.data.data.openTime,
                    closeTime: response.data.data.closeTime,
                    password: "",
                    confirmpassword: "",
                })
            }
        })
    );

    function handleChangeInput(e) {
        if (e.target.name == "document") {
            setBarbershop({...barbershop, document: e.target.value.replace(/[^\d]/g, '')});
            return
        }

        setBarbershop({...barbershop, [e.target.name]: e.target.value});
    };

    function onFileChange(e) {
        setPreview(e.target.files[0]) // preview da image
        setBarbershop({...barbershop, [e.target.name]: e.target.files[0]}) // setando a image no perfil
    }

    function verifyData() {

        // Valid email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(barbershop.email)) {
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

        return true;
    };

    async function validCEP(e) {
        try {
            if(e.target.value.length == 9) {
                const response = await axios.get(`https://viacep.com.br/ws/${e.target.value}/json/`);
                const data = response.data;
                
                setBarbershop({
                    ...barbershop,
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

        const formattedBarbershop = {
            image: preview ? barbershop.image : null,
            name: barbershop.name,
            email: barbershop.email,
            document: barbershop.document,
            birth: barbershop.birth,
            phone: barbershop.phone,
            address: {
                cep: barbershop.cep,
                street: barbershop.street,
                number: barbershop.number,
                neighborhood: barbershop.neighborhood,
                city: barbershop.city,
                state: barbershop.state,
                complement: barbershop.complement || ""
            },
            openTime: barbershop.openTime,
            closeTime: barbershop.closeTime,
            password: barbershop.password,
            confirmpassword: barbershop.confirmpassword
        };

        console.log(formattedBarbershop)

        BarbershopEdit(formattedBarbershop, barbershop.id);
    };

    return (
        <section className="container container-form">
            {authenticatedBarbershop
            ? (
                <>
                    <h1 className="title">Atualize seus dados!</h1>
                    {(barbershop.image || preview) && (
                        <RoundImage src={
                            preview
                            ? URL.createObjectURL(preview)
                            : barbershop.image}
                            alt={barbershop.name}
                            size="rem12" />
                    )}
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="file" name="image" handleChangeInput={onFileChange} text="Imagem" />
                        <Input type="name" name="name" id="name" minLength={5} handleChangeInput={handleChangeInput} value={barbershop.name || ""} text="Barbearia" placeholder="Digite o nome da barbearia" />
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} value={barbershop.email || ""} text="E-mail" placeholder="Digite seu e-mail" />
                        <div className="form-input">
                            <label htmlFor="phone">Celular:</label>
                            <IMaskInput mask={"(00) 000000000"} name="phone" id="phone" onChange={handleChangeInput} value={barbershop.phone || ""} placeholder="Digite seu celular" className="imask" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="cep">CEP:</label>
                            <IMaskInput mask={"00000-000"} name="cep" id="cep" onChange={validCEP} value={barbershop.cep || ""} placeholder="Digite seu CEP" className="imask" />
                        </div>
                        <Input type="street" name="street" id="street" minLength={6} handleChangeInput={handleChangeInput} value={barbershop.street || ""} text="Rua" placeholder="Digite sua rua" />
                        <Input type="text" name="number" id="number" handleChangeInput={handleChangeInput} value={barbershop.number || ""} text="Número" placeholder="Digite o número da rua" />
                        <Input type="neighborhood" name="neighborhood" id="neighborhood" handleChangeInput={handleChangeInput} value={barbershop.neighborhood || ""} text="Bairro" placeholder="Digite seu bairro" />
                        <Input type="city" name="city" id="city" handleChangeInput={handleChangeInput} value={barbershop.city || ""} text="Cidade" placeholder="Digite sua cidade" />
                        <Input type="state" name="state" id="state" handleChangeInput={handleChangeInput} value={barbershop.state || ""} text="Estado" placeholder="Digite sua estado" />
                        <Input type="complement" name="complement" id="complement" handleChangeInput={handleChangeInput} value={barbershop.complement || ""} text="Complemento" placeholder="Complemento se houver" />
                        <Input type="time" name="openTime" id="openTime" handleChangeInput={handleChangeInput} value={barbershop.openTime || ""} text="Horário de Abertura" placeholder="" />
                        <Input type="time" name="closeTime" id="closeTime" handleChangeInput={handleChangeInput} value={barbershop.closeTime || ""} text="Horário de Encerramento" placeholder="" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Atualizar" />
                        </div>
                    </form>
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado!" comebackText="Entre com sua conta." toURL="/barbershop/login" />
                </>
            )}
        </section>
    )
}