import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class HttpClientTest {
    public static final String BASE_URI = "https://reqres.in/";
    public static final String LIST_USERS_URI = BASE_URI + "api/users?page=2";

    @Test
    public void getListUsers() throws IOException {
        HttpUriRequest request = new HttpGet(LIST_USERS_URI);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK,
                response.getStatusLine().getStatusCode());
    }
}
