package com.swagger.api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PetServiceErrors {


    PetIsDuplicated("E50001", "Pet is already presented in DB"), ExpiredPromo("E50010", "Promocode date is expired");

    private final String code, message;

}
