package com.swagger.api.controller;

import com.swagger.petstore.models.Pet;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetController extends BaseController {


    private RequestSpecification petApi() {
        return petStoreApiClient("/pet");
    }

    @Step("Add new pet to the store")
    public Response addNewPetToStore(Pet petDto) {
        return petApi().body(petDto).post();
    }

    public Response getPetById(Long targetPetId) {
        return petApi().get("/{targetPetId}", targetPetId);
    }

}
