package com.frogcrew.frogcrew.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean flag;
    private Integer code;
    private String message;
    private T data;

    // Primary signature based on OpenAPI spec (message first, then data)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    // Simple signature for convenience (data only)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "Success", data);
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }

    // Helper method for common error cases
    public static <T> ApiResponse<T> badRequest(String message, T details) {
        return new ApiResponse<>(false, 400, message, details);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, 404, message, null);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(false, 409, message, null);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(false, 401, message, null);
    }

    // Helper method to handle String codes (for backward compatibility)
    public static <T> ApiResponse<T> error(String codeStr, String message) {
        Integer code = 500;
        try {
            code = Integer.parseInt(codeStr);
        } catch (NumberFormatException e) {
            // Default to 500 if not parseable
        }
        return error(code, message);
    }
}