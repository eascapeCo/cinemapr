package com.eascapeco.cinemapr.api.constants;

public enum ErrorCode {
    NOT_PARAMETER("ERROR_CODE_0000","Not Parameter"),
    NOT_NULL("ERROR_CODE_0001","필수값이 누락되었습니다"),
    MIN_VALUE("ERROR_CODE_0002", "최소값보다 커야 합니다."),
    MAX_VALUE("ERROR_CODE_0003", "최대값보다 작아야 합니다."),
    BAD_REQUEST("ERROR_CODE_0004", "Bad Request");

    private String code;
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
