package org.example.demo1.domain.dto;

public class ServiceResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int statusCode;

    private ServiceResponse( boolean success, T data, String message, int statusCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static<T> ServiceResponse<T> success(T data, String message) {
        return new ServiceResponse<>(true,data,message,201);
    }

    public static <T> ServiceResponse<T> error(String message, int statusCode) {
        return new ServiceResponse<>(false, null, message, statusCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
