package com.swagger.api.controller;

import com.swagger.api.controller.common.ResponseAssertion;
import com.swagger.petstore.models.Pet;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetController extends BaseController<PetController> {


    private RequestSpecification petApi() {
        return petStoreApiClient("/pet");
    }

    @Step("Add new pet to the store")
    public Response addNewPetToStore(Pet petDto) {
        return petApi().body(petDto).post();
    }

    public ResponseAssertion getPetById(Long targetPetId) {
        var getPetResponse = petApi().get("/{targetPetId}", targetPetId);
        return new ResponseAssertion(getPetResponse);
    }

    @Step("Updat besdfklj by id")
    public ResponseAssertion updatBesdfkljById(Long id) {
        var updatBesdfkljByIdResponse = petApi().post();
        return new ResponseAssertion(updatBesdfkljByIdResponse);
    }

}
