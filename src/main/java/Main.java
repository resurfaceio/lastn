// Copyright (c) 2016 Resurface Labs LLC, All Rights Reserved

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {

    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));

        LinkedList<String> queue = new LinkedList<>();
        ReentrantReadWriteLock queueLock = new ReentrantReadWriteLock();

        get("/read", (req, res) -> {
            StringBuilder sb = new StringBuilder();
            queueLock.readLock().lock();
            try {
                for (String json : queue) {
                    sb.append("<p><code>");
                    sb.append(json);
                    sb.append("</code></p>");
                }
            } finally {
                queueLock.readLock().unlock();
            }
            return sb.toString();
        });

        get("/write", (req, res) -> {
            queueLock.writeLock().lock();
            try {
                if (queue.size() == LIMIT) queue.removeLast();
                queue.addFirst(String.valueOf(System.currentTimeMillis()));
                return "OK";
            } finally {
                queueLock.writeLock().unlock();
            }
        });

    }

    private static final int LIMIT = 5;
}
