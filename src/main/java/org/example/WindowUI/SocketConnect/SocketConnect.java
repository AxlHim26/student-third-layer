package org.example.WindowUI.SocketConnect;

import org.example.BusLayer.Model.Student;
import org.example.BusLayer.Server.StreamSocket;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketConnect {
    private String host = "localhost";
    private int port = 1234;

    private Socket subscribeSocket;
    private StreamSocket subscribeStream;
    private Thread listenThread;
    private final CopyOnWriteArrayList<Consumer<List<Student>>> listeners = new CopyOnWriteArrayList<>();

    public void startSubscription() {
        if (listenThread != null && listenThread.isAlive()) return;
        listenThread = new Thread(() -> {
            while (true) {
                try {
                    subscribeSocket = new Socket(host, port);
                    subscribeStream = new StreamSocket(subscribeSocket);
                    subscribeStream.send("SUBSCRIBE");
                    Object ack = subscribeStream.receive();
                    if (!"SUBSCRIBED".equals(ack)) {
                        try { subscribeStream.close(); } catch (Exception ignore) {}
                        Thread.sleep(1000);
                        continue;
                    }
                    while (true) {
                        Object event = subscribeStream.receive();
                        if ("REFRESH".equals(event)) {
                            List<Student> list = (List<Student>) subscribeStream.receive();
                            for (Consumer<List<Student>> cb : listeners) {
                                try { cb.accept(list); } catch (Exception ignore) {}
                            }
                        }
                    }
                } catch (Exception e) {
                    try { if (subscribeStream != null) subscribeStream.close(); } catch (Exception ignore) {}
                    try { if (subscribeSocket != null) subscribeSocket.close(); } catch (Exception ignore) {}
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

    public void onStudentsUpdated(Consumer<List<Student>> callback) {
        listeners.add(callback);
    }


    public List<Student> getAllStudents() {
        try (Socket socket = new Socket(host, port)) {
            StreamSocket ss = new StreamSocket(socket);
            ss.send("GET_ALL");
            return (List<Student>) ss.receive();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addStudent(Student s) {
        try (Socket socket = new Socket(host, port)) {
            StreamSocket ss = new StreamSocket(socket);
            ss.send("ADD");
            ss.send(s);
            ss.receive();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(int id) {
        try (Socket socket = new Socket(host, port)) {
            StreamSocket ss = new StreamSocket(socket);
            ss.send("DELETE");
            ss.send(id);
            ss.receive();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
