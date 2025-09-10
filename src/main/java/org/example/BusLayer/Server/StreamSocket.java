package org.example.BusLayer.Server;

import java.io.*;
import java.net.Socket;

public class StreamSocket {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public StreamSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Object obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void close() throws IOException {
        socket.close();
    }
}
