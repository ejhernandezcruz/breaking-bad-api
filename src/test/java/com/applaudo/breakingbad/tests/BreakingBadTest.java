package com.applaudo.breakingbad.tests;


import com.applaudo.breakingbad.model.CharacterSeries;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
public class BreakingBadTest {

    private static final String BASE_URL = "https://www.breakingbadapi.com/api/characters/";
    public static final String WALTER_WHITE_ID = "1";
    private RequestSpecification requestSpec;

    @BeforeClass
    public void setUp() {
        requestSpec = new RequestSpecBuilder().
                setBaseUri(BASE_URL).
                build();
    }

    @Test(testName = "Get Walter White information")
    public void walterWhiteTest() {
        Response response = given().spec(requestSpec)
                .when().get(WALTER_WHITE_ID)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().response();
        CharacterSeries[] characters = getCharacters(response);
        String birthday = String.format("Birthday: %s", characters[0].getBirthday());
        System.out.println(birthday);
    }

    @Test(testName = "Get all characters information")
    public void allCharactersTest() {
        Response response = given().spec(requestSpec)
                .when().get()
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
        List<CharacterSeries> characters = Arrays.asList(getCharacters(response));
        characters.forEach(this::printCharacterSeries);
    }

    private CharacterSeries[] getCharacters(Response response) {
        return response.jsonPath().getObject("$", CharacterSeries[].class);
    }

    private void printCharacterSeries(CharacterSeries character) {
        String name = String.format("Character name: \"%s\"", character.getName());
        String portrayed = String.format("Portrayed: \"%s\"", character.getPortrayed());
        String lineSeparator = "------------------------------------------------------";
        System.out.println(name);
        System.out.println(portrayed);
        System.out.println(lineSeparator);
    }
}
