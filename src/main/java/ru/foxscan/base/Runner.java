package ru.foxscan.base;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;


public class Runner implements Runnable {

    @Override
    public void run() {
        Response auth = getAuth();

        if(!auth.then().extract().contentType().equals("text/html")) {

            Response holdings = getHoldings();
            Response employee = getEmployee();
            Response notes = getNotes();
            DBUtils.openConnection();
            DBUtils.writeResponseResult(
                    "getNote",
                    "PPRB.CS",
                    notes.then().extract().body().path("success").toString(),
                    notes.statusCode(),
                    (int) notes.getTime());
            DBUtils.writeResponseResult(
                        "getHoldings",
                        "EPK",
                        holdings.then().extract().body().path("success").toString(),
                        holdings.statusCode(),
                        (int) holdings.getTime());

            DBUtils.writeResponseResult(
                        "getEmployee",
                        "EPC",
                        employee.then().extract().body().path("success").toString(),
                        employee.statusCode(),
                        (int) holdings.getTime());

            DBUtils.writeResponseResult(
                        "getAuth",
                        "auth",
                        "true",
                        200,
                        (int) auth.getTime());
        } else {
                DBUtils.writeResponseResult(
                        "getAuth",
                        "auth",
                        "false",
                        200,
                        (int) auth.getTime());
        }

    }

    public Response getAuth(){
        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        BaseSteps baseSteps = new BaseSteps();
        return baseSteps.auth("TIB_4");

    }

    public Response getHoldings() {
        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        BaseSteps baseSteps = new BaseSteps();
        baseSteps.auth("TIB_4");
        return baseSteps.getHoldings("1774284400545669839");
    }

    public Response getNotes() {
        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        BaseSteps baseSteps = new BaseSteps();
        baseSteps.auth("TIB_4");
        return baseSteps.getNote("1774284400545669839");
    }

    public Response getEmployee() {
        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        BaseSteps baseSteps = new BaseSteps();
        baseSteps.auth("TIB_4");
        return baseSteps.getEmployee("1656291");
    }

}
