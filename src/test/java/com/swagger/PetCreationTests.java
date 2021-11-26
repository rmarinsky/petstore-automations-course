package com.swagger;

import com.github.javafaker.Faker;
import com.swagger.api.controller.PetController;
import com.swagger.api.model.PetDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.requestSpecification;

public class PetCreationTests {

    static {
        requestSpecification = new RequestSpecBuilder().log(LogDetail.ALL).build();
    }

    Faker faker = new Faker();
    PetController petController = new PetController();

    @Test
    @DisplayName("Creation of a new pet via API")
    void creationOfANewPetViaApi() {
        String targetPetName = faker.name().name();
        PetDto targetPet = PetDto.builder().name(targetPetName).id(faker.number().randomNumber()).build();

        var createPetResponse = petController
                .addNewPetToStore(targetPet);

        Assertions.assertEquals(200, createPetResponse.statusCode());

        var petByIdResponse = petController.getPetById(targetPet.getId());
        PetDto actualPet = petByIdResponse.as(PetDto.class);

        Assertions.assertEquals(targetPet, actualPet);
        Assertions.assertEquals(200, petByIdResponse.statusCode());
    }

    private void extracted(PetDto targetPetName, PetDto actualPet) {
        Assertions.assertEquals(targetPetName, actualPet.getName());
        Assertions.assertEquals(targetPetName, actualPet.getName());
        Assertions.assertEquals(targetPetName, actualPet.getName());
    }

}
