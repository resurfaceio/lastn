// © 2016-2018 Resurface Labs LLC

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;

import static com.mscharhag.oleaster.matcher.Matchers.expect;

/**
 * Tests against Heroku app that tracks last n messages.
 */
public class MainTest {

    @BeforeClass
    public static void beforeClass() {
        Main.N = 5;
        Main.main(null);
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void testReadAndWriteAndReset() throws IOException {
        get(prefix(0) + POSTFIX);
        post("");
        get(prefix(1) + POSTFIX);
        post("B");
        get(prefix(2) + "B," + POSTFIX);
        post("C");
        get(prefix(3) + "C,B," + POSTFIX);
        post("D");
        get(prefix(4) + "D,C,B," + POSTFIX);
        post("E");
        get(prefix(5) + "E,D,C,B," + POSTFIX);
        post("F2");
        get(prefix(5) + "F2,E,D,C,B" + POSTFIX);
        post("G G!");
        get(prefix(5) + "G G!,F2,E,D,C" + POSTFIX);
        reset();
        get(prefix(0) + POSTFIX);
    }

    private void get(String expected) throws IOException {
        String response = Request.Get(URL).execute().returnContent().asString();
        expect(response).toEqual(expected);
    }

    private void post(String body) throws IOException {
        HttpResponse response = Request.Post(URL).bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse();
        expect(response.getStatusLine().getStatusCode()).toEqual(204);
    }

    private String prefix(int count) {
        return "{\"count\":\"" + count + "\",\"messages\":[";
    }

    private void reset() throws IOException {
        String response = Request.Get(URL + "/reset").execute().returnContent().asString();
        expect(response).toEqual("RESET OK");
    }

    private static final String POSTFIX = "]}";
    private static final String URL = "http://localhost:9000/messages";

}
