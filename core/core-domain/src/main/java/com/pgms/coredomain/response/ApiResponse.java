package com.pgms.coredomain.response;

public record ApiResponse<T>(String message, T data) {
    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>("ok", result);
    }

    public static <T> ApiResponse<T> created(T result) {
        return new ApiResponse<>("created", result);
    }
}
