import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.io.IOException;
import static org.testng.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HttpClientTest {
    private static final String API_KEY = "ca829860e6a92b9934a8577f0bd2bbab";
    private static final String API_TOKEN = "2d853b7060d6c732ddfc1c13904ac5bc5a2ad792d3e628546ac60e1d6b415dba";
    private static final String BOARD_ID = "62b762f1c9776c8140710beb";
    private static final String BASE_URI = "https://api.trello.com/1/";

    private static String LIST_ID = "";
    private static String CARD_ID = "";

    @Test
    public void test1_GetBoardListID() throws IOException {
        HttpUriRequest request = new HttpGet(BASE_URI + "boards/" + BOARD_ID + "/lists?key="+API_KEY+"&token="+API_TOKEN);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK,
                response.getStatusLine().getStatusCode());

        String result = EntityUtils.toString(response.getEntity());
        this.LIST_ID = ((JSONObject) new JSONArray(result).get(0)).get("id").toString();    }

    @Test
    public void test2_addCard() throws IOException, InterruptedException {
        String card_name = "test_card";
        String card_desc = "test_description";
        String url = BASE_URI + "cards/?idList=" + LIST_ID + "&key="+API_KEY+"&token="+API_TOKEN;
        url += "&name="+card_name+"&desc="+card_desc;
        HttpUriRequest request = new HttpPost(url);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK,
                response.getStatusLine().getStatusCode());
        Thread.sleep(5000); // it was made only for see it in bowser

        String result = EntityUtils.toString(response.getEntity());
        this.CARD_ID = (new JSONObject(result)).get("id").toString();
    }

    @Test
    public void test3_archiveCard() throws IOException, InterruptedException {
        String url = BASE_URI + "cards/"+CARD_ID + "?key="+API_KEY+"&token="+API_TOKEN;
        url += "&closed=true";
        HttpUriRequest request = new HttpPut(url);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK,
                response.getStatusLine().getStatusCode());
        Thread.sleep(5000); // it was made only for see it in bowser
    }

    @Test
    public void test4_delCard() throws IOException {
        String url = BASE_URI + "cards/"+CARD_ID + "?key="+API_KEY+"&token="+API_TOKEN;
        HttpUriRequest request = new HttpDelete(url);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK,
                response.getStatusLine().getStatusCode());
    }

}
