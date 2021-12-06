package com.swagger;

import com.swagger.extension.KeykcloakAuth;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class MercedesAuthTests {

    static {
        RestAssured.requestSpecification = new RequestSpecBuilder().log(LogDetail.ALL).build();
    }


    @RegisterExtension
    private final String keycloakAuth = new KeykcloakAuth().getAuthCode();


    private RequestSpecification getMBAuthApi() {
        return given().baseUri("https://id.mercedes-benz.com/")
                .basePath("ciam/auth/login/")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all();
    }

    public void checkUserEmail(UserData clientData) {
        getMBAuthApi()
                .body("{\"username\":\"texefim302@tinydef.com\"}")
                .post("/user")
                .prettyPeek();
    }

    public Response checkUserPasw(UserData clientData) {
        return getMBAuthApi()
                .body("{\"username\":\"texefim302@tinydef.com\",\"password\":\"J6CGkc+,6/Gk(+q\",\"rememberMe\":true}")
                .post("/pass");
    }

    void testLogin(UserData clientData) {
        System.out.println("openLoginForm() = " + openLoginForm(clientData));

        checkUserEmail(clientData);

        checkUserPasw(clientData).prettyPrint();
    }

    @Test
    @DisplayName("Login user wo env")
    void loginUserWoEnv() {

        testLogin(UserData.get());
    }


    private String openLoginForm(UserData clientData) {
        var locationHeaderValue = given()
                .baseUri("https://car-simulator.developer.mercedes-benz.com/auth/mercedes-benz")
                .redirects().follow(false)
                .get()
                .header("Location");

        Response response = given()
                .accept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Referer", "https://car-simulator.developer.mercedes-benz.com/")
                .header(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like" +
                                " " +
                                "Gecko) Chrome/96.0.4664.55 Safari/537.36"
                )
                .header("Accept-Language", "en,uk;q=0.9,ru;q=0.8")
                .headers(Map.of(
                        "sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"",
                        "sec-ch-ua-mobile", "?0",
                        "sec-ch-ua-platform", "macOS",
                        "Sec-Fetch-Dest", "document",
                        "Sec-Fetch-Mode", "navigate",
                        "Sec-Fetch-Site", "same-site",
                        "Sec-Fetch-User", "?1",
                        "Upgrade-Insecure-Requests", "1"
                ))
                .redirects().follow(false)
                .get(locationHeaderValue);
        var authCookie = response.detailedCookies();
        var resumeLocationValue = response.header("Location");

        response.prettyPrint();

        return given()
                .redirects().follow(false)
                .cookies(authCookie)
                .get(resumeLocationValue)
                .header("Set-Cookie");
    }


    @Getter
    public enum UserData {

        DEV("", null, "dsjf", null), SIT("", null, "", ""), PROD("", null, "", "");

        UserData(String clientId, String clientSecret, String login, String password) {
            this.clientId = clientId;
            this.clientSecret = System.getProperty("clientSecret");
            this.login = login;
            this.password = System.getProperty("clientpassword");
        }

        private final String clientId, clientSecret, login, password;

        public static UserData get() {
            return UserData.valueOf(System.getProperty("env"));
        }

    }


}
