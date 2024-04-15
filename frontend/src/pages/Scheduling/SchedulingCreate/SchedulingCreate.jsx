import api from "../../../utils/api";

// CONTEXT
import { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { Context } from "../../../context/AppContext";

// COMPONENT
import { BarbershopCard } from "../../../components/BarbershopCard/BarbershopCard";
import { EmployeeCard } from "../../../components/EmployeeCard/EmployeeCard";
import ReactLoading from 'react-loading';
import { RedirectAuth } from '../../../components/RedirectAuth/RedirectAuth';

// DATE
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import addDays from 'date-fns/addDays';
import { format } from 'date-fns';

// RRD
import { Link } from "react-router-dom";

// NOTIFY
import { toast } from "react-toastify";

// ICONS
import { IoMdClose } from "react-icons/io";

import "./SchedulingCreate.css";

export function SchedulingCreate() {

    const [scheduling, setScheduling] = useState({});

    // Form
    const [step, setStep] = useState(1);
    const [barbershops, setBarbershops] = useState([]);
    const [employees, setEmployees] = useState([]);
    const [services, setServices] = useState([]);
    const [selectedDate, setSelectedDate] = useState();
    const [formattedDate, setFormattedDate] = useState();
    const [openTimeBarbershop, setOpenTimeBarbershop] = useState("00:00:00");
    const [closeTimeBarbershop, setCloseTimeBarbershop] = useState("00:00:00");
    const [hoursSchedulings, setHoursSchedulings] = useState([]);

    // Titles
    const [barbershopTitle, setBarbershopTitle] = useState();
    const [employeeTitle, setEmployeeTitle] = useState();

    // Payment page
    const [paymentInfo, setPaymentInfo] = useState({ qrcode: "", link: "", copiaecola: "" });

    const { authenticatedUser, SchedulingCreate } = useContext(Context);

    // DatePicker
    const today = new Date();
    const sevenDaysFromNow = addDays(today, 7);
    const filterSundays = date => {
        return date.getDay() !== 0; // Retorna falso (false) para desabilitar domingos
    };

    // Consulting API
    const { dataBarbershops, isLoadingBarbershops } = useQuery(['barbershop'], () =>
        api.get(`/barbershops`).then((response) => {
            setBarbershops(response.data)
        })
    );

    async function handleBarbershopInput(id, title, openTime, closeTime) {
        setScheduling({...scheduling, "idBarbershop": id});
        setBarbershopTitle(title);
        setOpenTimeBarbershop(openTime)
        setCloseTimeBarbershop(closeTime)
        toggleModalBarbershop();

        await api.get(`/barbershops/employees/${id}`).then((response) => {
            setEmployees(response.data)
        })

        await api.get(`/barbershops/services/${id}`).then((response) => {
            setServices(response.data)
        })
    };

    function handleEmployeeInput(id, title) {
        setScheduling({...scheduling, "idEmployee": id});
        setEmployeeTitle(title);
        toggleModalEmployee();
    };

    function handleSelectOptions(e) {
        setScheduling({...scheduling, "idService": parseInt(e.target.options[e.target.selectedIndex].value, 10)});
    };

    function handleHourOptions(e) {
        const time = e.target.options[e.target.selectedIndex].value;

        const dateFormatted = `${formattedDate}T${time}:00`;

        setScheduling({...scheduling, "date": dateFormatted});
    };

    function handleDateChange(date) {
        setSelectedDate(date);

        setFormattedDate(format(date, 'yyyy-MM-dd'));

        const formattedDateApi = format(date, 'yyyy-MM-dd');

        api.get(`/barbershops/schedulings/${scheduling.idBarbershop}/${formattedDateApi}/${formattedDateApi}`).then((response) => {
            
            const times = response.data.map(item => {
                const dateTimeString = item.date;
                const timeString = dateTimeString.split('T')[1];
                return timeString.split(':')[0] + ':' + timeString.split(':')[1];
            });
    
            setHoursSchedulings(times);
        })
    };

    async function handleSubmit(e) {
        e.preventDefault();

        if (!scheduling.idBarbershop || !scheduling.idEmployee || !scheduling.idService || !scheduling.date) {
            return toast.error("Selecione os dados corretamente!", {
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

        const responseId = await SchedulingCreate(scheduling);

        if (responseId) {
            api.post(`/pix/generate-pix/${responseId}`).then((response) => {
                setPaymentInfo({
                    qrcode: response.data.qrcode.imagemQrcode,
                    link: response.data.qrcode.linkVisualizacao,
                    copiaecola: response.data.pix.pixCopiaECola
                })
            })
    
            setStep(2);
        }
    };

    // Step 2
    function copyCodeToEquipment() {
        navigator.clipboard.writeText(paymentInfo.copiaecola).then(() => {
            return toast.success("PIX copiado!", {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        })
    }

    // Modal's
    const [modalBarbershop, setModalBarbershop] = useState(false);

    const toggleModalBarbershop = () => {
        setModalBarbershop(!modalBarbershop);
    };

    if (modalBarbershop) {
        document.body.classList.add('active-modal')
    } else {
        document.body.classList.remove('active-modal')
    }

    const [modalEmployee, setModalEmployee] = useState(false);

    const toggleModalEmployee = () => {
        setModalEmployee(!modalEmployee);
    };

    if (modalEmployee) {
        document.body.classList.add('active-modal')
    } else {
        document.body.classList.remove('active-modal')
    }

    // Generate hours
    function generateTimes(hoursToExclude) {
        const times = [];

        // Converter o horário de abertura para minutos
        const [openHour, openMinute] = openTimeBarbershop.split(':').map(Number);
        const openTimeMinutes = openHour * 60 + openMinute;

        // Converter o horário de fechamento para minutos
        const [closeHour, closeMinute] = closeTimeBarbershop.split(':').map(Number);
        const closeTimeMinutes = closeHour * 60 + closeMinute;

        // Loop para gerar os horários entre o horário de abertura e fechamento
        let currentTimeMinutes = openTimeMinutes;
        while (currentTimeMinutes < closeTimeMinutes) {
            // Converter os minutos de volta para o formato HH:MM
            const hour = Math.floor(currentTimeMinutes / 60);
            const minute = currentTimeMinutes % 60;
            const currentTime = `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;

            // Verificar se o horário não está na lista de horários a excluir
            if (!hoursToExclude.includes(currentTime)) {
                times.push(currentTime);
            }

            // Incrementar o horário atual em 30 minutos
            currentTimeMinutes += 30;
        }

        return times;
    }

    const availableTimes = generateTimes(hoursSchedulings);

    return (
        <section className="container container-form">
            {authenticatedUser
            ? (
                <>
                    {step == 1 && (
                        <>
                            <h1 className="title">Crie um agendamento!</h1>
                                <div className="step-numbers">
                                    <p className="step-number active">1</p>
                                    <p className="step-number">2</p>
                                    <p className="step-number">3</p>
                                </div>
                                <form onSubmit={handleSubmit} className="form-container">
                                    <div className="form-input">
                                        <label htmlFor="">Barbearia:</label>
                                        {scheduling.idBarbershop ? (
                                            <div className="button-input" onClick={toggleModalBarbershop}>{barbershopTitle}</div>
                                        ) : (
                                            <div className="button-input" onClick={toggleModalBarbershop}>Selecione a barbearia</div>
                                        )}
                                    </div>
                                    {modalBarbershop && (
                                        <div className="modal">
                                            <div onClick={toggleModalBarbershop} className="overlay"></div>
                                            <div className="modal-content">
                                                <h2>Barbearias</h2>
                                                <div className="barbershops-list">
                                                {isLoadingBarbershops ? (
                                                    <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                                                ) : (
                                                    <>
                                                        {(barbershops.length > 0) ? (
                                                            barbershops.map((item, index) => (
                                                                <div style={{ cursor: "pointer" }} key={index} onClick={() => {
                                                                    handleBarbershopInput(item.id, item.name, item.openTime, item.closeTime)
                                                                }}>
                                                                    <BarbershopCard barbershop={item} />
                                                                </div>
                                                            ))
                                                        ) : (
                                                            <>
                                                                <p style={{ marginTop: "2rem" }}>Sem barbearias até o momento...</p>
                                                            </>
                                                        )}
                                                    </>
                                                )}
                                                </div>
                                                <button className="close-modal" onClick={toggleModalBarbershop}><IoMdClose size={20} color="black" /></button>
                                            </div>
                                        </div>
                                    )}
                                    <div className="form-input">
                                        <label htmlFor="">Funcionário:</label>
                                        {scheduling.idEmployee ? (
                                            <div className="button-input" onClick={toggleModalEmployee}>{employeeTitle}</div>
                                        ) : (
                                            <div className="button-input" onClick={toggleModalEmployee}>Selecione o funcionário</div>
                                        )}
                                    </div>
                                    {modalEmployee && (
                                        <div className="modal">
                                            <div onClick={toggleModalEmployee} className="overlay"></div>
                                            <div className="modal-content">
                                                <h2>Funcionários</h2>
                                                <div className="barbershops-list">
                                                {isLoadingBarbershops ? (
                                                    <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                                                ) : (
                                                    <>
                                                        {(employees.length > 0) ? (
                                                            employees.map((item, index) => (
                                                                <div style={{ cursor: "pointer" }} key={index} onClick={() => {
                                                                    handleEmployeeInput(item.id, item.name)
                                                                }}>
                                                                    <EmployeeCard employee={item} />
                                                                </div>
                                                            ))
                                                        ) : (
                                                            <>
                                                                <p style={{ marginTop: "2rem" }}>Sem barbearias até o momento...</p>
                                                            </>
                                                        )}
                                                    </>
                                                )}
                                                </div>
                                                <button className="close-modal" onClick={toggleModalEmployee}><IoMdClose size={20} color="black" /></button>
                                            </div>
                                        </div>
                                    )}
                                    <div className="form-input">
                                        <label htmlFor="idService">Serviço:</label>
                                        <select className='form-entry' name="idService" id="idService" onChange={handleSelectOptions}>
                                            <option value="" className="option" key="defaultValue">Selecione um serviço</option>
                                            {services.map((service, index) => (
                                                <option value={service.id} className="option" key={index}>{service.title} - R${service.price}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="form-input">
                                        <label htmlFor="date">Data:</label>
                                        <DatePicker selected={selectedDate} onChange={handleDateChange} dateFormat="dd/MM/yyyy" placeholderText="Selecione a data" className="date-picker" minDate={today} maxDate={sevenDaysFromNow} filterDate={filterSundays} />
                                    </div>
                                    <div className="form-input">
                                        <label htmlFor="idHour">Horário:</label>
                                        <select className='form-entry' name="idHour" id="idHour" onChange={handleHourOptions}>
                                            <option value="" className="option" key="defaultValue">Selecione um horário</option>
                                            {availableTimes.map((hour, index) => (
                                                <option value={hour} className="option" key={index}>{hour}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="form-buttons">
                                        <input type="submit" value="Agendar" />
                                    </div>
                                </form>
                        </>
                    )}
                    {step == 2 && (
                        <>
                            {paymentInfo.qrcode ? (
                                <>
                                    <h1 className="title">Realize o pagamento!</h1>
                                    <div className="step-numbers">
                                        <p className="step-number">1</p>
                                        <p className="step-number active">2</p>
                                        <p className="step-number">3</p>
                                    </div>
                                    <a href={paymentInfo.link} target="_blank">
                                        <img src={paymentInfo.qrcode} alt="QRCode de pagamento" />
                                    </a>
                                    <div className="payment-buttons">
                                        <button onClick={copyCodeToEquipment}>Copiar Código</button>
                                        <button className="finish" onClick={() => setStep(3)}>Realizei o Pagamento</button>
                                    </div>
                                </>
                            ) : (
                                <ReactLoading type={"spin"} color="#2ab7eb" height={50} width={50} />
                            )}
                        </>
                    )}
                    {step == 3 && (
                        <>
                            <h1 className="title">Obrigado!</h1>
                            <div className="step-numbers">
                                <p className="step-number">1</p>
                                <p className="step-number">2</p>
                                <p className="step-number active">3</p>
                            </div>
                            <p style={{ margin: '1rem' }}>Agora é só esperar o pagamento ser processado que o status do seu agendamento será atualizado no site!</p>
                            <Link to="/" className="button-option">Voltar ao início</Link>
                        </>
                    )}
                </>
            ) : (
                <>
                    <RedirectAuth title="Você não está autenticado." comebackText="Entre com sua conta." toURL="/user/login" />
                </>
            )}
        </section>
    )
}