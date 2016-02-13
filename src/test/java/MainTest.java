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
        Main.main(null);
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void testReadAndWrite() throws IOException {
        get("<h1>0 messages</h1>");
        post("");
        get("<h1>1 messages</h1><p><code></code></p>");
        post("B");
        get("<h1>2 messages</h1><p><code>B</code></p><p><code></code></p>");
        post("C");
        get("<h1>3 messages</h1><p><code>C</code></p><p><code>B</code></p><p><code></code></p>");
        post("D");
        get("<h1>4 messages</h1><p><code>D</code></p><p><code>C</code></p><p><code>B</code></p><p><code></code></p>");
        post("E");
        get("<h1>5 messages</h1><p><code>E</code></p><p><code>D</code></p><p><code>C</code></p><p><code>B</code></p><p><code></code></p>");
        post("F2");
        get("<h1>5 messages</h1><p><code>F2</code></p><p><code>E</code></p><p><code>D</code></p><p><code>C</code></p><p><code>B</code></p>");
        post("G G!");
        get("<h1>5 messages</h1><p><code>G G!</code></p><p><code>F2</code></p><p><code>E</code></p><p><code>D</code></p><p><code>C</code></p>");
    }

    private void get(String expected) throws IOException {
        String response = Request.Get(URL).execute().returnContent().asString();
        assertEquals(response, expected);
    }

    private void post(String body) throws IOException {
        HttpResponse response = Request.Post(URL).bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse();
        assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    private static final String URL = "http://localhost:9000/messages";

}
