package com.codecool.client;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Socket socket;
    private RecieveMessage recieve;
    private SendMessage send;

    public ClientHandler(Socket socket, RecieveMessage recieve, SendMessage send) {
        this.socket = socket;
        this.recieve = recieve;
        this.send = send;
    }

    public void run() throws IOException{
        send.start();
        recieve.start();
    }
}
