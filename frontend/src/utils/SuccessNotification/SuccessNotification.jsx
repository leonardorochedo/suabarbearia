import { toast } from "react-toastify";

export async function SuccesNotification(msgText) {
    return new Promise((resolve) => {
        toast.success(msgText, {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
            onClose: resolve, // Resolve this promise when notification is closed
        });
    });
};