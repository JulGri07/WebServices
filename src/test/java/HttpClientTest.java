import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HttpClientTest {
    private static final String API_KEY = "ca829860e6a92b9934a8577f0bd2bbab";
    private static final String API_TOKEN = "2d853b7060d6c732ddfc1c13904ac5bc5a2ad792d3e628546ac60e1d6b415dba";
    private static final String BOARD_ID = "62b762f1c9776c8140710beb";
    private static final String BASE_URI = "https://api.trello.com/1/";

    private static String LIST_ID = "";
    private static String CARD_ID = "";

    @DataProvider(name="inputData")
    public Object[][] inputData() {
        return new Object[][] {{"test_trello_card","test_test_test_test"}};
    }

    @Test
    public void test1_GetBoardListID() {
        LIST_ID = given().
                queryParam("key", API_KEY).
                queryParam("token", API_TOKEN).
                pathParam("board_id", BOARD_ID).
        when().
                get(BASE_URI + "boards/{board_id}/lists").
        then().
                statusCode(200).
        extract().
                jsonPath().getList("id").get(0).toString();
    }

    @Test(dependsOnMethods = "test1_GetBoardListID",dataProvider = "inputData")
    public void test2_addCard(String[] inputData) throws InterruptedException {
        String card_name = inputData[0];
        String card_desc = inputData[1];

        CARD_ID = given().
                queryParam("idList", LIST_ID).
                queryParam("key", API_KEY).
                queryParam("token", API_TOKEN).
                queryParam("name", card_name).
                queryParam("desc", card_desc).
                noContentType().
        when().
                post(BASE_URI + "cards").
        then().
                statusCode(200).
        extract().
                jsonPath().get("id").toString();
        Thread.sleep(5000); // it was made only for see it in browser
    }

    @Test(dependsOnMethods = "test2_addCard")
    public void test3_archiveCard() throws InterruptedException  {
        given().pathParam("card_id", CARD_ID).
                queryParam("key", API_KEY).
                queryParam("token", API_TOKEN).
                queryParam("closed","true").
        when().
                put(BASE_URI + "cards/{card_id}").
        then().
                statusCode(200);
        Thread.sleep(5000); // it was made only for see it in browser
    }

    @Test(dependsOnMethods = "test3_archiveCard")
    public void test4_delCard() {
        given().pathParam("card_id", CARD_ID).
                queryParam("key", API_KEY).
                queryParam("token", API_TOKEN).
        when().
                delete(BASE_URI + "cards/{card_id}").
        then().
                statusCode(200);
    }

}
