package com.one.arpitInstituteAPI.response;

public class StandardResponse<T> {
    private boolean status;
    private String message;
    private T data;

    public StandardResponse() {}

    public StandardResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> StandardResponse<T> success(String message, T data) {
        return new StandardResponse<>(true, message, data);
    }

    public static <T> StandardResponse<T> error(String message) {
        return new StandardResponse<>(false, message, null);
    }


    // ✅ Add this overload to support (message, data)
    public static <T> StandardResponse<T> error(String message, T data) {
        return new StandardResponse<>(false, message, data);
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}