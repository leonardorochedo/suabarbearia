// CONTEXT
import { useState, useContext } from 'react';
import { Context } from "../../../context/AppContext";

// RRD
import { Link } from "react-router-dom";

// COMPONENT
import { Input } from "../../../components/Input/Input";
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// MASK
import { cpf } from 'cpf-cnpj-validator'; 
import { IMaskInput } from 'react-imask';

// NOTIFY
import { toast } from "react-toastify";

// DATE
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format } from 'date-fns';

// API
import axios from 'axios';

export function UserRegister() {

    const [user, setUser] = useState({});
    const [selectedDate, setSelectedDate] = useState();
    const { authenticatedUser, authenticatedBarbershop, authenticatedEmployee, UserRegister } = useContext(Context);

    function handleChangeInput(e) {
        if (e.target.name == "cpf") {
            setUser({...user, cpf: e.target.value.replace(/[^\d]/g, '')});
            return
        }

        setUser({...user, [e.target.name]: e.target.value});
    };

    function handleDateChange(date) {
        setSelectedDate(date);
        console.log(format(date, 'yyyy-MM-dd'));
        setUser({ ...user, birth: format(date, 'yyyy-MM-dd')});
    };

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

        toast.info("Carregando...", {
            position: "top-right",
            autoClose: 3500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });

        UserRegister(formattedUser);
    };

    return (
        <section className="container container-form">
            {authenticatedUser || authenticatedBarbershop || authenticatedEmployee
            ? (
                <>
                    <RedirectAuth title="Você já está autenticado." comebackText="Voltar para a página inicial." toURL="/" />
                </>
            ) : (
                <>
                    <h1 className="title">Registrar como Cliente!</h1>
                    <form onSubmit={handleSubmit} className="form-container">
                        <Input type="name" name="name" id="name" minLength={5} handleChangeInput={handleChangeInput} text="Nome" placeholder="Digite seu nome" />
                        <Input type="email" name="email" id="email" handleChangeInput={handleChangeInput} text="E-mail" placeholder="Digite seu e-mail" />
                        <div className="form-input">
                            <label htmlFor="cpf">CPF:</label>
                            <IMaskInput mask={"000.000.000-00"} name="cpf" id="cpf" onChange={handleChangeInput} placeholder="Digite seu CPF" className="imask" />
                        </div>
                        <div className="form-input">
                            <label htmlFor="birth">Data de Nascimento:</label>
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
                        <Input type="street" name="street" id="street" minLength={6} handleChangeInput={handleChangeInput} value={user.street} text="Rua" placeholder="Digite sua rua" />
                        <Input type="number" name="number" id="number" handleChangeInput={handleChangeInput} text="Número" placeholder="Digite o número da rua" />
                        <Input type="neighborhood" name="neighborhood" id="neighborhood" handleChangeInput={handleChangeInput} value={user.neighborhood} text="Bairro" placeholder="Digite seu bairro" />
                        <Input type="city" name="city" id="city" handleChangeInput={handleChangeInput} value={user.city} text="Cidade" placeholder="Digite sua cidade" />
                        <Input type="state" name="state" id="state" handleChangeInput={handleChangeInput} value={user.state} text="Estado" placeholder="Digite sua estado" />
                        <Input type="complement" name="complement" id="complement" handleChangeInput={handleChangeInput} text="Complemento" placeholder="Complemento se houver" />
                        <Input type="password" name="password" id="password" minLength={8} handleChangeInput={handleChangeInput} text="Senha" placeholder="Digite sua senha" />
                        <Input type="password" name="confirmpassword" id="confirmpassword" minLength={8} handleChangeInput={handleChangeInput} text="Confirme sua senha" placeholder="Confirme sua senha" />
                        <div className="form-buttons">
                            <input type="submit" value="Entrar" />
                            <p>Já possui uma conta? <Link to="/user/login" className='link'><span>Clique aqui.</span></Link></p>
                        </div>
                    </form>
                </>
            )}
        </section>
    )
}