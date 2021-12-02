package com.swagger.api.controller.common;

import com.swagger.api.controller.PetServiceErrors;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;


public class ResponseAssertion {

    private final Response targetResponse;

    //todo try to create and use kotlin extension function to assert responses
    public ResponseAssertion(Response targetResponse) {
        this.targetResponse = targetResponse;
    }

    public ResponseAssertion statusCodeIsEqualTo(ResponseExpectMessages.StatusCode expectedStatusCode) {
        var statusCodeAssertionMessage = new ResponseExpectMessages(targetResponse)
                .expectedStatusCode(expectedStatusCode);

        Assertions.assertThat(targetResponse.statusCode())
                .withFailMessage(statusCodeAssertionMessage)
                .isEqualTo(expectedStatusCode.code);
        return this;
    }

    public ResponseAssertion errorMessageIsEqual(PetServiceErrors targetError) {
        var expectedErrorResponse = ErrorReponse.builder()
                .code(targetError.getCode())
                .message(targetError.getMessage())
                .build();

        Assertions.assertThat(bindAs(ErrorReponse.class))
                .isEqualToIgnoringGivenFields("stackTrace")
                .isEqualTo(expectedErrorResponse);

        return this;
    }

    /**
     * Assert possibility response body binding to target class
     *
     * @return Binded response body to target object
     */
    public <T> T bindAs(Class<T> expectedClass) {
        T convertedObject;
        try {
            convertedObject = targetResponse.as(expectedClass);
        } catch (Exception ex) {
            var assertionMessage = new ResponseExpectMessages(targetResponse)
                    .expectedResponseBodyClass(expectedClass);
            throw new AssertionError(assertionMessage);
        }
        return convertedObject;
    }

    public <T> T[] bindAsListOf(Class<T[]> expectedClass) {
        return bindAs(expectedClass);
    }

    public void responseIsEqualToObjectIgnoringTimeFields(Object expectedObject) {
        var objectUnderTest = bindAs(expectedObject.getClass());

        Assertions.assertThat(objectUnderTest)
                .isEqualToIgnoringGivenFields(expectedObject);
    }

    public void responseIsEmpty() {
        Assertions.assertThat(targetResponse.asString()).isEmpty();
    }

    public void responseIsEmptyArray() {
        Assertions.assertThat(targetResponse.asString()).isEqualTo("[]");
    }

    public ResponseAssertion responseFieldsAreEqualTo(Object actualResponse, String... fieldsTOAssert) {
        Assertions.assertThat(actualResponse)
                .isEqualToComparingFieldByField(fieldsTOAssert);
        return this;
    }

}
