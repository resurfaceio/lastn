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
            response.type("text/plain");
            StringBuilder sb = new StringBuilder(1024);
            sb.append("{");
            queueLock.readLock().lock();
            try {
                sb.append("\"count\":\"").append(queue.size()).append("\",\"messages\":[");
                int idx = 0;
                for (String message : queue) sb.append(idx++ == 0 ? "" : ',').append(message);
                sb.append("]}");
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

        get("/messages/reset", (request, response) -> {
            queueLock.writeLock().lock();
            try {
                queue.clear();
            } finally {
                queueLock.writeLock().unlock();
            }
            return "RESET OK";
        });

    }

}
