package com.eascapeco.cinemapr.bo.controller.exception;

import com.eascapeco.cinemapr.api.constants.ErrorCode;
import com.eascapeco.cinemapr.api.exception.BadRequestException;
import com.eascapeco.cinemapr.api.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j

@ControllerAdvice
public class ErrorControllerAdvice {
    /**
     * validataion exception handling
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException 발생 url:{}",request.getRequestURI());

        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * RequestBody annotation의 required가 true인데 해당 값이 없는 경우에 발생하는 exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("HttpMessageNotReadableException 발생 url:{}", request.getRequestURI());
        log.error("{}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_PARAMETER.getCode(), ErrorCode.NOT_PARAMETER.getDescription());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 잘못된 요청을 했을 경우에 발생하는 exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequestException(BadRequestException e, HttpServletRequest request) {
        log.error("BadRequestException 발생 url:{}", request.getRequestURI());
        log.error("{}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getDescription(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * validataion case handling
     *
     * @param bindingResult
     * @return
     */
    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        String code = "";
        String description = "";
        String detail = "";

        // 에러가 있다면
        if(bindingResult.hasErrors()){
            // model에 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getDefaultMessage();

            // model에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (bindResultCode){
                case "NotNull":
                    code = ErrorCode.NOT_NULL.getCode();
                    description = ErrorCode.NOT_NULL.getDescription();
                    break;
                case "Min":
                    code = ErrorCode.MIN_VALUE.getCode();
                    description = ErrorCode.MIN_VALUE.getDescription();
                    break;
                case "Max":
                    code = ErrorCode.MAX_VALUE.getCode();
                    description = ErrorCode.MAX_VALUE.getDescription();
                    break;
            }
        }

        return new ErrorResponse(code, description, detail);
    }
}
