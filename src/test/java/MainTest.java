// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

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
    public void testReadAndWrite() throws IOException {
        get(preamble(0));
        post("");
        get(preamble(1) + textarea(null));
        post("B");
        get(preamble(2) + textarea("B") + textarea(null));
        post("C");
        get(preamble(3) + textarea("C") + textarea("B") + textarea(null));
        post("D");
        get(preamble(4) + textarea("D") + textarea("C") + textarea("B") + textarea(null));
        post("E");
        get(preamble(5) + textarea("E") + textarea("D") + textarea("C") + textarea("B") + textarea(null));
        post("F2");
        get(preamble(5) + textarea("F2") + textarea("E") + textarea("D") + textarea("C") + textarea("B"));
        post("G G!");
        get(preamble(5) + textarea("G G!") + textarea("F2") + textarea("E") + textarea("D") + textarea("C"));
    }

    private void get(String expected) throws IOException {
        String response = Request.Get(URL).execute().returnContent().asString();
        assertEquals(response, expected);
    }

    private void post(String body) throws IOException {
        HttpResponse response = Request.Post(URL).bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse();
        assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    private String preamble(int messages) {
        return "<head><style>textarea{width:800px;height:50px;}</style></head><h1>" + messages + " messages</h1>";
    }

    private String textarea(String content) {
        return "<p><textarea>" + (content == null ? "" : content) + "</textarea></p>";
    }

    private static final String URL = "http://localhost:9000/messages";

}
