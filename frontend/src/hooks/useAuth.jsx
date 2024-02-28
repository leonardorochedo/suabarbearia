import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// NOTIFY
import { SuccesNotification } from "../utils/SuccessNotification/SuccessNotification";
import { toast } from "react-toastify";

import api from "../utils/api";

export function useAuth() {

    const [authenticatedUser, setAuthenticateUser] = useState(false);
    const [authenticatedBarbershop, setAuthenticateBarbershop] = useState(false);
    const [authenticatedEmployee, setAuthenticateEmployee] = useState(false);
    const navigate = useNavigate();

    // Token managment
    async function checkTokenValidity() {
        const tokenType = localStorage.getItem('tokenType');

        if (tokenType) {
            try {
                const response = await api.get(`/${tokenType}/profile`);
            
                return;
            } catch (err) {
                if (err.response.status == 401) {
                    setAuthenticateUser(false);
                    setAuthenticateBarbershop(false);
                    setAuthenticateEmployee(false);
                    localStorage.removeItem('token');
                    localStorage.removeItem('tokenType');
                    localStorage.removeItem('tokenExpiration');
                    api.defaults.headers.Authorization = undefined;
                    navigate('/');
                    window.location.reload(true);
                };
            }
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('token');
        const tokenExpiration = localStorage.getItem('tokenExpiration');

        if (token && tokenExpiration) {
            const expirationTimestamp = parseInt(tokenExpiration);
            if (Date.now() < expirationTimestamp) {
                // O token está dentro do prazo de validade
                api.defaults.headers.Authorization = `Bearer ${JSON.parse(token)}`;

                if (localStorage.getItem('tokenType') === 'users') { setAuthenticateUser(true); }

                if (localStorage.getItem('tokenType') === 'barbershops') { setAuthenticateBarbershop(true); }

                if (localStorage.getItem('tokenType') === 'employees') { setAuthenticateEmployee(true); }
            } else {
                // O token expirou, limpar o localStorage
                localStorage.removeItem('token');
                localStorage.removeItem('tokenType');
                localStorage.removeItem('tokenExpiration');
            }
        }
    }, []);

    useEffect(() => {
        checkTokenValidity();

        const intervalId = setInterval(checkTokenValidity, 3 * 60 * 1000);

        return () => clearInterval(intervalId);
    }, []);

    // AuthData
    async function authAccount(token, tokenType) {
        const expirationTimestamp = Date.now() + 24 * 60 * 60 * 1000; // 24 horas de exp pro token

        localStorage.setItem('token', JSON.stringify(token));
        localStorage.setItem('tokenType',tokenType);
        localStorage.setItem('tokenExpiration', expirationTimestamp.toString());

        if (localStorage.getItem('tokenType') === 'users') { setAuthenticateUser(true); }

        if (localStorage.getItem('tokenType') === 'barbershops') { setAuthenticateBarbershop(true); }

        if (localStorage.getItem('tokenType') === 'employees') { setAuthenticateEmployee(true); }

        navigate('/');
        window.location.reload(true);
    };

    // API's Methods

    // User
    async function UserRegister(user) {

        let msgText = '';

        try {
            const data = await api.post('/users/signup', user).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            await authAccount(data.token, "users");
        } catch (err) {
            msgText = err.response.data.message // pegando o error message mandado da API
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        }
    };

    async function UserLogin(user) {
        let msgText = '';

        try {
            const data = await api.post('/users/signin', user).then((response) => {
                msgText = response.data.message;
                return response.data;
            });

            await SuccesNotification(msgText);

            await authAccount(data.token, "users");
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function UserDelete(id) {
        let msgText = '';

        try {
            const data = await api.delete(`/users/delete/${id}`).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            setAuthenticateUser(false);
            localStorage.removeItem('token');
            localStorage.removeItem('tokenType');
            localStorage.removeItem('tokenExpiration');
            api.defaults.headers.Authorization = undefined;
            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function UserEdit(user, id) {
        let msgText = '';

        const userFormData = new FormData();

        userFormData.append('name', user.name);
        userFormData.append('email', user.email);
        userFormData.append('cpf', user.cpf);
        userFormData.append('birth', user.birth);
        userFormData.append('phone', user.phone);
        
        // Adicionar o objeto address ao FormData
        Object.entries(user.address).forEach(([key, value]) => {
            userFormData.append(`address.${key}`, value);
        });

        // Adicione a imagem ao FormData (se necessário)
        if (user.image) {
            userFormData.append('image', user.image);
        };

        userFormData.append('password', user.password);
        userFormData.append('confirmpassword', user.confirmpassword);

        try {
            const data = await api.patch(`/users/edit/${id}`, userFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            // Erro de imagem muito pesada
            if (err.response.status == 500) {
                return toast.error("Tamanho de imagem inválida!", {
                    position: "top-right",
                    autoClose: 3500,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            }

            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function UserChangePassword(user, token) {
        let msgText = '';

        try {
            api.defaults.headers.Authorization = `Bearer ${token}`;
    
            const data = await api.patch("/users/changepassword", user).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            await authAccount(token, "users");
        } catch (err) {
            msgText = err.response.data.message;
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function UserChangePasswordWithToken(user) {
        let msgText = '';

        try {
            const data = await api.patch("/users/changepassword", user).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message;
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    // Barbershop
    async function BarbershopRegister(barbershop) {

        let msgText = '';

        try {
            const data = await api.post('/barbershops/signup', barbershop).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            await authAccount(data.token, "barbershops");
        } catch (err) {
            msgText = err.response.data.message // pegando o error message mandado da API
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        }
    };

    async function BarbershopLogin(barbershop) {
        let msgText = '';

        try {
            const data = await api.post('/barbershops/signin', barbershop).then((response) => {
                msgText = response.data.message;
                return response.data;
            });

            await SuccesNotification(msgText);

            await authAccount(data.token, "barbershops");
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function BarbershopDelete(id) {
        let msgText = '';

        try {
            const data = await api.delete(`/barbershops/delete/${id}`).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            setAuthenticateBarbershop(false);
            localStorage.removeItem('token');
            localStorage.removeItem('tokenType');
            localStorage.removeItem('tokenExpiration');
            api.defaults.headers.Authorization = undefined;
            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function BarbershopEdit(barbershop, id) {
        let msgText = '';

        const barbershopFormData = new FormData();

        barbershopFormData.append('name', barbershop.name);
        barbershopFormData.append('email', barbershop.email);
        barbershopFormData.append('document', barbershop.document);
        barbershopFormData.append('birth', barbershop.birth);
        barbershopFormData.append('phone', barbershop.phone);
        
        // Adicionar o objeto address ao FormData
        Object.entries(barbershop.address).forEach(([key, value]) => {
            barbershopFormData.append(`address.${key}`, value);
        });

        // Adicione a imagem ao FormData (se necessário)
        if (barbershop.image) {
            barbershopFormData.append('image', barbershop.image);
        };

        barbershopFormData.append('openTime', barbershop.openTime);
        barbershopFormData.append('closeTime', barbershop.closeTime);
        barbershopFormData.append('password', barbershop.password);
        barbershopFormData.append('confirmpassword', barbershop.confirmpassword);

        try {
            const data = await api.patch(`/barbershops/edit/${id}`, barbershopFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            // Erro de imagem muito pesada
            if (err.response.status == 500) {
                return toast.error("Tamanho de imagem inválida!", {
                    position: "top-right",
                    autoClose: 3500,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            }

            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function BarbershopChangePassword(barbershop, token) {
        let msgText = '';

        try {
            api.defaults.headers.Authorization = `Bearer ${token}`;
    
            const data = await api.patch("/barbershops/changepassword", barbershop).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            await authAccount(token, "barbershops");
        } catch (err) {
            msgText = err.response.data.message;
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function BarbershopChangePasswordWithToken(barbershop) {
        let msgText = '';

        try {
            const data = await api.patch("/barbershops/changepassword", barbershop).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message;
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    // Employee
    async function EmployeeCreate(employee) {

        let msgText = '';

        try {
            const data = await api.post('/employees/create', employee).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message // pegando o error message mandado da API
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        }
    };

    async function EmployeeLogin(employee) {
        let msgText = '';

        try {
            const data = await api.post('/employees/signin', employee).then((response) => {
                msgText = response.data.message;
                return response.data;
            });

            await SuccesNotification(msgText);

            await authAccount(data.token, "employees");
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function EmployeeDelete() {
        let msgText = '';

        try {
            const data = await api.delete(`/employees/delete`).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            setAuthenticateEmployee(false);
            localStorage.removeItem('token');
            localStorage.removeItem('tokenType');
            localStorage.removeItem('tokenExpiration');
            api.defaults.headers.Authorization = undefined;
            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function EmployeeEdit(employee, id) {
        let msgText = '';

        try {
            const data = await api.patch(`/employees/edit/${id}`, employee, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            // Erro de imagem muito pesada
            if (err.response.status == 500) {
                return toast.error("Tamanho de imagem inválida!", {
                    position: "top-right",
                    autoClose: 3500,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            }

            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function EmployeeBarbershopDelete(id) {
        let msgText = '';

        try {
            const data = await api.delete(`/employees/barbershop/delete/${id}`).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function EmployeeBarbershopEdit(employee, id) {
        let msgText = '';

        try {
            const data = await api.patch(`/employees/barbershop/edit/${id}`, employee, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            // Erro de imagem muito pesada
            if (err.response.status == 500) {
                return toast.error("Tamanho de imagem inválida!", {
                    position: "top-right",
                    autoClose: 3500,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            }
            
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    // Service
    async function ServiceCreate(service) {

        let msgText = '';

        try {
            const data = await api.post('/services/create', service).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        }
    };

    async function ServiceEdit(service, id) {
        let msgText = '';

        try {
            const data = await api.patch(`/services/edit/${id}`, service).then((response) => {
                msgText = response.data.message;
                return response.data;
            });
            
            await SuccesNotification(msgText);

            navigate('/');
            window.location.reload(true);
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 3500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        };
    };

    async function Logout() {
        const msgText = 'Logout realizado com sucesso!';

        await SuccesNotification(msgText);

        // logout geral
        setAuthenticateUser(false);
        setAuthenticateBarbershop(false);
        setAuthenticateEmployee(false);
        localStorage.removeItem('token');
        localStorage.removeItem('tokenType');
        localStorage.removeItem('tokenExpiration');
        api.defaults.headers.Authorization = undefined;
        navigate('/');
        window.location.reload(true);
    };

    return { 
        authenticatedUser,
        authenticatedBarbershop,
        authenticatedEmployee,
        UserRegister,
        UserLogin,
        UserDelete,
        UserEdit,
        UserChangePassword,
        UserChangePasswordWithToken,
        BarbershopRegister,
        BarbershopLogin,
        BarbershopDelete,
        BarbershopEdit,
        BarbershopChangePassword,
        BarbershopChangePasswordWithToken,
        EmployeeCreate,
        EmployeeLogin,
        EmployeeDelete,
        EmployeeEdit,
        EmployeeBarbershopDelete,
        EmployeeBarbershopEdit,
        ServiceCreate,
        ServiceEdit,
        Logout
    }
}