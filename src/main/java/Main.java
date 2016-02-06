// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static spark.Spark.*;

/**
 * REST service that tracks last n messages.
 */
public class Main {

    public static void main(String[] args) {
        String portstr = System.getenv("PORT");
        port(Integer.valueOf(portstr == null ? "9000" : portstr));

        LinkedList<String> queue = new LinkedList<>();
        ReentrantReadWriteLock queueLock = new ReentrantReadWriteLock();

        get("/messages", (request, response) -> {
            StringBuilder sb = new StringBuilder(1024);
            queueLock.readLock().lock();
            try {
                for (String s : queue) {
                    sb.append("<p><code>");
                    sb.append(s);
                    sb.append("</code></p>");
                }
            } finally {
                queueLock.readLock().unlock();
            }
            return sb.toString();
        });

        post("/messages", (request, response) -> {
            queueLock.writeLock().lock();
            try {
                queue.addFirst(request.body());
                if (queue.size() == 6) queue.removeLast();
            } finally {
                queueLock.writeLock().unlock();
            }
            halt(200);
            return null;
        });

    }
}
