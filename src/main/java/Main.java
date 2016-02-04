// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {

    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        get("/hello", (req, res) -> "Hello World from resurfaceio-lastn");
    }

}
