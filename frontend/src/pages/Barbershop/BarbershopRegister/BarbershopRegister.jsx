// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// MASK
import { cpf, cnpj } from 'cpf-cnpj-validator'; 
import { IMaskInput } from 'react-imask';

// NOTIFY
import { toast } from "react-toastify";

// DATE
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format } from 'date-fns';

// API
import axios from 'axios';

export function BarbershopRegister() {

    const [barbershop, setBarbershop] = useState({});
    const [selectedDate, setSelectedDate] = useState();
    const [documentType, setDocumentType] = useState('cpf');
    const { authenticated, BarbershopRegister } = useContext(Context);

    function handleChangeInput(e) {
        if (e.target.name == "document") {
            setBarbershop({...barbershop, document: e.target.value.replace(/[^\d]/g, '')});
            return
        }

        setBarbershop({...barbershop, [e.target.name]: e.target.value});
    };

    function handleDateChange(date) {
        setSelectedDate(date);
        console.log(format(date, 'yyyy-MM-dd'));
        setBarbershop({ ...barbershop, birth: format(date, 'yyyy-MM-dd')});
    };

    function handleDocumentTypeChange(e) {
        setDocumentType(e.target.value);
    };

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

        // Valid Document
        if (documentType === 'cpf' && !cpf.isValid(barbershop.document)) {
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

        if (documentType === 'cnpj' && !cnpj.isValid(barbershop.document)) {
            toast.error("CNPJ inválido!", {
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
                complement: barbershop.complement
            },
            openTime: barbershop.openTime,
            closeTime: barbershop.closeTime,
            password: barbershop.password,
            confirmpassword: barbershop.confirmpassword
        };

        BarbershopRegister(formattedBarbershop);
    };

    return (
        <section className="container container-form">
            {authenticated
            ? (
                <>
                    <RedirectAuth title="Você já está autenticado." comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Registrar como Barbearia!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="name" name="name" id="name" minLength={5} handleChangeInput={handleChangeInput} text="Barbearia" placeholder="Digite o nome da barbearia" />
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} text="E-mail" placeholder="Digite seu e-mail" />
                        <div className="form-input">
                            <label htmlFor="document">Documento:</label>
                            <div className="form-select-document">
                                <input
                                    type="radio"
                                    id="cpf"
                                    name="documentType"
                                    value="cpf"
                                    checked={documentType === 'cpf'}
                                    onChange={handleDocumentTypeChange}
                                />
                                CPF
                                <input
                                    type="radio"
                                    id="cnpj"
                                    name="documentType"
                                    value="cnpj"
                                    checked={documentType === 'cnpj'}
                                    onChange={handleDocumentTypeChange}
                                />
                                CNPJ
                            </div>
                            <IMaskInput mask={documentType === 'cpf' ? '000.000.000-00' : '00.000.000/0000-00'} name="document" id="document" onChange={handleChangeInput} placeholder={`Digite seu ${documentType === 'cpf' ? 'CPF' : 'CNPJ'}`} className="imask" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="birth">Data de Nascimento (Dono):</label>
                            <DatePicker selected={selectedDate} onChange={handleDateChange} dateFormat="dd/MM/yyyy" placeholderText="Selecione a data" className="date-picker" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="phone">Celular:</label>
                            <IMaskInput mask={"(00) 000000000"} name="phone" id="phone" onChange={handleChangeInput} placeholder="Digite seu celular" className="imask" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="cep">CEP:</label>
                            <IMaskInput mask={"00000-000"} name="cep" id="cep" onChange={validCEP} placeholder="Digite seu CEP" className="imask" />
                        </div>
                        <Input type="street" name="street" id="street" minLength={6} handleChangeInput={handleChangeInput} value={barbershop.street} text="Rua" placeholder="Digite sua rua" />
                        <Input type="number" name="number" id="number" handleChangeInput={handleChangeInput} text="Número" placeholder="Digite o número da rua" />
                        <Input type="neighborhood" name="neighborhood" id="neighborhood" handleChangeInput={handleChangeInput} value={barbershop.neighborhood} text="Bairro" placeholder="Digite seu bairro" />
                        <Input type="city" name="city" id="city" handleChangeInput={handleChangeInput} value={barbershop.city} text="Cidade" placeholder="Digite sua cidade" />
                        <Input type="state" name="state" id="state" handleChangeInput={handleChangeInput} value={barbershop.state} text="Estado" placeholder="Digite sua estado" />
                        <Input type="complement" name="complement" id="complement" handleChangeInput={handleChangeInput} text="Complemento" placeholder="Complemento se houver" />
                        <Input type="time" name="openTime" id="openTime" handleChangeInput={handleChangeInput} text="Horário de Abertura" placeholder="" />
                        <Input type="time" name="closeTime" id="closeTime" handleChangeInput={handleChangeInput} text="Horário de Encerramento" placeholder="" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Já possui uma conta? <Link to="/barbershop/login" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}