// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests against REST service that tracks last n messages.
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

    private static final String URL = "http://localhost:9000/messages";

    private String get() throws IOException {
        return Request.Get(URL).execute().returnContent().asString();
    }

    private int post(String body) throws IOException {
        return Request.Post(URL).bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse().getStatusLine().getStatusCode();
    }

    @Test
    public void testReadAndWrite() throws IOException {
        assertEquals(get(), "");
        assertEquals(post("A"), 200);
        assertEquals(get(), "<p><code>A</code></p>");
        assertEquals(post("B"), 200);
        assertEquals(get(), "<p><code>B</code></p><p><code>A</code></p>");
        assertEquals(post("C"), 200);
        assertEquals(get(), "<p><code>C</code></p><p><code>B</code></p><p><code>A</code></p>");
        assertEquals(post("D"), 200);
        assertEquals(get(), "<p><code>D</code></p><p><code>C</code></p><p><code>B</code></p><p><code>A</code></p>");
        assertEquals(post("E"), 200);
        assertEquals(get(), "<p><code>E</code></p><p><code>D</code></p><p><code>C</code></p><p><code>B</code></p><p><code>A</code></p>");
        assertEquals(post("F2"), 200);
        assertEquals(get(), "<p><code>F2</code></p><p><code>E</code></p><p><code>D</code></p><p><code>C</code></p><p><code>B</code></p>");
        assertEquals(post("G G!"), 200);
        assertEquals(get(), "<p><code>G G!</code></p><p><code>F2</code></p><p><code>E</code></p><p><code>D</code></p><p><code>C</code></p>");
    }

}
