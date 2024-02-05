import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import api from "../utils/api";

export function useAuth() {

    const [authenticated, setAuthenticate] = useState(false)
    const navigate = useNavigate()

    // manipulando o token
    useEffect(() => {
        const token = localStorage.getItem('token')
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
    }, [])

    async function authUser(data) {
        const expirationTimestamp = Date.now() + 24 * 60 * 60 * 1000 // 24 horas de exp pro token

        setAuthenticate(true)
        localStorage.setItem('token', JSON.stringify(data.token))
        localStorage.setItem('tokenExpiration', expirationTimestamp.toString());
        navigate('/')
        window.location.reload(true) // dar um refresh quando redirecionar
    }

    async function UserRegister(user) {

        let msgText = ''

        try {
            const data = await api.post('/users/signup', user).then((response) => {
                msgText = response.data.message
                return response.data
            })

            await authUser(data)

            toast.success(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        } catch (err) {
            msgText = err.response.data.message // pegando o error message mandado da API
            toast.error(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    }

    async function UserLogin(user) {
        let msgText = ''

        try {
            const data = await api.post('/users/signin', user).then((response) => {
                msgText = response.data.message
                return response.data
            })

            await authUser(data)
            
            toast.success(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    }

    async function UserDelete(id) {
        let msgText = ''

        try {
            const data = await api.delete(`/users/delete/${id}`).then((response) => {
                msgText = response.data.message
                return response.data
            })

            setAuthenticate(false)
            localStorage.removeItem('token')
            api.defaults.headers.Authorization = undefined
            navigate('/')

            window.location.reload(true) // dar um refresh quando redirecionar
            
            toast.success(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    }

    async function UserEdit(user, id) {
        let msgText = ''

        try {
            const data = await api.patch(`/users/edit/${id}`, user, {
                headers: {
                    'Content-Type': 'multipart/form-data' // backend entender que esta indo uma imagem
                }
            }).then((response) => {
                msgText = response.data.message
                return response.data
            })
            
            navigate('/')
            window.location.reload(true)

            toast.success(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        } catch (err) {
            msgText = err.response.data.message
            toast.error(msgText, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    }

    function UserLogout() {
        const msgText = 'Logout realizado com sucesso!'

        // logout geral
        setAuthenticate(false)
        localStorage.removeItem('token')
        localStorage.removeItem('tokenExpiration')
        api.defaults.headers.Authorization = undefined
        navigate('/')

        window.location.reload(true) // dar um refresh quando redirecionar

        toast.success(msgText, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        })
    }


    return { authenticated, UserRegister, UserLogin, UserDelete, UserEdit, UserLogout }
}