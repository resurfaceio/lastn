// Copyright (c) 2016-2017 Resurface Labs LLC, All Rights Reserved

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
        assertEquals(response, expected);
    }

    private void post(String body) throws IOException {
        HttpResponse response = Request.Post(URL).bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse();
        assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    private String prefix(int count) {
        return "{\"count\":\"" + count + "\",\"messages\":[";
    }

    private void reset() throws IOException {
        String response = Request.Get(URL + "/reset").execute().returnContent().asString();
        assertEquals(response, "RESET OK");
    }

    private static final String POSTFIX = "]}";
    private static final String URL = "http://localhost:9000/messages";

}
