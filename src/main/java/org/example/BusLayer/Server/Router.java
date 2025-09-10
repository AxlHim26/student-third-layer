package org.example.BusLayer.Server;

import org.example.BusLayer.Handle.StudentBUS;
import org.example.BusLayer.Model.Student;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Router {
    private static final CopyOnWriteArrayList<StreamSocket> subscribers = new CopyOnWriteArrayList<>();

    private static void broadcastRefresh(StudentBUS bus) {
        try {
            List<Student> list = bus.getAllStudents();
            for (StreamSocket subscriber : subscribers) {
                try {
                    subscriber.send("REFRESH");
                    subscriber.send(list);
                } catch (Exception ex) {
                    try { subscriber.close(); } catch (Exception ignore) {}
                    subscribers.remove(subscriber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Router() throws Exception {
        ServerSocket server = new ServerSocket(1234);
        StudentBUS bus = new StudentBUS();
        System.out.println("Server started on port 1234");

        while (true) {
            Socket client = server.accept();
            new Thread(() -> {
                try {
                    StreamSocket ss = new StreamSocket(client);
                    while (true) {
                        String command = (String) ss.receive();
                        if ("GET_ALL".equals(command)) {
                            List<Student> list = bus.getAllStudents();
                            ss.send(list);
                        } else if ("SUBSCRIBE".equals(command)) {
                            subscribers.add(ss);
                            ss.send("SUBSCRIBED");
                            // Keep the connection open; optionally read until client exits
                        
                        } else if ("ADD".equals(command)) {
                            Student s = (Student) ss.receive();
                            bus.addStudent(s);
                            ss.send("OK");
                            broadcastRefresh(bus);
                        } else if ("UPDATE".equals(command)) {
                            Student s = (Student) ss.receive();
                            bus.updateStudent(s);
                            ss.send("OK");
                            broadcastRefresh(bus);
                        } else if ("DELETE".equals(command)) {
                            int id = (int) ss.receive();
                            bus.deleteStudent(id);
                            ss.send("OK");
                            broadcastRefresh(bus);
                        } else if ("EXIT".equals(command)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
