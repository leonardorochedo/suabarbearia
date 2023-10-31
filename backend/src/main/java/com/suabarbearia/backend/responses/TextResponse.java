package com.suabarbearia.backend.responses;

public class TextResponse {

    private String message;

    public TextResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
