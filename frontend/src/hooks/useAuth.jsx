import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// NOTIFY
import { SuccesNotification } from "../utils/SuccessNotification/SuccessNotification";
import { toast } from "react-toastify";

import api from "../utils/api";

export function useAuth() {

    const [authenticated, setAuthenticate] = useState(false);
    const navigate = useNavigate();

    // Token managment
    useEffect(() => {
        const token = localStorage.getItem('token');
        const tokenExpiration = localStorage.getItem('tokenExpiration');

        if (token && tokenExpiration) {
            const expirationTimestamp = parseInt(tokenExpiration);
            if (Date.now() < expirationTimestamp) {
                // O token está dentro do prazo de validade
                api.defaults.headers.Authorization = `Bearer ${JSON.parse(token)}`;
                setAuthenticate(true);
            } else {
                // O token expirou, limpar o localStorage
                localStorage.removeItem('token');
                localStorage.removeItem('tokenExpiration');
            }
        }
    }, []);

    // AuthData
    async function authUser(token) {
        const expirationTimestamp = Date.now() + 24 * 60 * 60 * 1000; // 24 horas de exp pro token

        setAuthenticate(true);
        localStorage.setItem('token', JSON.stringify(token));
        localStorage.setItem('tokenExpiration', expirationTimestamp.toString());
        navigate('/');
        window.location.reload(true); // dar um refresh quando redirecionar
    };

    // API's Methods
    async function UserRegister(user) {

        let msgText = '';

        try {
            const data = await api.post('/users/signup', user).then((response) => {
                msgText = response.data.message;
                return response.data;
            })

            await SuccesNotification(msgText);

            await authUser(data.token);
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

            await authUser(data.token);
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

            setAuthenticate(false);
            localStorage.removeItem('token');
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

            await authUser(token);
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

    async function UserLogout() {
        const msgText = 'Logout realizado com sucesso!';

        await SuccesNotification(msgText);

        // logout geral
        setAuthenticate(false);
        localStorage.removeItem('token');
        localStorage.removeItem('tokenExpiration');
        api.defaults.headers.Authorization = undefined;
        navigate('/');
        window.location.reload(true);
    };

    return { authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserChangePassword, UserLogout }
}