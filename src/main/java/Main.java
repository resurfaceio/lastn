// © 2016-2019 Resurface Labs Inc.

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.Inflater;

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
            String body;
            if (!"deflated".equals(request.headers("Content-Encoding"))) {
                System.out.println("Request is not deflated");
                body = request.body();
            } else {
                System.out.println("Attempting to deflate");
                try {
                    Inflater inflater = new Inflater();
                    inflater.setInput(request.bodyAsBytes());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[64];
                    while (!inflater.finished()) baos.write(buffer, 0, inflater.inflate(buffer));
                    body = baos.toString();
                } catch (Throwable t) {
                    throw halt(500);
                }
            }

            queueLock.writeLock().lock();
            try {
                queue.addFirst(body);
                if (queue.size() > N) queue.removeLast();
            } finally {
                queueLock.writeLock().unlock();
            }
            throw halt(204);
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
