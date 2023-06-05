package ru.foxscan.base;

import io.restassured.http.Cookies;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseSteps {

    protected final TestContext testContext = TestContext.getContext();
    //protected ValidatableResponse validatableResponse;

    //@Step("Авторизоваться в ЕФС")
    public Response auth(String login){
        login(login);
        getSession();
        return getAuth();
    }

    public Response getNote(String epkId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/tib-arm-holding-card/api/v1/note/" + epkId)
                .header("Content-Type", "application/json")
                .cookies(testContext.getCookies())
                .cookie(testContext.getCookie_pd_s())
                .cookie(testContext.getCookie_pd_id())
                .cookie("username",testContext.getUsername())
                .get();
    }

    //@Step("Получение информации по холдингу")
    public Response getHoldings(String epkId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/tib-arm-holding-card/api/v1/holdings/" + epkId)
                .header("Content-Type", "application/json")
                .cookies(testContext.getCookies())
                .cookie(testContext.getCookie_pd_s())
                .cookie(testContext.getCookie_pd_id())
                .cookie("username",testContext.getUsername())
                .get();

    }

    public Response getEmployee(String employeeId){
        return given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/tib-arm-holding-card/api/v1/employee/" + employeeId)
                .header("Content-Type", "application/json")
                .cookies(testContext.getCookies())
                .cookie(testContext.getCookie_pd_s())
                .cookie(testContext.getCookie_pd_id())
                .cookie("username",testContext.getUsername())
                .get();

    }

    public void login(String login){
        Cookies cookies = given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/pkmslogin.form")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", login)
                .formParam("password", "q12345678")
                .formParam("login-form-type", "pwd")
                .post()
                .then()
                .extract()
                .response()
                .detailedCookies();
        testContext.setCookie_pd_s(cookies.get("PD-S-SESSION-ID"));
        testContext.setCookie_pd_id(cookies.get("PD-ID"));
        testContext.setUsername(login);
    }

    public void getSession(){
        Cookies cookies = given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/rmkib.auth/api/v1/session")
                .header("Content-Type", "application/json")
                .cookie(testContext.getCookie_pd_s())
                .cookie(testContext.getCookie_pd_id())
                .get()
                .then()
                .extract()
                .response()
                .detailedCookies();
        testContext.setCookies(cookies);
        testContext.setCookie_ufs_session(cookies.get("UFS-SESSION"));
    }

    public Response getAuth(){
        return given()
                .relaxedHTTPSValidation()
                .baseUri("https://sb-enigma-ift.delta.sbrf.ru/rmkib.auth/api/v6/auth")
                .header("Content-Type", "application/json")
                .cookies(testContext.getCookies())
                .get();
    }

}
