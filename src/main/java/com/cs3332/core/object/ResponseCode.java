package com.cs3332.core.object;

public enum ResponseCode {
    // 2xx Success
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),

    // 3xx Redirection
    MOVED_PERMANENTLY(301),
    FOUND(302),
    NOT_MODIFIED(304),

    // 4xx Client errors
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    UNSUPPORTED_MEDIA_TYPE(415),
    UNPROCESSABLE_ENTITY(422),

    // 5xx Server errors
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503);

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ResponseCode fromCode(int code) {
        for (ResponseCode rc : values()) {
            if (rc.code == code) {
                return rc;
            }
        }
        return null; // or throw an exception if preferred
    }
}

