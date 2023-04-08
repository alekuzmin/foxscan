package ru.foxscan.base;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class Runner implements Runnable {

    @Override
    public void run() {
        Response response = checkBaseGetTest();
        System.out.println(response.statusCode());
        System.out.println(response.getTime());
        }

    public Response checkBaseGetTest() {

        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

       Response response = given()
                .param("user", "123")
                .when()
                .get("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/auth/getToken");
       return response;

    }

}
