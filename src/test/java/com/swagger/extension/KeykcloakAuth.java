package com.swagger.extension;

import com.swagger.MercedesAuthTests;
import lombok.Getter;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


@Getter
public class KeykcloakAuth implements BeforeAllCallback {

    private String authCode;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        new MersAuth().auth(MercedesAuthTests.UserData.get());
    }

    private class MersAuth {

        public void auth(MercedesAuthTests.UserData userData) {

        }

    }

}
