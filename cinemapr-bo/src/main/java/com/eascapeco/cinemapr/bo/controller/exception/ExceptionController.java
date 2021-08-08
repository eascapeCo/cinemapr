package com.eascapeco.cinemapr.bo.controller.exception;

import com.eascapeco.cinemapr.api.model.dto.Result;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController implements ErrorController {

    @GetMapping(value = "/entrypoint")
    public Result entrypointException() {
        throw new RuntimeException();
    }

    @GetMapping("/error")
    public String redirect() {
        return "index";
    }

}