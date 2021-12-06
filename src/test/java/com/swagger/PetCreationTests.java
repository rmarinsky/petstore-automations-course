package com.swagger;

import com.github.javafaker.Faker;
import com.swagger.api.controller.PetController;
import com.swagger.api.controller.PetServiceErrors;
import com.swagger.api.controller.common.ResponseExpectMessages;
import com.swagger.petstore.models.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.requestSpecification;

public class PetCreationTests {

    static {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .addHeader("X-Tracing-Id", UUID.randomUUID().toString())
                .build();
    }

    Faker faker = new Faker();
    PetController petController = new PetController().withToken(faker.number().digit());

    @Test
    @DisplayName("Creation of a new pet via API")
    void creationOfANewPetViaApi() {
        String targetPetName = faker.name().name();
        Pet targetPet = new Pet()
                .name(targetPetName)
                .id(faker.number().randomNumber());

        var createPetResponse = petController
                .addNewPetToStore(targetPet);

        Assertions.assertEquals(200, createPetResponse.statusCode());

        petController.getPetById(targetPet.getId())
                .statusCodeIsEqualTo(ResponseExpectMessages.StatusCode.CONFLICT)
                .errorMessageIsEqual(PetServiceErrors.ExpiredPromo)
                .responseFieldsAreEqualTo(new Pet(), "date", "id");
    }

}
