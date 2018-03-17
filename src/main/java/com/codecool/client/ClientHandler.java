package com.codecool.client;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Socket socket;
    private RecieveMessage recieve;


    public ClientHandler(Socket socket, RecieveMessage recieve) {
        this.socket = socket;
        this.recieve = recieve;
    }

    public void run() throws IOException{
        recieve.start();
    }
}
