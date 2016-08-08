package com.jlshix.wlife_v03.data;

import java.io.Serializable;

public class RegisterModel implements Serializable {
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}

