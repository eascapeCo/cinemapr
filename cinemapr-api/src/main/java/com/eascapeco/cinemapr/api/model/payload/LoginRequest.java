package com.eascapeco.cinemapr.api.model.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter @RequiredArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "Login Username can be null but not blank")
    private String admId;

    @NotNull(message = "Login password cannot be blank")
    private String pwd;

}
