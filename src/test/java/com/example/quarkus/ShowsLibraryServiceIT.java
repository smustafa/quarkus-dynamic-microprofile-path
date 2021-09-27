package com.example.quarkus;

import com.example.quarkus.services.ShowsLibraryService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import org.hamcrest.core.IsEqual;


@QuarkusTest
class ShowsLibraryServiceIT {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setupBeforeAll() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.checkForUnmatchedRequests();
        wireMockServer.start();

        stubFor(get(urlPathMatching("/v1/singlesearch/shows"))
                .withQueryParam("q", equalTo("girls"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                " {\"id\":\"139\",\"url\":\"https://www.tvmaze.com/shows/139/girls\"," +
                                        "\"name\":\"V1ResponseGirls\",\"type\":\"Scripted\"," +
                                        "\"language\":\"English\",\"genres\":[\"Drama\",\"Romance\"],\"runtime\":30,\"averageRunTime\":null,\"status\":\"Ended\"}"
                        )));

        stubFor(get(urlPathMatching("/v2/singlesearch/shows"))
                .withQueryParam("q", equalTo("girls"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                " {\"id\":\"139\",\"url\":\"https://www.tvmaze.com/shows/139/girls\"," +
                                        "\"name\":\"V2ResponseGirls\",\"type\":\"Scripted\"," +
                                        "\"language\":\"English\",\"genres\":[\"Drama\",\"Romance\"],\"runtime\":30,\"averageRunTime\":null,\"status\":\"Ended\"}"
                        )));
    }

    @AfterAll
    public static void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void validateDynamicURL_towardsV1_Configuration() throws MalformedURLException {

       RestAssured.given()
                .queryParam("showName", "girls")
                .when().get("/v1/library")
                .then()
                .statusCode(200)
                .body("name", IsEqual.equalTo("V1ResponseGirls"));
    }


    @Test
    public void validateDynamicURL_towardsV2_Configuration() throws MalformedURLException {

        RestAssured.given()
                .queryParam("showName", "girls")
                .header("baseURL", "http://localhost:8080/v2")
                .when().get("/v1/library")
                .then()
                .statusCode(200)
                .body("name", IsEqual.equalTo("V2ResponseGirls"));
    }
}