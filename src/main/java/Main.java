// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static spark.Spark.*;

/**
 * Heroku app that tracks last n messages.
 */
public class Main {

    public static int N = 32;

    public static void main(String[] args) {
        String portstr = System.getenv("PORT");
        port(Integer.valueOf(portstr == null ? "9000" : portstr));

        LinkedList<String> queue = new LinkedList<>();
        ReentrantReadWriteLock queueLock = new ReentrantReadWriteLock();

        get("/messages", (request, response) -> {
            StringBuilder sb = new StringBuilder(1024);
            sb.append("<head><style>textarea{width:1200px;height:50px;}</style></head>");
            queueLock.readLock().lock();
            try {
                sb.append("<h1>").append(queue.size()).append(" messages</h1>");
                queue.forEach(item -> sb.append("<p><textarea>").append(item).append("</textarea></p>"));
            } finally {
                queueLock.readLock().unlock();
            }
            return sb.toString();
        });

        post("/messages", (request, response) -> {
            queueLock.writeLock().lock();
            try {
                queue.addFirst(request.body());
                if (queue.size() > N) queue.removeLast();
            } finally {
                queueLock.writeLock().unlock();
            }
            halt(200);
            return null;
        });
    }

}
