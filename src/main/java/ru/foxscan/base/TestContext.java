package ru.foxscan.base;

import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestContext {
    private Cookies cookies;
    private Cookie cookie_pd_id;
    private Cookie cookie_pd_s;
    private Cookie cookie_ufs_session;
    private String username;


    private static TestContext instance;


    public static TestContext getContext() {
        if (instance == null) {
            instance = new TestContext();
        }
        return instance;
    }

}
