package com.eascapeco.cinemapr.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 응답 결과를 저장하는 model
 *
 * @author jaehankim
 * @date 2019. 10. 10
 */
@Getter @Setter
public class Result {
    private int code;
    private String message;
    private Map<String, Object> info;

    public Result() {
        // TODO Auto-generated constructor stub
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
